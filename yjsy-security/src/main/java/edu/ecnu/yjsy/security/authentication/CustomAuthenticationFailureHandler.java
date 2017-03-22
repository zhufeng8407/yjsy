package edu.ecnu.yjsy.security.authentication;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import edu.ecnu.yjsy.security.exception.BadCredentialsException;
import edu.ecnu.yjsy.security.exception.ErrorCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * FIXME 考虑到未来的单点登录机制，这里需要做什么调整吗？
 * 
 * 定义登录验证失败时的处理动作。
 *
 * @author guhang
 */
public class CustomAuthenticationFailureHandler
        implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        if (exception instanceof BadCredentialsException) {
            request.getRequestDispatcher("/securityError?errorCode="
                    + ((BadCredentialsException) exception).getErrCode())
                    .forward(request, response);
        } else {
            request.getRequestDispatcher(
                    "/securityError?errorCode=" + ErrorCode.E00403)
                    .forward(request, response);
        }

    }

}
