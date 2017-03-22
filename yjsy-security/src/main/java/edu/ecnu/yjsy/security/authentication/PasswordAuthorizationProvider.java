package edu.ecnu.yjsy.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import edu.ecnu.yjsy.security.exception.BadCredentialsException;
import edu.ecnu.yjsy.security.exception.ErrorCode;

/**
 * 登录验证的实现方法
 *
 * @author xiafan
 * @author guhang
 */
@Service
public class PasswordAuthorizationProvider implements AuthenticationProvider {

    @Autowired
    private UserDomainDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (user == null)
            throw new BadCredentialsException(ErrorCode.E002001);
        if (!user.getPassword().equals(password))
            throw new BadCredentialsException(ErrorCode.E002002);

        return new UsernamePasswordAuthenticationToken(username, password,
                user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

}
