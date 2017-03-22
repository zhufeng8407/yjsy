package edu.ecnu.yjsy.exception;

/**
 * 内部exception封装类
 * 
 * @author freedom.zhu
 *
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 416501633975755519L;

    private String errMsg;

    private String errCode;

    public BusinessException(String errMsg, String errCode) {
        super(errMsg + "#" + errCode);
    }

    public BusinessException(String errCode) {
        super(errCode);
        this.errCode = errCode;
    }

    public BusinessException() {
        super();
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