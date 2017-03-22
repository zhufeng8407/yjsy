package edu.ecnu.yjsy.security.authorization;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import edu.ecnu.yjsy.model.auth.RestAPIRepository;
import edu.ecnu.yjsy.model.staff.StaffRepository;
import edu.ecnu.yjsy.model.student.StudentRepository;

/**
 * 用于实现权限注解中对应自定义注解函数实现的类，目前实现的权限注解函数包括：
 * <ul>
 * <li>1. <code>isGrantedByRoles</code>:
 * 该注解用于只需要通过用户拥有的角色与方法访问所需的角色判断用户能否访问方法</li>
 * <li>2. <code>hasAccessToStudent</code>:
 * 这个注解用于注释那些方法中明确指明需要访问的学生ID的情况，也即注释那些指访问一个学生的 方法</li>
 * <li>3. <code>hasDomainsPrivilegeByCondition</code>:
 * 基于查询中的搜索条件，结合当前用户的访问权限，判断用户是否有权调用方法， 这个注解通常用于注释那些访问一批学生的方法</li>
 * </ul>
 * FIXME：目前方法的访问角色还是需要同通过关联的PagePrivilege去获得
 * FIXME：由于权限是存储在Session中的，而只要用户没有重新登录，权限部分是不会更新的，
 * 因此如果在这期间更新用户的访问权限的话，是需要用户重新登录的。 FIXME：每次新增老师和学生的时候，都需要为他们设置相应的初始权限
 *
 * @author guhang
 * @author xiafan
 */

public class DomainSecurityExpressionRoot extends SecurityExpressionRoot
        implements MethodSecurityExpressionOperations {

    // 以下是权限相关注解的模板，由于注解中只能使用常量，此处不能使用静态函数替代

    // isGrantedByRoles的HTTP方法的注解
    public static final String GET_IS_GRANTED_BY_ROLES = "isGrantedByRoles(\"GET\",\"";

    public static final String DELETE_IS_GRANTED_BY_ROLES = "isGrantedByRoles(\"DELETE\",\"";

    public static final String POST_IS_GRANTED_BY_ROLES = "isGrantedByRoles(\"POST\",\"";

    public static final String PUT_IS_GRANTED_BY_ROLES = "isGrantedByRoles(\"PUT\",\"";

    // hasDomainsPrivilegeByCondition的HTTP方法的注解
    public static final String GET_HAS_DOMAINS_BY_CONDITION = "hasDomainsPrivilegeByCondition(\"GET\",\"";

    public static final String POST_HAS_DOMAINS_BY_CONDITION = "hasDomainsPrivilegeByCondition(\"POST\",\"";

    public static final String PUT_HAS_DOMAINS_BY_CONDITION = "hasDomainsPrivilegeByCondition(\"PUT\",\"";

    public static final String DELETE_HAS_DOMAINS_BY_CONDITION = "hasDomainsPrivilegeByCondition(\"DELETE\",\"";

    // hasAccessToStudent的HTTP方法的注解
    public static final String GET_HAS_ACCESS_TO_STUDENT = "hasAccessToStudent(\"GET\",\"";

    public static final String POST_HAS_ACCESS_TO_STUDENT = "hasAccessToStudent(\"POST\",\"";

    public static final String PUT_HAS_ACCESS_TO_STUDENT = "hasAccessToStudent(\"PUT\",\"";

    public static final String DELETE_HAS_ACCESS_TO_STUDENT = "hasAccessToStudent(\"DELETE\",\"";

    private DomainBasedAuthorizer authorizer;

    public DomainSecurityExpressionRoot(Authentication authentication,
            RestAPIRepository restAPIRepo, StudentRepository studentRepo,
            StaffRepository staffRepository) {
        super(authentication);
        this.authorizer = new DomainBasedAuthorizer(restAPIRepo, studentRepo,
                staffRepository);
    }

    /**
     * This privilege api is used to intercept roles without domains
     *
     * @param api
     *            具体的方法名，和后台的API要一一对应，不是前台的页面URL
     */
    public boolean isGrantedByRoles(String httpMethod, String api) {
        DomainBasedAuthorizer.AuthorizationContext context = authorizer
                .getDomainsGrantedByAPI(api, httpMethod);
        return context.isAcceptedByRole();
    }

    /**
     * 这个注解用于注释那些方法中明确指明需要访问的学生ID的情况，也即注释那些指访问一个学生的方法
     */
    public boolean hasAccessToStudent(String httpMethod, String api,
            String sno) {
        DomainBasedAuthorizer.AuthorizationContext context = authorizer
                .getDomainsGrantedByAPI(api, httpMethod);
        return authorizer.isAccessGrantedByDomain(context, sno);
    }

    /**
     * This privilege api is used to intercept roles with a student's number
     */
    public boolean hasDomainsPrivilegeByCondition(String httpMethod, String api,
            String condition) {
        return authorizer.hasDomainsPrivilegeByCondition(httpMethod, api,
                condition);
    }

    // XXX
    // 以下几个函数用于通过注解过滤函数调用返回结果的功能，目前暂不实现。
    // <code>CustomMethodSecurityExpressionHandler</code>继承了<code>DefaultMethodSecurityExpressionHandler</code>,
    // 导致当前类必须实现接口<code>MethodSecurityExpressionOperations</code>。
    @Override
    public void setFilterObject(Object filterObject) {}

    @Override
    public Object getFilterObject() {
        return null;
    }

    @Override
    public void setReturnObject(Object returnObject) {}

    @Override
    public Object getReturnObject() {
        return null;
    }

    @Override
    public Object getThis() {
        return null;
    }

}
