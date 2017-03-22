package edu.ecnu.yjsy.service.yby;

import static edu.ecnu.yjsy.constant.Constant.PAGE_PAGE_INT;
import static edu.ecnu.yjsy.constant.Constant.PAGE_SIZE_INT;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;

import edu.ecnu.yjsy.model.auth.Account;
import edu.ecnu.yjsy.model.auth.AccountRepository;
import edu.ecnu.yjsy.model.pregraduation.APIMapping;
import edu.ecnu.yjsy.model.pregraduation.APIMappingReposipory;
import edu.ecnu.yjsy.model.pregraduation.PregraduateModifyType;
import edu.ecnu.yjsy.model.pregraduation.PregraduateRequst;
import edu.ecnu.yjsy.model.pregraduation.PregraduateRequstRepository;
import edu.ecnu.yjsy.model.staff.Staff;
import edu.ecnu.yjsy.model.student.*;
import edu.ecnu.yjsy.model.student.StudentRepository;
import edu.ecnu.yjsy.service.BaseService;
import edu.ecnu.yjsy.service.search.SearchProcessorBuilder;
import edu.ecnu.yjsy.service.search.SearchSQLService;
import edu.ecnu.yjsy.service.search.processor.CountProcessor;
import edu.ecnu.yjsy.service.search.processor.DifficultyProcessor;
import edu.ecnu.yjsy.service.search.processor.DisciplineProcessor;
import edu.ecnu.yjsy.service.search.processor.LimitClauseProcessor;
import edu.ecnu.yjsy.service.search.processor.StudentProcessor;
import edu.ecnu.yjsy.service.search.processor.UnitProcessor;
import net.sf.json.JSONObject;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 预毕业模块主要分为四个部分：
 * <p>
 * 1.管理员初始化预毕业（设置预毕业审核状态），设置预毕业字段修改状态
 * <p>
 * 2.学生显示可修改和需审批的预毕业字段，检查信息，若信息有误可以按要求修改，如信息无误，确认信息
 * <p>
 * 3.管理员查看学生提交的审批请求，进行审核
 * <p>
 * 4.管理员查看学生的预毕业审核状态，可以更改预毕业审核状态
 *
 * @author PYH
 */

@Service
public class PregraduationService extends BaseService {

    //以下是与前端交互时的参数名
    public static final String PARAM_FIELD = "field";

    public static final String MODIFY_TYPE = "modifyType";

    public static final String NOTES = "notes";

    public static final String CLASS_FIELD = "classField";

    public static final String ORI_DATA = "oridata";

    public static final String NEW_DATA = "newdata";

    public static final String TYPE = "type";

    public static final String FRONT_API = "frontApi";

    public static final String APPROVAL = "approval";

    private static final Sort SORT_ID_DESC = new Sort(Direction.ASC,
            "createDate");

    @SuppressWarnings("serial")
    private static final Set<String> CLASS_FIELD_SET = new HashSet<String>() {
        {
            add(SsnType.class.getName());
            add(Nation.class.getName());
            add(Ethnic.class.getName());
            add(Gender.class.getName());
            add(Marriage.class.getName());
            add(Military.class.getName());
            add(Religion.class.getName());
            add(Region.class.getName());
            add(Railway.class.getName());
            add(PreStudy.class.getName());
            add(DegreeLevel.class.getName());
            add(DegreeType.class.getName());
            add(Campus.class.getName());
            add(Staff.class.getName());
            add(BachelorDiscipline.class.getName());
            add(Discipline.class.getName());
            add(Unit.class.getName());
            add(Medical.class.getName());
            add(Examination.class.getName());
            add(SpecialPlan.class.getName());
            add(Enrollment.class.getName());
            add(Graduation.class.getName());
            add(ExamineeOrigin.class.getName());
            add(University.class.getName());
            add(Discipline.class.getName());
            add(EducationMode.class.getName());
            add(Degree.class.getName());
            add(Education.class.getName());
        }
    };

    @Autowired
    private ListableBeanFactory factory;

    @Autowired
    private PregraduateRequstRepository repo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private APIMappingReposipory apiRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    public void initConditionSearch(SearchSQLService searchSQLService) {
        SearchProcessorBuilder processorBuilder = searchSQLService.build()
                .addProcessor(new StudentProcessor())// 学籍表的搜索
                .addProcessor(new DifficultyProcessor())
                .addProcessor(new UnitProcessor())// 院系的搜索
                .addProcessor(new DisciplineProcessor())
                .addProcessor(new LimitClauseProcessor());// 专业的搜索;

        exec = processorBuilder.create();
        countExec = new SearchProcessorBuilder(processorBuilder)
                .addProcessor(new CountProcessor()).create();
    }

    /**
     * 管理员初始修改信息的函数
     *
     * @return
     */
    public Map<String, Object> getConfigInfo() throws Exception {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> lists = new ArrayList<>();
        List<APIMapping> mappings = apiRepo.findAll();
        for (APIMapping mapping : mappings) {
            Map<String, Object> map = new HashMap<>();
            map.put(PARAM_FIELD, mapping.getField());
            map.put(MODIFY_TYPE, mapping.getModifyType());
            map.put(NOTES, mapping.getNotes());
            lists.add(map);
        }
        result.put("info", lists);
        return result;
    }

    /**
     * 修改预毕业审核状态
     *
     * @param sno
     * @param pregraduationStatus
     * @return
     * @throws Exception
     */
    public boolean modifyPregraduationStatus(String sno,
                                             PregraduationStatus pregraduationStatus) throws Exception {
        Student student = studentRepo.findBySno(sno);
        student.setPregraduationStatus(pregraduationStatus);
        studentRepo.save(student);
        return true;
    }

    /**
     * 管理员初始化预毕业状态
     *
     * @param day
     * @return
     */
    public boolean intialize(int day) throws Exception {
        for (Student student : studentRepo.findByLeave(false)) {
            Date currentDate = new Date(System.currentTimeMillis());
            Date pregraduationDate = student.getPregraduationDate();
            if (pregraduationDate != null) {
                if (getDaysBetween(currentDate, pregraduationDate) <= day) {
                    student.setPregraduationStatus(PregraduationStatus.可修改);
                    studentRepo.save(student);
                }
            }
        }
        return true;
    }

    /**
     * 获得当前学生
     *
     * @param sno
     * @return
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public Map<String, Object> getCurStudent(String sno) throws Exception {
        Map<String, Object> results = new HashMap<>();
        List<Map<String, Object>> students = new ArrayList<>();
        Student student = studentRepo.findBySno(sno);
        boolean flag = true;
        // 判断学生的预毕业修改状态，修改状态不为空可查看预毕业信息
        if (student.getPregraduationStatus() != PregraduationStatus.空) {
            List<APIMapping> mappings = apiRepo.findAll();
            for (APIMapping mapping : mappings) {
                // 学生只能看到可修改和需审批的预毕业学籍字段
                if (mapping.getModifyType() == PregraduateModifyType.不可修改
                        || mapping.getModifyType() == null) {
                    continue;

                }
                Map<String, Object> map = new HashMap<>();
                Field field = Student.class
                        .getDeclaredField(mapping.getClassField());
                String classType = field.getType().getName().toString();
                field.setAccessible(true);
                Object oridata = field.get(student);
                if (CLASS_FIELD_SET.contains(classType)) {
                    oridata = oridata != null ? oridata.toString() : "";
                }
                if (classType.equals("boolean")) {
                    oridata = ((Boolean) oridata == true) ? "是" : "否";
                }
                oridata = oridata != null ? oridata : "";
                map.put(CLASS_FIELD, classType);
                map.put(ORI_DATA, oridata);
                map.put(MODIFY_TYPE, mapping.getModifyType());
                map.put(PARAM_FIELD, mapping.getField());
                map.put(NOTES, mapping.getNotes());
                map.put(NEW_DATA, "");
                map.put(TYPE, mapping.getDisplayFormat());
                map.put(FRONT_API, mapping.getFrontGetApi());
                map.put(APPROVAL, "");
                String newdata = "";
                List<PregraduateRequst> requsts = repo
                        .findBySnoAndFieldAndCreatedate(sno,
                                mapping.getField());
                if (!requsts.isEmpty()) {
                    PregraduateRequst requst = requsts.get(0);
                    if (requst.getAuditDate() == null) {
                        map.put(APPROVAL, "审批中");
                        flag = false;
                        newdata = requst.getNewDisplayValue();
                    } else {
                        if (requst.isAccept() == true) {
                            map.put(APPROVAL, "已同意");
                        } else {
                            map.put(APPROVAL, "已否决");
                        }
                    }
                }
                map.put(NEW_DATA, newdata);
                students.add(map);
            }
        }
        results.put("students", students);
        results.put("flag", flag);
        results.put("pregraduationStatus", student.getPregraduationStatus());
        return results;
    }

    /**
     * 学生提交申请 分为2种情况： 1，提交的申请是可修改的，则直接修改学籍数据。2，提交的申请是需要审批的，则提交修改申请
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean submitApplication(String sno, String apply)
            throws Exception {
        Map<String, Object> map = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(apply), Map.class);
        Repositories repositories = new Repositories(factory);

        String classField = map.get(CLASS_FIELD).toString();
        String field = map.get(PARAM_FIELD).toString();
        String oridata = map.get(ORI_DATA).toString();
        String newdata = map.get(NEW_DATA).toString();
        String newDisplayValue = (String) newdata;

        // 显示数据变成日期
        if (classField.equals("Date")) {
            Date date = getDateByUTC(newDisplayValue);
            newDisplayValue = sdf.format(date);
        }

        // 获取修改后显示数据
        // 判断是否是外键,为外键获取修改后显示数据
        // 这是改变的数据
        Object object = newdata;
        if (CLASS_FIELD_SET.contains(classField)) {
            object = ((JpaRepository) repositories
                    .getRepositoryFor(Class.forName(classField)))
                    .findOne(Long.parseLong(newdata));
            // FIXME
            // 建议格式化对应的元数据的toString
            newDisplayValue = object.toString();
        }

        Student student = studentRepo.findBySno(sno);

        APIMapping mapping = apiRepo.findByField(field);
        if (mapping.getModifyType() == PregraduateModifyType.可修改) {

            Field fieldName = Student.class
                    .getDeclaredField(mapping.getClassField());
            setUp(student, mapping, object, fieldName);
            // 更新学籍表
            studentRepo.save(student);
            return true;
        } else {
            // 提交申请
            Date curDate = new Date(System.currentTimeMillis());
            if (classField.equals("boolean")) {
                newDisplayValue = ((String) object).equals("true") ? "是" : "否";
            }
            // 可审批的申请写入数据库
            PregraduateRequst pre = new PregraduateRequst(student, classField,
                    field, oridata, newdata, newDisplayValue, curDate);
            // 只要没有正在审批得申请，都可以提交申请
            if (repo.findBySnoAndField(sno, field).isEmpty()) {
                repo.save(pre);
                return true;
            }
        }

        return false;
    }

    /**
     * 管理员提交修改状态配置
     *
     * @param apply
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean submitConfig(String apply) {
        Map<String, Object> map = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(apply), Map.class);
        String field = map.get(PARAM_FIELD).toString();

        PregraduateModifyType modifyType = PregraduateModifyType
                .valueOf(map.get(MODIFY_TYPE).toString());
        // 备注可以为空
        String notes = map.get(NOTES) != null ? map.get(NOTES).toString()
                : null;
        APIMapping mapping = apiRepo.findByField(field);
        mapping.setModifyType(modifyType);
        mapping.setNotes(notes);
        apiRepo.save(mapping);
        return true;
    }

    /**
     * 管理员获取申请数据
     *
     * @param page
     * @param size
     * @return
     */
    public List<Map<String, Object>> getAuditdata(int page, int size) {
        List<Map<String, Object>> res = new ArrayList<>();
        Page<PregraduateRequst> applys = repo
                .findAll(new PageRequest(page, size, SORT_ID_DESC));
        for (PregraduateRequst apply : applys) {
            // 过滤掉已经审批的预毕业修改申请
            if (apply.getAccount() == null) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", apply.getId());
                map.put("name", apply.getStudent().getName());
                map.put("sno", apply.getStudent().getSno());
                map.put("grade", apply.getStudent().getGrade());
                map.put("school", apply.getStudent().getUnit().getSchool());
                map.put("department",
                        apply.getStudent().getUnit().getDepartment());
                map.put("fieldDisplayName", apply.getFieldDisplayName());
                map.put("curDisplayValue", apply.getCurDisplayValue());
                map.put("newDisplayValue", apply.getNewDisplayValue());
                res.add(map);
            }
        }
        return res;
    }

    /**
     * 修改学籍数据
     *
     * @param student
     * @param mapping
     * @param object
     * @throws Exception
     */
    private void setUp(Student student, APIMapping mapping, Object object,
                       Field field) throws Exception {
        String classType = field.getType().getName().toString();
        if (classType.endsWith("Date")) {
            object = getDateByNYR((String) object);
        }
        if (classType.equals("short")) {
            object = Short.parseShort((String) object);
        }
        if (classType.equals("int")) {
            object = Integer.parseInt((String) object);
        }
        field.setAccessible(true);
        field.set(student, object);
    }

    /**
     * 管理员审核结果
     *
     * @param id
     * @param sno
     * @param accountId
     * @param flag
     * @return
     * @throws Exception
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Map<String, Object>> isPassAudit(long id, String sno,
                                                 long accountId, boolean flag) throws Exception {
        // 更新预毕业申请表
        Date auditDate = new Date(System.currentTimeMillis());
        Account account = accountRepo.findOne(accountId);
        boolean isAccept = true;
        PregraduateRequst apply = repo.findOne(id);

        // 如果审核通过
        if (flag == true) {
            Object newdata = apply.getNewValue();
            String classFiled = apply.getFieldName();
            String fieldDisplayName = apply.getFieldDisplayName();
            Student student = studentRepo.findBySno(sno);
            Repositories repositories = new Repositories(factory);
            APIMapping mapping = apiRepo.findByField(fieldDisplayName);
            Object object = newdata;

            // 查看是否是自定义类型
            if (CLASS_FIELD_SET.contains(classFiled)) {
                object = ((JpaRepository) repositories
                        .getRepositoryFor(Class.forName(classFiled)))
                        .findOne(Long.parseLong((String) newdata));
            }
            Field field = Student.class
                    .getDeclaredField(mapping.getClassField());
            setUp(student, mapping, object, field);
            studentRepo.save(student);
        } else {
            isAccept = false;
        }

        apply.setAuditDate(auditDate);
        apply.setAccount(account);
        apply.setAccept(isAccept);
        apply.setId(id);
        repo.save(apply);
        return getAuditdata(PAGE_PAGE_INT, PAGE_SIZE_INT);
    }

}
