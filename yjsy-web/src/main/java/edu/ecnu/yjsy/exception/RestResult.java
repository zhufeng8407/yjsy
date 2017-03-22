package edu.ecnu.yjsy.exception;

/**
 * 全局返回类型
 * @author freedom.zhu
 *
 * @param <T>
 */
public class RestResult <T>{
    private int errorCode;
    private String message;
    private T data;

    protected RestResult(){}

    protected RestResult(int errorCode, String message, T data) {
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public RestResult<T> setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public RestResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public RestResult<T> setData(T data) {
        this.data = data;
        return this;
    }
}