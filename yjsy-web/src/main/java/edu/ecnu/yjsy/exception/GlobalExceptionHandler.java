package edu.ecnu.yjsy.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.ecnu.yjsy.security.exception.ErrorCode;

/**
 * 全局异常控制类
 *
 * @author freedom.zhu
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @Autowired
    private ErrorMsgProperties errorMsgProperties;

    public GlobalExceptionHandler() {
        super();
    }

    /**
     * 将用户注销
     *
     * @param request
     */
    public static void clearUserSession(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, null, auth);
        }
    }

    /**
     * 400 - Bad Request
     *
     * @param ex      HttpMessageNotReadableException
     * @param request HttpServletRequest
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public <T> RestResult<T> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        clearUserSession(request);
        return new RestResultBuilder<T>().setErrorCode(ErrorCode.ERROR)
                .setMessage(errorMsgProperties.getProperty(ErrorCode.E00400))
                .build();
    }

    /**
     * 403 - Forbidden
     *
     * @param accessDeniedException
     * @param request
     * @return
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public <T> RestResult<T> handleIndexOutOfBoundsException(
            AccessDeniedException accessDeniedException,
            HttpServletRequest request) {
        clearUserSession(request);
        return new RestResultBuilder<T>().setErrorCode(ErrorCode.ERROR)
                .setMessage(errorMsgProperties.getProperty(ErrorCode.E00403))
                .build();
    }

    /**
     * 405 - Method Not Allowed
     *
     * @param methodNotSupportedException
     * @param request
     * @return
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public <T> RestResult<T> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException methodNotSupportedException,
            HttpServletRequest request) {
        clearUserSession(request);
        return new RestResultBuilder<T>().setErrorCode(ErrorCode.ERROR)
                .setMessage(errorMsgProperties.getProperty(ErrorCode.E00405))
                .build();
    }

    /**
     * 500 - INTERNAL_SERVER_ERROR
     *
     * @param businessException
     * @param request
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BusinessException.class)
    public <T> RestResult<T> handleBusinessException(
            BusinessException businessException, HttpServletRequest request) {
        return new RestResultBuilder<T>().setErrorCode(ErrorCode.WARN)
                .setMessage(errorMsgProperties
                        .getProperty(businessException.getErrCode()))
                .build();
    }

}