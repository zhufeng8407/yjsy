package edu.ecnu.yjsy.exception;

/**
 * 构建返回类型class
 * @author freedom.zhu
 *
 * @param <T>
 */
public class RestResultBuilder<T> {

    private int errorCode;

    private String message;;

    private T data ;

    public RestResultBuilder<T> setErrorCode(int errorCode){
        this.errorCode = errorCode;
        return this;
    }

    public RestResultBuilder<T> setMessage(String message){
        this.message = message;
        return this;
    }

    public RestResultBuilder<T> setData(T data){
        this.data = data;
        return this;
    }

    public RestResult<T> build(){
        return new RestResult<T>(errorCode,message,data);
    }


}