package edu.ecnu.yjsy.service.xj;

import edu.ecnu.yjsy.model.student.Registration;
import edu.ecnu.yjsy.model.student.RegistrationRespository;
import edu.ecnu.yjsy.model.student.Student;
import edu.ecnu.yjsy.model.student.StudentRepository;
import edu.ecnu.yjsy.service.BaseService;
import edu.ecnu.yjsy.service.search.SearchSQLService;
import edu.ecnu.yjsy.service.search.processor.CountProcessor;
import edu.ecnu.yjsy.service.search.processor.DynamicDiffProcessor;
import edu.ecnu.yjsy.service.search.processor.FeeProcessor;
import edu.ecnu.yjsy.service.search.processor.RegistrationProcessor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * <code>每学年</code>研究生需要完成缴费。
 * <p>
 * 主要业务逻辑如下：
 * <p>
 * 1. 在每学期开始之前重置缴费状态
 * <p>
 * 2. 学籍管理员从财务处获取缴费文件，通过文件上传的模式导入学籍系统
 * <p>
 * 3. 对于<code>助困生</code>，系统自动为该生完成缴费操作（即将缴费状态置为真）
 * <p>
 * 4. 依据查询条件，统计缴费情况
 * <p>
 * 5. 依据查询条件，导出缴费信息（实现参见<code>RegistrationService</code>)
 * <p>
 * FIXME 1. 考虑 全日制 与 非全日制 的收费差异：收费时间不一样，收费方式不一样（有可能提前一次性收取）
 * <p>
 * 2. 考虑学工部和财务处随时查询收费标准和缴费情况
 * <p>
 * 3. 如果新生推迟入学的情况下，已经完成了缴费，需要如何处理
 *
 * @author liyanbin
 * @author xulinhao
 * 
 * @see RegistrationService
 * @see CheckinService
 */

@Service
public class FeeService extends BaseService {

    private static final String[] FILE_FIELDS = { "学号", "缴费", "应缴学费", "实缴学费",
            "缴费日期", "学年", "学期" };

    // 报到注册缴费使用了一张流水表
    @Autowired
    private RegistrationRespository registrationRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    @Override
    public void initConditionSearch(SearchSQLService searchSQLService) {
        exec = searchSQLService.defaultSearch()
                .addProcessor(new RegistrationProcessor())
                .addProcessor(new DynamicDiffProcessor())
                .addProcessor(new FeeProcessor()).create();
        countExec = searchSQLService.defaultSearch()
                .addProcessor(new RegistrationProcessor())
                .addProcessor(new DynamicDiffProcessor())
                .addProcessor(new FeeProcessor())
                .addProcessor(new CountProcessor()).create();
    }

    public Boolean upload(InputStream is, String errorType, boolean uploadType)
            throws InvalidFormatException, IOException, ParseException {
        try {
            Workbook workbook = createWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            stateStore.setState("progress", 5);

            // 检查文件格式
            Row titles = sheet.getRow(0);
            if (!checkFormat(titles, FILE_FIELDS)) { return false; }

            Map<String, Integer> schema = parseSchema(titles);

            if (uploadType) { // 如果是从前端上传的
                return this.upload(errorType, sheet, schema, getSchoolYear(),
                        new Date(System.currentTimeMillis()));
            } else { // 用于测试的批量数据加载
                return this.upload(sheet, schema);
            }
        } finally {
            close(is);
        }
    }

    @Transactional
    public boolean upload(String type, Sheet sheet, Map<String, Integer> schema,
            String schoolYear, Date date)
            throws InvalidFormatException, IOException, ParseException {
        int term = 1; // 缴费均为第一学期
        Row row;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            List<String> values = parseRow(row, FILE_FIELDS, schema);

            String sno = values.get(0);
            Student student = (sno != null) ? studentRepo.findBySno(sno) : null;
            if (student == null || student.isLeave()) {
                handleErrorData(row, FILE_FIELDS, type, "");
                continue;
            }

            double shouldPaid = values.get(2) != null
                    ? Double.parseDouble(values.get(2)) : 0;

            Date feeDate = null;
            double feePaid = 0;
            boolean fee = parseBoolean(values.get(1));
            if (fee) {
                feePaid = values.get(3) != null
                        ? Double.parseDouble(values.get(3)) : 0;
                feeDate = values.get(4) != null ? getDateByNYR(values.get(4))
                        : null;
            }

            String year = values.get(5);

            Registration registration;
            registration = registrationRepo.findByStudentAndYearAndTerm(student,
                    year, term);
            if (registration != null) {
                registration.setFee(fee);
                registration.setFeeDate(feeDate);
                registration.setFeePaid(feePaid);
                registration.setFeeShouldPaid(shouldPaid);
                // 判断是否全日制
                if (student.getEducationMode().getName().equals("全日制")) {
                    if (registration.isCheckin() && fee) {// 如果报到和缴费都为true,则注册自动为true
                        registration.setRegister(true);
                        registration.setRegisterDate(date);// 注册时间为系统当前时间
                        // 如果是流水表中查出的记录是当前学年的，才会对学籍表中的学期进行+1的操作
                        if (schoolYear.equals(registration.getYear())) {
                            student.setTerm(student.getTerm() + 1);
                        }
                    }
                } else {
                    if (fee) {
                        registration.setRegister(true);
                        registration.setRegisterDate(date);// 注册时间为系统当前时间
                        // 如果是流水表中查出的记录是当前学年的，才会对学籍表中的学期进行+1的操作
                        if (schoolYear.equals(registration.getYear())) {
                            student.setTerm(student.getTerm() + 1);
                        }
                    }
                }
                registrationRepo.save(registration);
                studentRepo.save(student);
            }

            stateStore.setState("progress", i * 95 / sheet.getLastRowNum());
        }
        stateStore.setState("progress", 100);
        return true;
    }

    private boolean upload(Sheet sheet, Map<String, Integer> schema)
            throws ParseException {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            List<String> values = parseRow(sheet.getRow(i), FILE_FIELDS,
                    schema);

            Student student = studentRepo.findBySno(values.get(0));
            if (student == null) {
                continue;
            }

            double shouldPaid = (values.get(2) != null)
                    ? Double.parseDouble(values.get(2)) : 0;
            double feePaid = (values.get(3) != null)
                    ? Double.parseDouble(values.get(3)) : 0;
            Date feeDate = getDateByNYR(values.get(4));
            String year = values.get(5);

            Registration registration = registrationRepo
                    .findByStudentAndYearAndTerm(student, year, 1);
            if (registration == null) {
                registration = new Registration();
                registration.setStudent(student);
                registration.setYear(year);
                registration.setTerm(1);
            }

            boolean isFee = (feePaid == shouldPaid);
            registration.setFee(isFee);

            registration.setFeeDate(feeDate);
            registration.setFeePaid(feePaid);
            registration.setFeeShouldPaid(shouldPaid);

            if (student.getEducationMode().getName().equals("全日制")) {
                if (registration.isCheckin() && isFee) {
                    registration.setRegister(true);
                    registration.setRegisterDate(feeDate);
                }
            } else {
                if (isFee) {
                    registration.setRegister(true);
                    registration.setRegisterDate(feeDate);
                }
            }
            registrationRepo.save(registration);
        }
        return true;
    }

}
