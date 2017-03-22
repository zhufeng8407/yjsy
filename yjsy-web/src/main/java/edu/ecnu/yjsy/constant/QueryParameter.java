package edu.ecnu.yjsy.constant;

/**
 * 定义与前端传递参数时的参数名
 *
 * @author xiafan
 */
public class QueryParameter {
    public static final String ID = "id";

    // 以下用于实现分页
    public static final String PARAM_ITEMS = "items";

    public static final String PARAM_ITEMS_COUNT = "count";

    public static final String PARAM_PAGE_SIZE = "size";

    public static final String PARAM_PAGE = "page";

    // ----------------------学籍相关---------------------------

    public static final String STUDENT_NAME = "name";

    public static final String STUDENT_PHOTO = "photo";

    // 学生ID，非学号
    public static final String STUDENT_ID = "stuID";

    // 学号
    public static final String STUDENT_NO = "sno";

    public static final String ADMISSION_DATE = "adminssionDate";

    public static final String GRADE = "grade";

    public static final String IS_FEE = "isFee";

    public static final String IS_NEW = "new";

    public static final String REGISTRATION = "registration";

    public static final String REPORT = "report";

    public static final String DEGREE_TYPE = "degreeType";

    public static final String EDUCATION_MODE = "educationMode";
    // -----------职工相关-------------------

    // 职工ID，不是职工号
    public static final String STAFF_ID = "staffID";

    // 职工号
    public static final String STAFF_NO = "staffNo";

    public static final String STAFF_NAME = "staffName";

    public static final String PROFESSION = "profession";

    public static final String IS_MASTER_SUPERVISOR = "isMasterSupervisor";

    public static final String IS_DOCTOR_SUPERVISOR = "isDoctorSupervisor";

    // -----------------组织机构相关--------------
    /* 在<code>DomainAccessDecisionManager</code>中用到以下变量，如果修改的话需要同时修改安全中的代码 */
    public static final String SCHOOLS = "schools";

    public static final String DEPARTMENTS = "departs";

    // 一级单位编码
    public static final String SCHOOL_CODE = "schoolCode";

    // 一级单位名称
    public static final String SCHOOL_NAME = "schoolName";

    // 二级单位编码
    public static final String DEPARTMENT_CODE = "departmentCode";

    // 二级单位编码
    public static final String DEPARTMENT_NAME = "departmentName";

    // 导师ID，非职工号
    public static final String SUPERVISOR_ID = "supervisorID";

    // -------------权限管理相关-----------------
    // 帐号ID
    public static final String ACCOUNT_ID = "accountID";

    // 账号用户名
    public static final String ACCOUNT_USERNAME = "username";

    // 角色id
    public static final String ROLE_ID = "roleID";

    // 角色名
    public static final String ROLE_NAME = "roleName";

    // 访问域级别
    public static final String DOMAIN_LEVEL = "domainLevel";

    public static final String DOMAINS = "domains";

    public static final String IS_UNIVERSITY = "isUniversity";

}
