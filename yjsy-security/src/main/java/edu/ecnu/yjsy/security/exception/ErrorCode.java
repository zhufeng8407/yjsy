package edu.ecnu.yjsy.security.exception;

/**
 * 报错信息id常量class
 * 
 * @author freedom.zhu
 *
 */
public class ErrorCode {

    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    public static final int WARN = 2;

    public static final String E00400 = "E00400";
    public static final String E00403 = "E00403";
    public static final String E00405 = "E00405";
    public static final String E001001 = "E001001";
    public static final String E001002 = "E001002";
    public static final String E001003 = "E001003";
    public static final String E001004 = "E001004";
    public static final String E001005 = "E001005";

    // ----------------------
    // 用于权限访问的异常处理
    // ----------------------

    /**
     * 用户名不存在。
     */
    public static final String E002001 = "E002001";

    /**
     * 密码错误。
     */
    public static final String E002002 = "E002002";

}
