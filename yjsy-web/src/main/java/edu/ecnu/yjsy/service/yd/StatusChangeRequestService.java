package edu.ecnu.yjsy.service.yd;

import static edu.ecnu.yjsy.constant.Constant.DEL_FLG_0;
import static edu.ecnu.yjsy.constant.Constant.EMPTY_STRING;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ecnu.yjsy.exception.BusinessException;
import edu.ecnu.yjsy.model.auth.Account;
import edu.ecnu.yjsy.model.auth.AccountPrivilege;
import edu.ecnu.yjsy.model.auth.AccountPrivilegeRepository;
import edu.ecnu.yjsy.model.auth.AccountRepository;
import edu.ecnu.yjsy.model.auth.DomainLevel;
import edu.ecnu.yjsy.model.auth.Role;
import edu.ecnu.yjsy.model.change.AuditStatus;
import edu.ecnu.yjsy.model.change.AuditWorkflow;
import edu.ecnu.yjsy.model.change.AuditWorkflowRepository;
import edu.ecnu.yjsy.model.change.StatusChangeAudit;
import edu.ecnu.yjsy.model.change.StatusChangeAuditRepository;
import edu.ecnu.yjsy.model.change.StatusChangeRequest;
import edu.ecnu.yjsy.model.change.StatusChangeRequestRepository;
import edu.ecnu.yjsy.model.change.StatusChangeType;
import edu.ecnu.yjsy.model.change.TemporaryStudentChange;
import edu.ecnu.yjsy.model.change.TemporaryStudentChangeRepository;
import edu.ecnu.yjsy.model.constant.Column;
import edu.ecnu.yjsy.model.student.Student;
import edu.ecnu.yjsy.model.student.StudentRepository;
import edu.ecnu.yjsy.model.student.Unit;
import edu.ecnu.yjsy.security.exception.ErrorCode;
import edu.ecnu.yjsy.service.MetadataService;
import net.sf.json.JSONObject;

/**
 * @author wanglei
 * @author xulinhao
 */

@Service
public class StatusChangeRequestService {

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private StatusChangeAuditRepository auditRepo;

    @Autowired
    private AuditWorkflowRepository workflowRepo;

    @Autowired
    private StatusChangeRequestRepository requestRepo;

    @Autowired
    private AccountPrivilegeRepository accountPrivilegeRepository;

    @Autowired
    private TemporaryStudentChangeRepository temporaryStudentChangeRepository;

    @Autowired
    private AccountRepository accountRepository;

    // FIXME
    // 建议重命名
    public List<StatusChangeRequest> getRequests(String sno) {
        return requestRepo.findByStudent_sno(sno);
    }

    // FIXME
    // 是否需要 major?
    // 应该在一个事务中完成.需要返回值 true or false
    // 20170111 modifided by zhufeng
    @Transactional
    public void save(String updateCondition) throws Exception {

        @SuppressWarnings("unchecked")
        Map<String, String> sc = (Map<String, String>) JSONObject
                .toBean(JSONObject.fromObject(updateCondition), Map.class);

        String majorCode = sc.get(Column.MAJOR);
        String minorCode = sc.get(Column.MINOR);

        String sno = sc.get(Column.SNO);

        String reason = sc.get(Column.REASON);

        StatusChangeType type = metadataService
                .getStatusChangeTypeByMinor(majorCode, minorCode);

        // FIXME
        // 是否需要验证学生可以申请学籍异动？
        Student student = studentRepo.findBySno(sno);

        List<StatusChangeRequest> statusChangeRequests = requestRepo
                .findByAuditstatusAndStudent(AuditStatus.待审批, student);

        if (statusChangeRequests.size() > 0) {
            // 该学生目前已经有等待审批的异动
            throw new BusinessException(ErrorCode.E001002);
        }

        // 1. 创建一个新的异动申请
        StatusChangeRequest request = new StatusChangeRequest();
        request.setReason(reason);
        request.setCreatedAt(new Timestamp(new Date().getTime()));
        request.setType(type);
        request.setStudent(student);
        request.setStatus(AuditStatus.待审批);
        // 取得当前用户名
        String userName = SecurityContextHolder.getContext().getAuthentication()
                .getName();
        Account account = accountRepository.findOne(userName);
        // 是教职工代学生申请异动的场合设置代申请人否则为空字符串
        String accountName = account.getStaff() == null ? EMPTY_STRING
                : account.getStaff().getName();
        userName = EMPTY_STRING.equals(accountName) ? EMPTY_STRING : userName;

        // 代申请人帐号
        request.setApplicantUserName(userName);
        // 代申请人真是姓名
        request.setApplicantName(accountName);

        requestRepo.save(request);

        // 20170111 modified by zhufeng
        // 2. 针对异动申请的类型，初始化每一步审批流程
        List<AuditWorkflow> workflows = workflowRepo.findByType(type);
        Timestamp auditAt = new Timestamp(System.currentTimeMillis());
        StatusChangeAudit audit = null;
        Role role = null;
        for (AuditWorkflow workflow : workflows) {
            audit = new StatusChangeAudit();
            audit.setRequest(request);
            audit.setWorkflow(workflow);
            if (workflow.getSequence() == 1) {
                audit.setStatus(AuditStatus.待审批);
            } else {
                audit.setStatus(AuditStatus.审批中);
            }

            role = workflow.getRole();

            audit.setAuditor(
                    this.getAccountByRoleAndUnit(role, student.getUnit()));
            audit.setAuditAt(auditAt);
            auditRepo.save(audit);
        }

        // 最后把学生申请的可能发生学籍变更的信息保存到临时学籍异动表中
        TemporaryStudentChange temporaryStudentChange = new TemporaryStudentChange(
                sno, updateCondition, DEL_FLG_0, request);
        temporaryStudentChangeRepository.save(temporaryStudentChange);
    }

    /**
     * 根据role取得相应的帐号
     * 
     * @param role
     * @param unit
     * @return
     */
    private Account getAccountByRoleAndUnit(Role role, Unit unit) {
        DomainLevel domainLevel = role.getDomainLevel();
        AccountPrivilege accountPrivilege = null;
        try {
            if (DomainLevel.校级.equals(domainLevel)) {
                accountPrivilege = accountPrivilegeRepository
                        .findByUniversity(role).get(0);
            } else if (DomainLevel.一级.equals(domainLevel)) {
                accountPrivilege = accountPrivilegeRepository
                        .findBySchoolAndRole(unit.getSchoolCode(), role).get(0);
            } else if (DomainLevel.二级.equals(domainLevel)) {
                accountPrivilege = accountPrivilegeRepository
                        .findByDepartmentAndRole(unit.getDepartmentCode(), role)
                        .get(0);
            } else {
                throw new BusinessException(ErrorCode.E001005);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new BusinessException(ErrorCode.E001001);
        }
        return accountPrivilege.getAccount();
    }
    
    /**
     * 
     * @param data 需要导入的异动数据
     * @return
     */
    public boolean batchUpdataData(InputStream data) {
        return false;
    }

}
