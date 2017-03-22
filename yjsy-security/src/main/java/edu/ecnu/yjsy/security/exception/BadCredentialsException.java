package edu.ecnu.yjsy.security.exception;

import org.springframework.security.core.AuthenticationException;

public class BadCredentialsException extends AuthenticationException {

    private static final long serialVersionUID = 419501633975755519L;

    private String errMsg;

    private String errCode;

    public BadCredentialsException(String errMsg, String errCode) {
        super(errMsg + "#" + errCode);
    }

    public BadCredentialsException(String errCode) {
        super(errCode);
        this.errCode = errCode;
    }

    public BadCredentialsException() {
        super("");
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

}
