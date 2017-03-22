package edu.ecnu.yjsy.security.conf;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionManagementFilter;

import edu.ecnu.yjsy.security.authentication.CustomAuthenticationFailureHandler;
import edu.ecnu.yjsy.security.authentication.CustomAuthenticationSuccessHandler;
import edu.ecnu.yjsy.security.authentication.PasswordAuthorizationProvider;
// import edu.ecnu.yjsy.security.authorization.CustomAccessDeniedHandler;
import edu.ecnu.yjsy.security.authorization.DomainAccessDecisionManager;

/**
 * 配置用于安全模块拦截的访问路径（即URL），以及用于验证和授权的处理类。
 *
 * @author guhang
 * @author xiafan
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${ecnu.yjsy.auth.login-url}")
    private String loginAPIURL = "/api/login";

    @Value("${ecnu.yjsy.auth.enabled}")
    private boolean isEnabled = true;

    @Autowired
    DomainAccessDecisionManager domainAccessDecisionManager;

    @Autowired
    private PasswordAuthorizationProvider authenticationProvider;

    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessionHandler;

    private CustomAuthenticationFailureHandler authenticationFailureHandler = new CustomAuthenticationFailureHandler();

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().permitAll().loginProcessingUrl(loginAPIURL)
                .successHandler(authenticationSuccessionHandler)
                .failureHandler(authenticationFailureHandler).and().csrf()
                .disable(); // FIXME: 关闭这个是不是会带来潜在的安全风险？
        if (isEnabled) {
            http.exceptionHandling()
                    .accessDeniedHandler(new RestAccessDeniedHandler())//用于处理<code>AccessDeniedException的异常</code>
                    .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                    .and().authorizeRequests().antMatchers(loginAPIURL)
                    .permitAll().and().authorizeRequests()
                    .accessDecisionManager(domainAccessDecisionManager)
                    .antMatchers("/api/**").access("authorized");
            http.addFilterAfter(expiredSessionFilter(),
                    SessionManagementFilter.class);
        } else {
            http.authorizeRequests().antMatchers("*").permitAll();
        }
    }

    private static class RestAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            Authentication auth = SecurityContextHolder.getContext()
                    .getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, null, auth);
            }
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            //FIXME: error can not be passed to front end
            response.getWriter().write(String.format("{message:%s}", accessDeniedException.getMessage()));
        }
    }


    private Filter expiredSessionFilter() {
        SessionManagementFilter smf = new SessionManagementFilter(
                new HttpSessionSecurityContextRepository());
        smf.setInvalidSessionStrategy(new InvalidSessionStrategy() {
            @Override
            public void onInvalidSessionDetected(HttpServletRequest request,
                                                 HttpServletResponse response)
                    throws IOException, ServletException {
                // 当session过期或者用户使用非法的JESSIONID访问系统时,抛出未授权的错误
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "您尚未登录或会话已过期!");
            }
        });
        return smf;
    }

}
