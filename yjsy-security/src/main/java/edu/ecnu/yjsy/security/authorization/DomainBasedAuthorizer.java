package edu.ecnu.yjsy.security.authorization;

import static edu.ecnu.yjsy.security.util.QueryParameter.STAFF_NO;
import static edu.ecnu.yjsy.security.util.QueryParameter.STUDENT_NO;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import edu.ecnu.yjsy.model.auth.AccountPrivilege;
import edu.ecnu.yjsy.model.auth.DomainLevel;
import edu.ecnu.yjsy.model.auth.RestAPI;
import edu.ecnu.yjsy.model.auth.RestAPIRepository;
import edu.ecnu.yjsy.model.auth.Role;
import edu.ecnu.yjsy.model.staff.StaffRepository;
import edu.ecnu.yjsy.model.student.StudentRepository;
import edu.ecnu.yjsy.model.student.Unit;
import edu.ecnu.yjsy.model.view.student.StudentAuthSummary;
import edu.ecnu.yjsy.security.authentication.GrantedDomainAuthority;
import net.sf.json.JSONObject;

/**
 * 基于权限域<code>AccountPrivilege</code>
 *
 * @author xiafan
 */
@Service
public class DomainBasedAuthorizer {

    private static final AuthorizationContext DENIED_AUTH_CONTEXT = new AuthorizationContext(
            null, false, new HashSet<>());

    private static final AuthorizationContext FULL_GRANTED_AUTH_CONTEXT = new AuthorizationContext(
            null, true, new HashSet<>());

    @Autowired
    private RestAPIRepository restAPIRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private StaffRepository staffRepo;

    public DomainBasedAuthorizer() {

    }

    public DomainBasedAuthorizer(RestAPIRepository restAPIRepo,
            StudentRepository studentRepo, StaffRepository staffRepo) {
        this.restAPIRepo = restAPIRepo;
        this.studentRepo = studentRepo;
        this.staffRepo = staffRepo;
    }

    /**
     * 为了提高某个请求中大批量授权的性能,将通用的授权文境信息保存在当前类中,以避免频繁构建授权信息
     */
    public static class AuthorizationContext {
        RestAPI restApi;
        boolean isApiFullGranted; // api是否任何用户都可以使用
        Set<AccountPrivilege> privileges;

        public AuthorizationContext(RestAPI restApi, boolean isApiFullGranted,
                Set<AccountPrivilege> privileges) {
            this.restApi = restApi;
            this.isApiFullGranted = isApiFullGranted;
            this.privileges = privileges;
        }

        public boolean isAcceptedByRole() {
            return isApiFullGranted || !privileges.isEmpty();
        }
    }

    public AuthorizationContext getDomainsGrantedByAPI(String api,
            String httpMethod) {
        AuthorizationContext ctx;

        Set<AccountPrivilege> ret = new HashSet<>();
        RestAPI restAPI = restAPIRepo.findByApiAndHttpMethod(api, httpMethod);
        if (restAPI == null)
            ctx = DENIED_AUTH_CONTEXT;
        else if (restAPI.getRoles() == null || restAPI.getRoles().isEmpty()) {
            // 如果方法没有配置相应的角色,那么就认为所有的用户都可以访问
            ctx = FULL_GRANTED_AUTH_CONTEXT;
        } else {
            for (GrantedAuthority authority : SecurityContextHolder.getContext()
                    .getAuthentication().getAuthorities()) {
                if (authority instanceof GrantedDomainAuthority
                        && restAPI.getRoles()
                                .contains(((GrantedDomainAuthority) authority)
                                        .getAccountPrivilege().getRole()))
                    ret.add(((GrantedDomainAuthority) authority)
                            .getAccountPrivilege());
            }
            ctx = new AuthorizationContext(restAPI, false, ret);
        }
        return ctx;
    }

    /**
     * 检查用户是否有权限访问某个院系学生的权限
     */
    public boolean isAccessGrantedByDomain(AuthorizationContext context,
            String sno) {
        if (context.isApiFullGranted) {
            return true;
        } else if (sno == null) {
            return false;
        } else {
            StudentAuthSummary student = studentRepo.findStudentBySno(sno);
            if (student == null) return true;
            Long supervisorID = student.getSupervisor().getId();

            for (AccountPrivilege privilege : context.privileges) {
                if (isAccessGrantedByDomain(privilege, student.getId(),
                        supervisorID, student.getUnit()))
                    return true;
            }
            return false;
        }
    }

    public boolean hasAccessBySupervisor(String httpMethod, String api,
            String staffNo) {
        Long supID = staffRepo.findIdBySno(staffNo);
        return isAccessGrantedByDomain(getDomainsGrantedByAPI(api, httpMethod),
                null, supID, null);
    }

    /**
     * 这个注解用于注释那些方法中明确指明需要访问的学生ID的情况，也即注释那些指访问一个学生的方法
     */
    public boolean hasAccessToStudent(String httpMethod, String api,
            String sno) {
        return isAccessGrantedByDomain(getDomainsGrantedByAPI(api, httpMethod),
                sno);
    }

    /**
     * 这个注解用于注释那些方法中明确指明需要访问的学生ID的情况，也即注释那些指访问一个学生的方法
     */
    public boolean hasAccessToSupervisor(String httpMethod, String api,
            String staffNo) {
        Long staffId = staffRepo.findIdBySno(staffNo);
        return isAccessGrantedByDomain(getDomainsGrantedByAPI(api, httpMethod),
                null, staffId, null);
    }

    @SuppressWarnings("unchecked")
    public boolean hasDomainsPrivilegeByCondition(String httpMethod, String api,
            String condition) {
        Map<String, Object> map = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(condition), Map.class);
        // 先判断是不是访问特定学生
        if (map.get(STUDENT_NO) != null
                && !map.get(STUDENT_NO).toString().isEmpty()) {
            return hasAccessToStudent(httpMethod, api,
                    map.get("sno").toString());
        } else if (map.get("name") != null) {
            List<String> snos = studentRepo
                    .findSnoByName(map.get("name").toString());
            if (snos.isEmpty()) {
                // 学生不存在，也就不存在访问的权限问题
                return true;
            } else if (snos.size() == 1)
                return hasAccessToStudent(httpMethod, api, snos.get(0));
        }

        // 后判断是否访问某个导师
        if (map.get(STAFF_NO) != null && !map.get(STAFF_NO).toString()
                .isEmpty()) { return hasAccessToSupervisor(httpMethod, api,
                        map.get(STAFF_NO).toString()); }

        Unit unit = new Unit();
        if (map.get("schoolCode") != null)
            unit.setSchoolCode(map.get("schoolCode").toString());
        if (map.get("departmentCode") != null)
            unit.setDepartmentCode(map.get("departmentCode").toString());

        return isAccessGrantedByDomain(getDomainsGrantedByAPI(api, httpMethod),
                null, null, unit);
    }

    /**
     * 检查用户是否有权限访问某个院系学生的权限
     */
    public boolean isAccessGrantedByDomain(AuthorizationContext context,
            Long stuID, Long supervisorID, Unit unit) {
        if (context.isApiFullGranted) {
            return true;
        } else {
            if (stuID != null) {
                StudentAuthSummary student = studentRepo.findStudentById(stuID);
                if (student == null) { return true; }
                supervisorID = student.getSupervisor().getId();
            }

            for (AccountPrivilege privilege : context.privileges) {
                if (isAccessGrantedByDomain(privilege, stuID, supervisorID,
                        unit))
                    return true;
            }
            return false;
        }
    }

    /**
     * 检查用户是否有权限访问一个特定的学生
     */
    private boolean isAccessGrantedByDomain(AccountPrivilege accountPrivilege,
            Long stuID, Long supID, Unit unit) {
        Role role = accountPrivilege.getRole();
        DomainLevel domain = role.getDomainLevel();
        switch (domain) {
        case 校级:
            return true;

        case 一级:
            return (unit != null) && (accountPrivilege.getSchoolCode()
                    .equals(unit.getSchoolCode()));

        case 二级:
            return (unit != null) && (accountPrivilege.getDepartmentCode()
                    .equals(unit.getDepartmentCode()));

        case 导师:
            return (supID != null) && (accountPrivilege.getAccount().getStaff()
                    .getId().equals(supID));

        case 学生:
            return (stuID != null) && (accountPrivilege.getAccount()
                    .getStudent().getId().equals(stuID));

        default:
            return false;
        }
    }

}
