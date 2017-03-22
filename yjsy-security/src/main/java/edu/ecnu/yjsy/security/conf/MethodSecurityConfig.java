package edu.ecnu.yjsy.security.conf;

import edu.ecnu.yjsy.security.authorization.CustomMethodSecurityExpressionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * 设置基于方法的安全注解中表达式函数的表达式实现类。
 *
 * @author xiafan
 * @author guhang
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(value = { "edu.ecnu.yjsy.security" })
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Autowired
    private CustomMethodSecurityExpressionHandler expressionHandler;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return expressionHandler;
    }

}
