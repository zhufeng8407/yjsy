package edu.ecnu.yjsy.service.xj;

import static edu.ecnu.yjsy.constant.QueryParameter.PARAM_ITEMS;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ecnu.yjsy.model.student.Registration;
import edu.ecnu.yjsy.model.student.RegistrationRespository;
import edu.ecnu.yjsy.model.student.Student;
import edu.ecnu.yjsy.model.student.StudentRepository;
import edu.ecnu.yjsy.service.BaseService;
import edu.ecnu.yjsy.service.search.SearchSQLService;
import edu.ecnu.yjsy.service.search.processor.CountProcessor;
import edu.ecnu.yjsy.service.search.processor.DynamicDiffProcessor;
import edu.ecnu.yjsy.service.search.processor.RegistrationProcessor;
import net.sf.json.JSONObject;

/**
 * <code>每学期</code>研究生需要完成学籍注册。学籍注册由院系秘书或学籍管理员。
 * <p>
 * 先决条件：是否完成<code>入学测试</code>（待最终确认）。
 * <p>
 * 主要业务逻辑如下：
 * <p>
 * 1. 在每学期开始之前重置注册状态
 * <p>
 * 2. 通过文件完成批量注册操作
 * <p>
 * 3. 通过注册界面的注册按钮完成批量注册操作
 * <p>
 * 4. 对于<code>助困生</code>，如果<code>已报到</code>，则自动为该生完成注册操作
 * <p>
 * 5. 对于<code>非助困生</code>，如果<code>已报到</code>且<code>已缴费</code>，则自动为该生完成注册操作
 * <p>
 * 6. 依据查询条件，统计学籍注册情况
 * <p>
 * 7. 依据查询条件，导出学籍注册报到缴费信息
 * <p>
 * 完成学籍注册后才可以进行正常的选课，培养，开题等操作。
 * <p>
 * FIXME: 需要添加一个判断学生是否已经注册的功能
 * <p>
 * FIXME: 功能需求：
 * 1. 增加一个函数，提供缴费和报道服务实现，用于判断在给定新的报道和注册流水下，当前学生是否应该被注册
 * 2. 给学生注册之后，学生信息是否应该修改什么字段
 *
 * @author liyanbin
 * @author xulinhao
 * @see CheckinService
 * @see FeeService
 */


@Service
public class RegistrationService extends BaseService {

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
                .addProcessor(new DynamicDiffProcessor()).create();
        countExec = searchSQLService.defaultSearch()
                .addProcessor(new RegistrationProcessor())
                .addProcessor(new DynamicDiffProcessor())
                .addProcessor(new CountProcessor()).create();
    }

    @Transactional
    public boolean register(Long id) {
        Student student = studentRepo.findOne(id);

        // 1. 先判断是否为在籍学生
        if (!student.isLeave()) {

            // 2. 计算学年学期
            int curterm = getSchoolTerm();
            String schoolYear = getSchoolYear();

            // 4. 查询该生是否存在一条当前学期的注册记录
            Registration record = registrationRepo
                    .findByStudentAndYearAndTerm(student, schoolYear, curterm);

            // 5. 如果已有注册记录就直接注册
            if ((record != null) && (!record.isRegister())) {
                Date date = new Date(System.currentTimeMillis());
                record.setRegister(true);
                record.setRegisterDate(date);

                // 6. 保存注册记录
                registrationRepo.save(record);

                // 7. 更新学籍表的学期数
                if (schoolYear.equals(record.getYear())) {
                    student.setTerm(student.getTerm() + 1);
                }
                studentRepo.save(student);
            }
        }
        return true;
    }

    /**
     * 重置在籍学生的注册状态：<code>每学期</code>为每个学生增加一条新的注册记录。
     *
     * @return true if reset successfully
     */
    @Transactional
    public boolean reset() {

        String schoolYear = getSchoolYear();
        int newterm = getSchoolTerm();

        List<Registration> registrations = registrationRepo
                .findByYearAndTerm(schoolYear, newterm);
        // 如果当前学年当前学期已经重置过了，便不能再重置
        if (!registrations.isEmpty()) {
            return false;
        }

        // 为每个在籍学生增加一条新的注册记录
        List<Student> students = studentRepo.findByLeave(false);
        for (Student student : students) {
            Registration registration = new Registration();
            registration.setCheckin(false);
            registration.setFee(false);
            registration.setRegister(false);
            registration.setStudent(student);
            registration.setTerm(newterm);
            registration.setYear(schoolYear);

            // FIXME
            // 是否为一个真正的批量更新;如果是，会不会出现日志存储溢出的问题
            registrationRepo.save(registration);
        }
        return true;
    }

    @SuppressWarnings("rawtypes")
    public byte[] export(List<Map> students) throws IOException {
        // FIXME
        // 建议与宋老师确认
        String[] titles = {"姓名", "学号", "一级院系", "二级院系", "年级", "专业", "是否报到",
                "是否缴费", "是否注册"};

        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet("sheet1");
        XSSFRow header = sheet.createRow(0);

        int j = 0;
        for (int i = 0; i < titles.length; i++) {
            header.createCell(j++).setCellValue(titles[i]);
        }

        int rowNum = 1;
        for (Map student : students) {
            XSSFRow row = sheet.createRow(rowNum++);
            j = 0;
            row.createCell(j++).setCellValue(student.get("name").toString());
            row.createCell(j++).setCellValue(student.get("sno").toString());
            row.createCell(j++).setCellValue(student.get("school").toString());
            row.createCell(j++)
                    .setCellValue(student.get("department").toString());
            row.createCell(j++).setCellValue(student.get("grade").toString());
            row.createCell(j++).setCellValue(student.get("minor").toString());
            row.createCell(j++).setCellValue(student.get("checkin").toString());
            row.createCell(j++).setCellValue(student.get("fee").toString());
            row.createCell(j++)
                    .setCellValue(student.get("register").toString());
        }

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            book.write(output);
            output.flush();
            return output.toByteArray();
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Map<String, Object> stats(String condition) {
        Map<String, Object> sc = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(condition), Map.class);
        List<Map> students = (List<Map>) conditionSearch(sc, 0,
                Integer.MAX_VALUE).get(PARAM_ITEMS);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", charts(students));

        return result;
    }

    @SuppressWarnings("rawtypes")
    private List<Map<String, Object>> charts(List<Map> students) {
        int register = 0;
        int unregister = 0;
        int fee = 0;
        int unfee = 0;
        int checkin = 0;
        int uncheckin = 0;
        if (students != null) {
            for (Map student : students) {
                if (student.get("register").equals("是")) {
                    register++;
                } else {
                    unregister++;
                }

                if (student.get("fee").equals("是")) {
                    fee++;
                } else {
                    unfee++;
                }

                if (student.get("checkin").equals("是")) {
                    checkin++;
                } else {
                    uncheckin++;
                }
            }
        }

        Map<String, Object> registered = new HashMap<>(2);
        registered.put("value", register);
        registered.put("name", "已注册");

        Map<String, Object> unregistered = new HashMap<>(2);
        unregistered.put("value", unregister);
        unregistered.put("name", "未注册");

        Map<String, Object> feed = new HashMap<>(2);
        feed.put("value", fee);
        feed.put("name", "已缴费");

        Map<String, Object> unfeed = new HashMap<>(2);
        unfeed.put("value", unfee);
        unfeed.put("name", "未缴费");

        Map<String, Object> checkined = new HashMap<>(2);
        checkined.put("value", checkin);
        checkined.put("name", "已报到");

        Map<String, Object> uncheckined = new HashMap<>(2);
        uncheckined.put("value", uncheckin);
        uncheckined.put("name", "未报到");

        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        data.add(registered);
        data.add(unregistered);
        data.add(feed);
        data.add(unfeed);
        data.add(checkined);
        data.add(uncheckined);

        return data;
    }

}
