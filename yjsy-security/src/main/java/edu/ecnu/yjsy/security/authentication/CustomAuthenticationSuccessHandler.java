package edu.ecnu.yjsy.security.authentication;

import edu.ecnu.yjsy.model.auth.Account;
import edu.ecnu.yjsy.model.auth.AccountPrivilege;
import edu.ecnu.yjsy.model.auth.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 处理登录成功时的处理逻辑
 *
 * @author guhang
 * @author xiafan
 */

@Component
@Transactional
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    // 从配置文件中读取前端部署的域名
    @Value("${ecnu.yjsy.auth.site-domain}")
    private String siteDomain;

    @Autowired
    private AccountRepository accountRepo;

    /**
     * 设置权限相关的HTTP请求头，主要是设置跨域请求的设置。
     *
     * @param request
     * @param response
     */
    private void addHeaders(HttpServletRequest request,
            HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", siteDomain);
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "*");
        response.addHeader("Access-Control-Allow-Headers",
                request.getHeader("Access-Control-Allow-Headers"));
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // 设置授权信息，以便拦截栈之后的业务逻辑可以访问到
        // 例如DomainAccessDecisionManager中判断判断权限的方法。
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        authentication.getName(),
                        authentication.getCredentials(),
                        authentication.getAuthorities()));

        // 向前端返回用户与权限相关的信息
        addHeaders(request, response);

        // 构建授权的访问域
        StringBuffer roles = new StringBuffer();
        Account account = accountRepo.findOne(authentication.getName());
        for (GrantedAuthority auth : getAuthorities(account)) {
            AccountPrivilege accountPrivilege = ((GrantedDomainAuthority) auth)
                    .getAccountPrivilege();

            // 向前端返回用户及其权限信息
            // 1.一个用户可以有多个角色，每个角色都有相应的访问权限
            // 2.权限信息的具体表示定义在<code>AccountPrivilege.toString()</code>函数中
            roles.append(accountPrivilege.toString());
            roles.append(",");
        }

        // 返回用户的基本信息，以及当前的会话：JSESSIONID
        // 1.JSESSIONID为后续访问系统的验证凭证
        // 2.如果不记录JSESSIONID，用户每个操作都必须重新验证，导致系统无法正常访问
        String uID = account.getStaff() != null ? account.getStaff().getSno()
                : account.getStudent().getSno();

        // 这部分信息会放入用户的Cookie中
        String ret = String.format(
                "{\"state\":%d," + "\"JSESSIONID\":\"%s\", \"userName\":\"%s\","
                        + "\"uID\":\"%s\",\"accountId\":\"%s\",\"roles\":[%s]}",
                1, request.getSession().getId(), authentication.getName(), uID,
                account.getId(), roles.length() == 0 ? ""
                        : roles.substring(0, roles.length() - 1));

        response.getWriter().println(ret);
    }
    
    private Collection<? extends GrantedAuthority> getAuthorities(
            Account account) {
        Set<AccountPrivilege> accountPrivileges = account
                .getAccountPrivileges();
        List<GrantedDomainAuthority> authorities = new ArrayList<>();
        for (AccountPrivilege accountPrivilege : accountPrivileges) {
            authorities.add(new GrantedDomainAuthority(accountPrivilege));
        }
        return authorities;
    }

}
