package edu.ecnu.yjsy.security.authorization;

import edu.ecnu.yjsy.model.auth.RestAPIRepository;
import edu.ecnu.yjsy.model.staff.StaffRepository;
import edu.ecnu.yjsy.model.student.StudentRepository;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * 用于配置自定义的权限表达式实现
 *
 * @author guhang
 */
@Service
public class CustomMethodSecurityExpressionHandler
        extends DefaultMethodSecurityExpressionHandler {

    private final static String DEFAULT_ROLE_PREFIX = "ROLE_";

    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    @Autowired
    private RestAPIRepository restAPIRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private StaffRepository staffRepo;

    /**
     * 创建基于security的注解表达式的执行类,
     *
     * @param authentication
     * @param invocation
     * @return
     */
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
            Authentication authentication, MethodInvocation invocation) {
        DomainSecurityExpressionRoot root = new DomainSecurityExpressionRoot(
                authentication, restAPIRepo, studentRepo, staffRepo);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        root.setDefaultRolePrefix(DEFAULT_ROLE_PREFIX);
        return root;
    }

}
