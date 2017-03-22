package edu.ecnu.yjsy.service.yd;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ecnu.yjsy.constant.Constant;
import edu.ecnu.yjsy.model.auth.Account;
import edu.ecnu.yjsy.model.auth.AccountRepository;
import edu.ecnu.yjsy.model.change.AuditStatus;
import edu.ecnu.yjsy.model.change.StatusChangeAudit;
import edu.ecnu.yjsy.model.change.StatusChangeAuditDaoImpl;
import edu.ecnu.yjsy.model.change.StatusChangeAuditRepository;
import edu.ecnu.yjsy.model.change.StatusChangeRequest;
import edu.ecnu.yjsy.model.change.StatusChangeRequestRepository;
import edu.ecnu.yjsy.model.change.TemporaryStudentChange;
import edu.ecnu.yjsy.model.change.TemporaryStudentChangeRepository;
import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.staff.Staff;
import edu.ecnu.yjsy.model.staff.StaffRepository;
import edu.ecnu.yjsy.model.student.Discipline;
import edu.ecnu.yjsy.model.student.DisciplineRepository;
import edu.ecnu.yjsy.model.student.Graduation;
import edu.ecnu.yjsy.model.student.GraduationRepository;
import edu.ecnu.yjsy.model.student.Student;
import edu.ecnu.yjsy.model.student.StudentRepository;
import edu.ecnu.yjsy.model.student.Unit;
import edu.ecnu.yjsy.model.student.UnitRepository;
import edu.ecnu.yjsy.service.BaseService;
import edu.ecnu.yjsy.service.search.SearchProcessorBuilder;
import edu.ecnu.yjsy.service.search.SearchSQLService;
import edu.ecnu.yjsy.service.search.processor.CountProcessor;
import edu.ecnu.yjsy.service.search.processor.DisciplineProcessor;
import edu.ecnu.yjsy.service.search.processor.LimitClauseProcessor;
import edu.ecnu.yjsy.service.search.processor.StatusChangeAuditProcessor;
import edu.ecnu.yjsy.service.search.processor.StatusChangeRequestProcessor;
import edu.ecnu.yjsy.service.search.processor.StudentProcessor;
import edu.ecnu.yjsy.service.search.processor.UnitProcessor;
import net.sf.json.JSONObject;

@Service
public class StatusChangeAuditService extends BaseService {

    @Autowired
    private StatusChangeAuditRepository repo;

    @Autowired
    private StatusChangeRequestRepository statusChangeRequestRepo;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private GraduationRepository graduationRepository;

    @Autowired
    private TemporaryStudentChangeRepository temporaryStudentChangeRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private StatusChangeAuditDaoImpl statusChangeAuditDaoImpl;

    @Autowired
    public void initConditionSearch(SearchSQLService searchSQLService) {
        SearchProcessorBuilder builder = searchSQLService.build()
                .addProcessor(new StatusChangeAuditProcessor(staffRepository,
                        studentRepository))
                .addProcessor(new StatusChangeRequestProcessor())
                .addProcessor(new StudentProcessor())// 学籍表的搜索
                .addProcessor(new UnitProcessor())// 院系的搜索
                .addProcessor(new DisciplineProcessor())
                .addProcessor(new LimitClauseProcessor());// 专业的搜索;
        exec = builder.create();
        countExec = new SearchProcessorBuilder(builder)
                .addProcessor(new CountProcessor()).create();
    }

    @Override
    public Map<String, Object> conditionSearch(
            Map<String, Object> searchCondition, int page, int size) {

        Account account = accountRepository.findOne(SecurityContextHolder
                .getContext().getAuthentication().getName());
        if (account != null) {
            searchCondition.put(Column.ACCOUNT, account.getId());
        }

        return super.conditionSearch(searchCondition, page, size);
    }

    /**
     * 异动审核通过处理
     * 
     * @param auditRecord
     *            异动审核记录
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public void accept(String auditRecord) throws ParseException {
        boolean isEnd = true;
        Map<String, Object> sc = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(auditRecord), Map.class);
        // 20170111 modified by zhufeng
        // 取得审核记录ID
        Object auditObject = sc.get(Constant.AUDIT_ID);
        if (auditObject != null && !"null".equals(auditObject.toString())) {
            Long id = Long.valueOf(auditObject.toString());
            long idRequest = repo.findOne(id).getRequest().getId();
            repo.accept(id, AuditStatus.同意,
                    new Timestamp(System.currentTimeMillis()));
            StatusChangeRequest statusChangeRequest = statusChangeRequestRepo
                    .findOne(idRequest);
            statusChangeRequestRepo.auditing(idRequest);
            short auditSeq = repo.findOne(id).getWorkflow().getSequence();

            List<StatusChangeAudit> audits = repo.findByRequest_id(idRequest);

            for (StatusChangeAudit auditItem : audits) {
                if (auditItem.getWorkflow().getSequence() == auditSeq + 1) {
                    // 如果有后续流程则把后续流程的审批状态更新成审核中
                    repo.auditing(auditItem.getId(), AuditStatus.待审批);
                    isEnd = false;
                    break;
                }
            }

            if (isEnd) {
                // 如果是最后的流程则进行最终的更新处理
                statusChangeRequestRepo.accept(idRequest);
                List<TemporaryStudentChange> temporaryStudentChangeList = temporaryStudentChangeRepository
                        .getStudentChangeByStatusChangeRequest(
                                statusChangeRequest.getId(),
                                Constant.DEL_FLG_0);

                Map<String, Object> updateCondition = null;

                if (temporaryStudentChangeList != null
                        && !temporaryStudentChangeList.isEmpty()) {

                    updateCondition = (Map<String, Object>) JSONObject.toBean(
                            JSONObject.fromObject(temporaryStudentChangeList
                                    .get(0).getUpdateJson()),
                            Map.class);
                }
                // 更新学籍表
                this.updateXjByStatusChangeType(statusChangeRequest,
                        updateCondition);
                // 逻辑删除学籍异动更新学籍临时表
                temporaryStudentChangeRepository.delTemporaryStudentChange(
                        statusChangeRequest.getId(), Constant.DEL_FLG_1);
            }

        }
    }

    /**
     * 异动审核否决处理
     * 
     * @param auditRecord
     *            异动审核记录
     */
    @Transactional
    public void reject(String auditRecord) {
        @SuppressWarnings("unchecked")
        Map<String, Object> sc = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(auditRecord), Map.class);
        // 20170111 modified by zhufeng
        Object auditObject = sc.get(Constant.AUDIT_ID);
        if (auditObject != null && !"null".equals(auditObject.toString())) {
            Long id = Long.valueOf(auditObject.toString());

            repo.reject(id, AuditStatus.否决,
                    new Timestamp(System.currentTimeMillis()));

            StatusChangeAudit audit = repo.findOne(id);
            short auditSeq = audit.getWorkflow().getSequence();
            List<StatusChangeAudit> audits = repo
                    .findByRequest_id(audit.getRequest().getId());
            for (StatusChangeAudit auditItem : audits) {
                // FIXME
                // 需要解释清楚为什么用 > 进行顺序上的更新处理
                if (auditItem.getWorkflow().getSequence() > auditSeq)
                    repo.reject(auditItem.getId(), AuditStatus.否决,
                            new Timestamp(System.currentTimeMillis()));
            }

            statusChangeRequestRepo
                    .reject(repo.findOne(id).getRequest().getId());
        }
    }

    public List<Map<String, Object>> searchStatusChangeAudits(
            String auditRecord) {

        @SuppressWarnings("unchecked")
        Map<String, Object> sc = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(auditRecord), Map.class);

        Object auditObject = sc.get(Constant.AUDIT_ID);

        StatusChangeAudit statusChangeAudit = repo
                .findOne(new Long(auditObject.toString()));

        long workflowId = statusChangeAudit.getWorkflow().getId();

        return statusChangeAuditDaoImpl.findByRequestIdAndFlowId(
                statusChangeAudit.getRequest().getId(), workflowId);

    }

    /**
     * 异动审核通过场合更新学籍表的处理
     * 
     * @param statusChangeRequest
     *            异动申请记录
     * @param updateCondition
     *            学籍异动更新学籍临时表的更新json
     * @throws ParseException
     */
    private void updateXjByStatusChangeType(
            StatusChangeRequest statusChangeRequest,
            Map<String, Object> updateCondition) throws ParseException {
        short majorCode = statusChangeRequest.getType().getMajorCode();
        Student student = statusChangeRequest.getStudent();
        Date expectedGraduationDate = null;
        // 学期数
        String term = (String) updateCondition.get(Column.TERM);
        // 预计毕业日期
        Date graduationDate = student.getExpectedGraduationDate();

        switch (majorCode) {
        // 延期的场合
        case Constant.STATUS_CHANGE_TYPE_MAJOR_CODE_1:

            if (Constant.XQS_1.equals(term)) {
                // 一学期的场合
                expectedGraduationDate = getDateAddHalfYear(graduationDate);
            } else {
                // 二学期的场合
                expectedGraduationDate = getDateAddYear(graduationDate);
            }
            updateStudentForPostponed(student, statusChangeRequest,
                    expectedGraduationDate);
            break;

        // 休学的场合
        case Constant.STATUS_CHANGE_TYPE_MAJOR_CODE_2:
            if (Constant.XQS_1.equals(term)) {
                // 一学期的场合
                expectedGraduationDate = getDateAddHalfYear(graduationDate);
            } else {
                // 二学期的场合
                expectedGraduationDate = getDateAddYear(graduationDate);
            }
            updateStudentForDropout(student, statusChangeRequest,
                    expectedGraduationDate);
            break;

        // 复学的场合
        case Constant.STATUS_CHANGE_TYPE_MAJOR_CODE_3:
            updateStudentForReinstate(student, statusChangeRequest);
            break;

        // 退学的场合
        case Constant.STATUS_CHANGE_TYPE_MAJOR_CODE_4:
            updateStudentForDropout(student, statusChangeRequest);
            break;

        // 提前毕业的场合
        case Constant.STATUS_CHANGE_TYPE_MAJOR_CODE_5:
            String earlyGraduationTerm = (String) updateCondition
                    .get("earlyGraduationTerm");
            if (Constant.XQS_1.equals(earlyGraduationTerm)) {
                // 一学期的场合
                expectedGraduationDate = getDateSubtractHalfYear(
                        graduationDate);
            } else if (Constant.XQS_2.equals(earlyGraduationTerm)) {
                // 二学期的场合
                expectedGraduationDate = getDateSubtractYear(graduationDate);
            } else if (Constant.XQS_3.equals(earlyGraduationTerm)) {
                // 三学期的场合
                expectedGraduationDate = getDateSubtractOneHalfYear(
                        graduationDate);
            } else {
                // 四学期的场合
                expectedGraduationDate = getDateSubtractTwoYears(
                        graduationDate);
            }
            updateStudentForGraduateEarly(student, statusChangeRequest,
                    expectedGraduationDate);
            break;

        // 放弃入学资格的场合
        case Constant.STATUS_CHANGE_TYPE_MAJOR_CODE_6:
            updateStudentForGiveupEntrance(student, statusChangeRequest);
            break;

        // 取消入学资格的场合
        case Constant.STATUS_CHANGE_TYPE_MAJOR_CODE_7:
            updateStudentForCancelAdmission(student, statusChangeRequest);
            break;

        // 转导师的场合
        case Constant.STATUS_CHANGE_TYPE_MAJOR_CODE_8:
            String staffSno = (String) updateCondition.get(Constant.STAFF);
            updateStudentForChangeTutor(student, statusChangeRequest, staffSno);
            break;

        // 结业的场合
        case Constant.STATUS_CHANGE_TYPE_MAJOR_CODE_9:
            updateStudentForCompletion(student, statusChangeRequest);
            break;

        // 肄业的场合
        case Constant.STATUS_CHANGE_TYPE_MAJOR_CODE_11:
            updateStudentForStudiedInSchool(student, statusChangeRequest);
            break;

        // 硕博连读转硕的场合
        case Constant.STATUS_CHANGE_TYPE_MAJOR_CODE_13:
            expectedGraduationDate = getDateByUTC(
                    (String) updateCondition.get(Constant.TRANSMIDATE));
            updateStudentForDoctorToMaster(student, statusChangeRequest,
                    expectedGraduationDate);
            break;

        // 转专业的场合
        case Constant.STATUS_CHANGE_TYPE_MAJOR_CODE_14:
            String disciplineStr = (String) updateCondition
                    .get(Constant.DISCIPLINE);
            Discipline discipline = disciplineRepository
                    .findByMinorCode(disciplineStr);
            updateStudentForSwitchDiscipline(student, statusChangeRequest,
                    discipline);
            break;

        // 转院系的场合
        case Constant.STATUS_CHANGE_TYPE_MAJOR_CODE_15:
            String unitCode = (String) updateCondition.get(Constant.UNIT);
            Unit unit = unitRepository.findByDepartmentCode(unitCode);
            updateStudentForSwitchUnit(student, statusChangeRequest, unit);
            break;

        // 推迟入学的场合
        case Constant.STATUS_CHANGE_TYPE_MAJOR_CODE_17:
            expectedGraduationDate = getDateAddYear(graduationDate);
            // 入学日期
            Date admissionDate = getDateAddYear(student.getAdmissionDate());

            updateStudentForDeferredAdmission(student, statusChangeRequest,
                    expectedGraduationDate, admissionDate);

        }
    }

    /**
     * 延期场合更新学籍表的处理
     * 
     * @param student
     *            学生
     * @param statusChangeRequest
     *            移动申请
     * @param expectedGraduationDate
     *            预计毕业日期
     */
    private void updateStudentForPostponed(Student student,
            StatusChangeRequest statusChangeRequest,
            Date expectedGraduationDate) {
        // 预计毕业时间
        student.setExpectedGraduationDate(
                new java.sql.Date(expectedGraduationDate.getTime()));
        Set<StatusChangeRequest> requestSet = student.getRequests();
        // 最近学籍变动情况
        requestSet.add(statusChangeRequest);
        student.setRequests(requestSet);
        // save student
        studentRepository.save(student);
    }

    /**
     * 休学场合更新学籍表的处理
     * 
     * @param student
     *            学生
     * @param statusChangeRequest
     *            异动申请的记录
     * @param expectedGraduationDate
     *            预计毕业日期
     */
    private void updateStudentForDropout(Student student,
            StatusChangeRequest statusChangeRequest,
            Date expectedGraduationDate) {
        // 预计毕业时间
        student.setExpectedGraduationDate(
                new java.sql.Date(expectedGraduationDate.getTime()));
        Set<StatusChangeRequest> requestSet = student.getRequests();
        // 最近学籍变动情况
        requestSet.add(statusChangeRequest);
        student.setRequests(requestSet);
        // save student
        studentRepository.save(student);
    }

    /**
     * 复学场合更新学籍表的处理
     * 
     * @param student
     *            学生
     * @param statusChangeRequest
     *            异动申请记录
     */
    private void updateStudentForReinstate(Student student,
            StatusChangeRequest statusChangeRequest) {
        // 最近学籍变动情况
        Set<StatusChangeRequest> requestSet = student.getRequests();
        requestSet.add(statusChangeRequest);
        student.setRequests(requestSet);
        // save student
        studentRepository.save(student);

    }

    /**
     * 退学场合更新学籍表的处理
     * 
     * @param student
     *            学生
     * @param statusChangeRequest
     *            异动申请的记录
     * @param expectedGraduationDate
     *            预计毕业日期
     */
    private void updateStudentForDropout(Student student,
            StatusChangeRequest statusChangeRequest) {
        // 最近学籍变动情况
        Set<StatusChangeRequest> requestSet = student.getRequests();
        requestSet.add(statusChangeRequest);
        student.setRequests(requestSet);
        java.sql.Date nextDay = new java.sql.Date(getNextDay().getTime());
        // 离校日期
        student.setLeaveDate(nextDay);
        // 离校状态
        student.setLeave(true);
        // 毕业类型code
        String graduationCode = null;
        // 异动小分类code
        short minorCode = statusChangeRequest.getType().getMinorCode();
        if (Constant.STATUS_CHANGE_TYPE_MINOR_CODE_1 == minorCode) {
            graduationCode = Constant.GRADUATION_CODE_6;
        } else {
            graduationCode = Constant.GRADUATION_CODE_7;
        }
        // 毕业类型
        Graduation graduation = graduationRepository.findByCode(graduationCode);
        student.setGraduation(graduation);
        // 实际毕业日期
        student.setActualGraduationDate(nextDay);
        // 更新学籍表
        studentRepository.save(student);
    }

    /**
     * 提前毕业场合更新学籍表的处理
     * 
     * @param student
     *            学生
     * @param statusChangeRequest
     *            异动申请的记录
     * @param expectedGraduationDate
     *            预计毕业日期
     */
    private void updateStudentForGraduateEarly(Student student,
            StatusChangeRequest statusChangeRequest,
            Date expectedGraduationDate) {
        // 预计毕业时间
        student.setExpectedGraduationDate(
                new java.sql.Date(expectedGraduationDate.getTime()));
        Set<StatusChangeRequest> requestSet = student.getRequests();
        // 最近学籍变动情况
        requestSet.add(statusChangeRequest);
        student.setRequests(requestSet);
        // save student
        studentRepository.save(student);
    }

    /**
     * 放弃入学资格场合更新学籍表的处理
     * 
     * @param student
     *            学生
     * @param statusChangeRequest
     *            异动申请的记录
     */
    private void updateStudentForGiveupEntrance(Student student,
            StatusChangeRequest statusChangeRequest) {
        // 最近学籍变动情况
        Set<StatusChangeRequest> requestSet = student.getRequests();
        requestSet.add(statusChangeRequest);
        student.setRequests(requestSet);
        java.sql.Date today = new java.sql.Date(new Date().getTime());
        // 离校日期
        student.setLeaveDate(today);
        // 离校状态
        student.setLeave(true);
        // 毕业类型
        Graduation graduation = graduationRepository
                .findByCode(Constant.GRADUATION_CODE_4);
        student.setGraduation(graduation);
        // 实际毕业日期
        student.setActualGraduationDate(today);
        // 更新学籍表
        studentRepository.save(student);
    }

    /**
     * 取消入学资格场合更新学籍表的处理
     * 
     * @param student
     *            学生
     * @param statusChangeRequest
     *            异动申请的记录
     */
    private void updateStudentForCancelAdmission(Student student,
            StatusChangeRequest statusChangeRequest) {
        // 最近学籍变动情况
        Set<StatusChangeRequest> requestSet = student.getRequests();
        requestSet.add(statusChangeRequest);
        student.setRequests(requestSet);
        java.sql.Date today = new java.sql.Date(new Date().getTime());
        // 离校日期
        student.setLeaveDate(today);
        // 离校状态
        student.setLeave(true);
        // 毕业类型
        Graduation graduation = graduationRepository
                .findByCode(Constant.GRADUATION_CODE_5);
        student.setGraduation(graduation);
        // 实际毕业日期
        student.setActualGraduationDate(today);
        // 更新学籍表
        studentRepository.save(student);
    }

    /**
     * 转导师场合更新学籍表的处理
     * 
     * @param student
     *            学生
     * @param statusChangeRequest
     *            异动申请的记录
     * @param staffSno
     *            导师sno
     */
    private void updateStudentForChangeTutor(Student student,
            StatusChangeRequest statusChangeRequest, String staffSno) {
        // 最近学籍变动情况
        Set<StatusChangeRequest> requestSet = student.getRequests();
        requestSet.add(statusChangeRequest);
        student.setRequests(requestSet);
        // 导师
        Staff staff = staffRepository.findById(new Long(staffSno));

        student.setSupervisor(staff);
        staff.getStudents().add(student);
        // 更新学籍表
        studentRepository.save(student);
        // 更新导师表
        staffRepository.save(staff);
    }

    /**
     * 结业异动场合更新学籍表的处理
     * 
     * @param student
     *            学生
     * @param statusChangeRequest
     *            异动申请的记录
     */
    private void updateStudentForCompletion(Student student,
            StatusChangeRequest statusChangeRequest) {
        // 最近学籍变动情况
        Set<StatusChangeRequest> requestSet = student.getRequests();
        requestSet.add(statusChangeRequest);
        student.setRequests(requestSet);
        java.sql.Date nextDay = new java.sql.Date(getNextDay().getTime());
        // 离校日期
        student.setLeaveDate(nextDay);
        // 离校状态
        student.setLeave(true);
        // 毕业类型
        Graduation graduation = graduationRepository
                .findByCode(Constant.GRADUATION_CODE_2);
        student.setGraduation(graduation);
        // 实际毕业日期
        student.setActualGraduationDate(nextDay);
        // 更新学籍表
        studentRepository.save(student);
    }

    /**
     * 肄业异动场合更新学籍表的处理
     * 
     * @param student
     *            学生
     * @param statusChangeRequest
     *            异动申请的记录
     */
    private void updateStudentForStudiedInSchool(Student student,
            StatusChangeRequest statusChangeRequest) {
        // 最近学籍变动情况
        Set<StatusChangeRequest> requestSet = student.getRequests();
        requestSet.add(statusChangeRequest);
        student.setRequests(requestSet);
        java.sql.Date nextDay = new java.sql.Date(getNextDay().getTime());
        // 离校日期
        student.setLeaveDate(nextDay);
        // 离校状态
        student.setLeave(true);
        // 毕业类型
        Graduation graduation = graduationRepository
                .findByCode(Constant.GRADUATION_CODE_3);
        student.setGraduation(graduation);
        // 实际毕业日期
        student.setActualGraduationDate(nextDay);
        // 更新学籍表
        studentRepository.save(student);
    }

    /**
     * 硕博连读转硕场合更新学籍表的处理
     * 
     * @param student
     *            学生
     * @param statusChangeRequest
     *            异动申请的记录
     * @param expectedGraduationDate
     *            预计毕业日期
     */
    private void updateStudentForDoctorToMaster(Student student,
            StatusChangeRequest statusChangeRequest,
            Date expectedGraduationDate) {
        // 预计毕业时间
        student.setExpectedGraduationDate(
                new java.sql.Date(expectedGraduationDate.getTime()));
        Set<StatusChangeRequest> requestSet = student.getRequests();
        // 最近学籍变动情况
        requestSet.add(statusChangeRequest);
        student.setRequests(requestSet);
        // save student
        studentRepository.save(student);
    }

    /**
     * 转专业场合更新学籍表的处理
     * 
     * @param student
     *            学生
     * @param statusChangeRequest
     *            异动申请的记录
     * @param discipline
     *            专业
     */
    private void updateStudentForSwitchDiscipline(Student student,
            StatusChangeRequest statusChangeRequest, Discipline discipline) {
        // 预计毕业时间
        Set<StatusChangeRequest> requestSet = student.getRequests();
        // 最近学籍变动情况
        requestSet.add(statusChangeRequest);
        student.setRequests(requestSet);
        // 专业
        student.setDiscipline(discipline);
        // save student
        studentRepository.save(student);
    }

    /**
     * 转院系场合更新学籍表的处理
     * 
     * @param student
     *            学生
     * @param statusChangeRequest
     *            异动申请的记录
     * @param Unit
     *            院系
     */
    private void updateStudentForSwitchUnit(Student student,
            StatusChangeRequest statusChangeRequest, Unit unit) {
        // 预计毕业时间
        Set<StatusChangeRequest> requestSet = student.getRequests();
        // 最近学籍变动情况
        requestSet.add(statusChangeRequest);
        student.setRequests(requestSet);
        // 院系
        student.setUnit(unit);
        // save student
        studentRepository.save(student);
    }

    /**
     * 推迟入学场合更新学籍表的处理
     * 
     * @param student
     *            学生
     * @param statusChangeRequest
     *            异动申请的记录
     * @param expectedGraduationDate
     *            预计毕业日期
     * @param admissionDate
     *            入学日期
     */
    private void updateStudentForDeferredAdmission(Student student,
            StatusChangeRequest statusChangeRequest,
            Date expectedGraduationDate, Date admissionDate) {
        // 预计毕业时间
        student.setExpectedGraduationDate(
                new java.sql.Date(expectedGraduationDate.getTime()));
        Set<StatusChangeRequest> requestSet = student.getRequests();
        // 最近学籍变动情况
        requestSet.add(statusChangeRequest);
        student.setRequests(requestSet);
        // 学期
        int term = student.getTerm();
        student.setTerm(++term);
        // 入学日期
        student.setAdmissionDate(new java.sql.Date(admissionDate.getTime()));
        // 年级
        short grade = student.getGrade();
        student.setGrade(++grade);
        // save student
        studentRepository.save(student);
    }
    
}