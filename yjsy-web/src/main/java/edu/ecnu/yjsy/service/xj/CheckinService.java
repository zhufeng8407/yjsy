package edu.ecnu.yjsy.service.xj;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ecnu.yjsy.model.student.DifficultyRepository;
import edu.ecnu.yjsy.model.student.Registration;
import edu.ecnu.yjsy.model.student.RegistrationRespository;
import edu.ecnu.yjsy.model.student.Student;
import edu.ecnu.yjsy.model.student.StudentRepository;
import edu.ecnu.yjsy.service.BaseService;
import edu.ecnu.yjsy.service.search.SearchSQLService;

/**
 * <code>每学期</code>学生持学生卡到院系进行刷卡报到（由院系秘书操作）。
 * <p>
 * 主要业务逻辑如下：
 * <p>
 * 1. 在每学期开始之前重置刷卡报到状态
 * <p>
 * 2. 学籍管理员从各院系获取报到刷卡文件，通过文件上传的模式导入学籍系统
 * <p>
 * 3. 依据查询条件，统计刷卡报到情况
 * <p>
 * 4. 依据查询条件，导出刷卡报到信息（实现参见<code>RegistrationService</code>)
 * <p>
 * 刷卡机记录学生的报到刷卡信息。本服务提供批量导入学生报到刷卡信息和重置学生报到状态的功能。
 *
 * @author liyanbin
 * @author xulinhao
 * @see RegistrationService
 * @see FeeService
 */

@Service
public class CheckinService extends BaseService {

    private static final String[] FILE_FIELDS = { "学号", "报到", "报到码", "报到日期",
            "学年", "学期" };

    // 报到注册缴费使用了一张流水表
    @Autowired
    private RegistrationRespository registrationRepo;

    @Autowired
    private DifficultyRepository difficultyRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Override
    public void initConditionSearch(SearchSQLService searchSQLService) {
        // DO NOTHING
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
                return this.upload(schema, sheet, getSchoolYear(),
                        new Date(System.currentTimeMillis()), errorType);
            } else { // 用于测试的批量数据加载
                return this.upload(schema, sheet);
            }
        } finally {
            close(is);
        }
    }

    @Transactional
    private boolean upload(Map<String, Integer> schema, Sheet sheet,
            String schoolYear, Date registerDate, String errorType) {
        Row row = null;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            List<String> values = parseRow(row, FILE_FIELDS, schema);

            // 通过学号找到学籍表中对应的学生对象
            String sno = values.get(0);
            Student student = (sno != null) ? studentRepo.findBySno(sno) : null;
            if (student == null || student.isLeave()) {
                handleErrorData(row, FILE_FIELDS, errorType, "");
                continue;
            }

            String checkinNo = null;
            Date checkinDate = null;
            boolean checkin = parseBoolean(values.get(1));
            if (checkin) {
                checkinNo = values.get(2);
                checkinDate = (values.get(3) != null)
                        ? getDateByNYR(values.get(3)) : null;
            }

            String year = values.get(4);
            int term = (values.get(5) != null) ? Integer.parseInt(values.get(5))
                    : 0;

            Registration registration = registrationRepo
                    .findByStudentAndYearAndTerm(student, year, term);
            if (registration != null) {
                // FIXME
                // 为什么第一个学期和第二个学期的报道不一样？
                if (term == 1) {
                    registration.setCheckin(checkin);
                    registration.setCheckinDate(checkinDate);
                    registration.setCheckinNo(checkinNo);
                    if (student.getEducationMode().getName().equals("全日制")) {
                        // 判断是否为助困生，如果是，而且已经报到则自动注册
                        if (difficultyRepo.findByStudentAndYear(student,
                                year) != null) {
                            if (checkin) {
                                registration.setRegister(true);
                                registration.setRegisterDate(registerDate);
                                // 如果是流水表中查出的记录是当前学年的，才会对学籍表中的学期进行+1的操作
                                if (schoolYear.equals(registration.getYear())) {
                                    student.setTerm(student.getTerm() + 1);
                                }
                            }
                        } else {
                            // 如果不是助困生，且报到和缴费都为true,则注册自动为true
                            if (registration.isFee() && checkin) {
                                registration.setRegister(true);
                                registration.setRegisterDate(registerDate);
                                if (schoolYear.equals(registration.getYear())) {
                                    student.setTerm(student.getTerm() + 1);
                                }
                            }
                        }
                    }
                } else {
                    registration.setCheckin(checkin);
                    registration.setCheckinDate(checkinDate);
                    registration.setCheckinNo(checkinNo);
                }
            }
            registrationRepo.save(registration);
            studentRepo.save(student);
            stateStore.setState("progress", 5 + i * 95 / sheet.getLastRowNum());
        }
        stateStore.setState("progress", 100);
        return true;
    }

    private boolean upload(Map<String, Integer> schema, Sheet sheet)
            throws ParseException {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            List<String> values = parseRow(sheet.getRow(i), FILE_FIELDS,
                    schema);

            // 通过学号找到学籍表中对应的学生对象
            Student student = studentRepo.findBySno(values.get(0));
            if (student == null) {
                continue;
            }

            String checkinNo = null;
            Date checkinDate = null;
            boolean checkin = parseBoolean(values.get(1));
            if (checkin) {
                checkinNo = values.get(2);
                checkinDate = getDateByNYR(values.get(3));
            }

            String year = values.get(4);
            int term = (values.get(5) != null) ? Integer.parseInt(values.get(5))
                    : 1;

            Registration registration = registrationRepo
                    .findByStudentAndYearAndTerm(student, year, term);
            if (registration == null) {
                registration = new Registration();
                registration.setStudent(student);
            }

            registration.setCheckin(checkin);
            registration.setCheckinDate(checkinDate);
            registration.setCheckinNo(checkinNo);
            registration.setYear(year);
            registration.setTerm(term);

            if (student.getEducationMode().getName().equals("全日制")) {
                // 判断是否为助困生，如果是且已经报到
                // 则自动注册
                if (difficultyRepo.findByStudentAndYear(student,
                        year) != null) {
                    if (checkin) {
                        registration.setRegister(true);
                        registration.setRegisterDate(checkinDate);
                    }
                } else {
                    // 如果不是助困生，且报到和缴费都为真
                    // 则注册自动为true
                    if (registration.isFee() && checkin) {
                        registration.setRegister(true);
                        registration.setRegisterDate(checkinDate);
                    }
                }
            }
            registrationRepo.save(registration);
        }

        return true;

    }

}
