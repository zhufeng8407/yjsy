package edu.ecnu.yjsy.security.authentication;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ecnu.yjsy.model.auth.Account;
import edu.ecnu.yjsy.model.auth.AccountRepository;

/**
 * 用于读取账号相关的访问域权限信息。
 * 
 * 访问域信息会作为<code>SecurityContext</code>的一部分，在拦截栈的后续过滤器中被使用;
 * 同时访问域信息也会被存储在用户会话中，便于后续访问。
 *
 * @author xiafan
 * @author guhang
 */
@Service
@Transactional
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
class UserDomainDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepo;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Account account = accountRepo.findOne(username);
        if (account != null) return new User(username, account.getPassword(),
                new ArrayList<GrantedDomainAuthority>());
        return null;
    }

}
