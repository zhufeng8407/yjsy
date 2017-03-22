package edu.ecnu.yjsy.web.auth;

import static edu.ecnu.yjsy.constant.Constant.API_AUTH;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.DELETE_IS_GRANTED_BY_ROLES;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.GET_IS_GRANTED_BY_ROLES;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.POST_IS_GRANTED_BY_ROLES;
import static edu.ecnu.yjsy.security.authorization.DomainSecurityExpressionRoot.PUT_IS_GRANTED_BY_ROLES;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import edu.ecnu.yjsy.service.auth.AccountService;

@RestController
// @RequestMapping(API_AUTH + "/accounts")
public class AccountController {
    // @Value("${ecnu.yjsy.auth.logout-url}")
    private final String logoutUrl = "/api/logout";

    // exposed APIS
    private static final String API_ACCOUNT_PREFIX = API_AUTH + "/accounts";

    private static final String API_ACCOUNT_ROLES_AND_CANDS = API_ACCOUNT_PREFIX
            + "/{accountId}/roles-and-cands";

    private static final String API_ACCOUNT_ROLES = API_ACCOUNT_PREFIX
            + "/{accountId}/roles/{roleId}";

    private static final String API_ACCOUNT_ROLES_DOMAINS = API_ACCOUNT_PREFIX
            + "/{accountId}/roles/{roleId}/domains";

    private static final String API_ACCOUNT_ACTIVE_ROLES = API_ACCOUNT_PREFIX
            + "/{accountId}/active-roles";

    private static final String API_ACCOUNT_STAFFS = API_ACCOUNT_PREFIX
            + "/staff-account";

    private static final String API_ACCOUNT_STUDENTS = API_ACCOUNT_PREFIX
            + "/student-account";

    private static final String API_ACCOUNT_PRIVILEGE = API_ACCOUNT_PREFIX
            + "/upload-privilege";

    private static final String API_ACCOUNT_STAFF = API_ACCOUNT_PREFIX
            + "/staff";

    @Autowired
    private AccountService service;

    // FIXME: spring security可以配置logout的处理，但是没有成功，只能临时用这种方法替代
    @RequestMapping(value = logoutUrl, method = GET)
    public void logout(HttpServletRequest request,
            HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }

    /**
     * 根据工号和学号查询账号权限
     *
     * @param searchCondition
     * @return
     */
    @PreAuthorize(GET_IS_GRANTED_BY_ROLES + API_ACCOUNT_PREFIX + "\")")
    @RequestMapping(value = API_ACCOUNT_PREFIX, method = GET)
    @ResponseBody
    public List<Map<String, Object>> query(
            @RequestParam String searchCondition) {
        return service.query(searchCondition);
    }

    /**
     * 返回账号已有的角色,以及尚未获取的角色
     *
     * @param accountId
     * @return
     */
    @PreAuthorize(GET_IS_GRANTED_BY_ROLES + API_ACCOUNT_ROLES_AND_CANDS + "\")")
    @RequestMapping(value = API_ACCOUNT_ROLES_AND_CANDS, method = GET)
    @ResponseBody
    public Map<String, Object> findModifyInfo(@PathVariable String accountId) {
        return service.findGrantedRoles(accountId);
    }

    @PreAuthorize(DELETE_IS_GRANTED_BY_ROLES + API_ACCOUNT_ROLES + "\")")
    @RequestMapping(value = API_ACCOUNT_ROLES, method = DELETE)
    @ResponseBody
    public void deleteRole(@PathVariable long accountId,
            @PathVariable long roleId) {
        service.deleteRole(accountId, roleId);
    }

    /**
     * 为账号添加角色以及角色下对应的具体访问域
     *
     * @param accountId
     * @param roleId
     * @param domains
     * @return
     */
    @PreAuthorize(PUT_IS_GRANTED_BY_ROLES + API_ACCOUNT_ROLES_DOMAINS + "\")")
    @RequestMapping(value = API_ACCOUNT_ROLES_DOMAINS, method = PUT)
    @ResponseBody
    public Map<String, Object> addRole(@PathVariable long accountId,
            @PathVariable long roleId,
            @RequestParam(defaultValue = "") String[] domains) {
        return service.updateRole(accountId, roleId, domains);
    }

    /**
     * 更新账号在某个角色下的具体访问域
     *
     * @param accountId
     * @param roleId
     * @param domains
     * @return
     */
    @PreAuthorize(POST_IS_GRANTED_BY_ROLES + API_ACCOUNT_ROLES_DOMAINS + "\")")
    @RequestMapping(value = API_ACCOUNT_ROLES_DOMAINS, method = POST)
    @ResponseBody
    public Map<String, Object> updateRole(@PathVariable long accountId,
            @PathVariable long roleId,
            @RequestParam(defaultValue = "") String[] domains) {
        return service.updateRole(accountId, roleId, domains);
    }

    /**
     * 获取账号在某个角色下的访问域
     *
     * @param accountId
     * @param roleId
     * @return
     */
    @PreAuthorize(GET_IS_GRANTED_BY_ROLES + API_ACCOUNT_ROLES_DOMAINS + "\")")
    @RequestMapping(value = API_ACCOUNT_ROLES_DOMAINS, method = GET)
    @ResponseBody
    public List<Map<String, Object>> getDomains(@PathVariable long accountId,
            @PathVariable Long roleId) {
        return service.getDomains(accountId, roleId);
    }

    @PreAuthorize(POST_IS_GRANTED_BY_ROLES + API_ACCOUNT_ACTIVE_ROLES + "\")")
    @RequestMapping(value = API_ACCOUNT_ACTIVE_ROLES, method = POST)
    @ResponseBody
    public void setRoles(@PathVariable long accountId,
            @RequestParam long[] roleIds) {
        service.setActiveRoles(accountId, roleIds);
    }

    /**
     * 获取当前用户能够管辖的Staff角色
     */
    @RequestMapping(value = API_ACCOUNT_STAFFS, method = GET)
    @ResponseBody
    public Map<String, Object> getStaffAccount() {
        return service.getStaffAccount();
    }

    /**
     * 获取当前用户能够管辖的Student角色
     */
    @RequestMapping(value = API_ACCOUNT_STUDENTS, method = GET)
    @ResponseBody
    public Map<String, Object> getStudentAccount() {
        return service.getStudentAccount();
    }

    /**
     * 批量修改权限
     */
    @RequestMapping(value = API_ACCOUNT_PRIVILEGE, method = POST)
    @ResponseBody
    public Map<String, Object> uploadPrivilege(
            MultipartHttpServletRequest req) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            InputStream data;
            data = req.getFile("file").getInputStream();
            result = service.uploadPrivilege(data);
        } catch (Exception e) {
            result.put("mes", 0);
            return result;
        }
        return result;
    }

    /**
     * 通过用户名，来查找对应学生的信息。
     **/
    @RequestMapping(value = API_ACCOUNT_STAFF, method = GET)
    @ResponseBody
    public Map<String, Object> findStaffInfoByUsername(
            @RequestParam String username) {
        return service.findStaffInfoByUsername(username);
    }

}
