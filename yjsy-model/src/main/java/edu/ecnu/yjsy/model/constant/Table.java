package edu.ecnu.yjsy.model.constant;

/**
 * 定义数据表名的常量。
 *
 * @author xulinhao
 */

public interface Table {

    // ========================
    // 元数据
    // ========================

    // 本科专业
    String BACHELOR_DISCIPLINE = "m_bkzy";

    // 校区
    String CAMPUS = "m_xq";

    // 最后学位
    String DEGREE = "m_xw";

    // 层次
    String DEGREE_LEVEL = "m_cc";

    // 学位类型
    String DEGREE_TYPE = "m_xwlx";

    // 专业
    String DISCIPLINE = "m_zy";

    // 最后学历
    String EDUCATION = "m_xl";

    // 学历学习形式
    String EDUCATION_MODE = "m_xxxs";

    // 报考类别
    String ENROLLMENT = "m_bklb";

    // 民族
    String ETHNIC = "m_mz";

    // 考试方式
    String EXAMINATION = "m_ksfs";

    // 考生来源
    String EXAIMINEE_ORIGIN = "m_ksly";

    // 性别
    String GENDER = "m_xb";

    // 毕业类型
    String GRADUATION = "m_bylx";

    // 婚姻状况
    String MARRIAGE = "m_hy";

    // 医疗类型
    String MEDICAL = "m_yllx";

    // 现役军人
    String MILITARY = "m_xyjr";

    // 国籍
    String NATION = "m_gj";

    // 入学前学习工作单位性质
    String PRESTUDY = "m_rxqxxgzdwxz";

    // 火车到站
    String RAILWAY = "m_hcdz";

    // 地区
    String REGION = "m_dq";

    // 政治面貌 = 宗教
    String RELIGION = "m_zzmm";

    // 专项计划
    String SPECIAL_PLAN = "m_zxjh";

    // 证件类型
    String SSN_TYPE = "m_zjlx";

    // 院系
    String UNIT = "m_yx";

    // 高等院校
    String UNIVERSITY = "m_gdyx";

    // 异动类型
    String STATUS_CHANGE_TYPE = "m_yd_lx";

    // 预毕业字段配置信息
    String APIMAPPING = "m_apimapping";

    // ========================
    // 学籍
    // ========================

    // 困难认定
    String DIFFICULTY = "t_xj_knrd";

    // 入学测试
    String EXAM = "t_xj_rxcs";

    // 注册报到
    String REGISTRATION = "t_xj_bdzc";

    // 缴费
    String FEE = "t_xj_jf";

    // 学籍
    String STUDENT = "t_xj";

    // 职工
    String STAFF = "t_jzg";

    // ========================
    // 异动
    // ========================

    String AUDIT_WORKFLOW = "t_yd_lc";

    String STATUS_CHANGE_REQUEST = "t_yd_sq";

    String STATUS_CHANGE_AUDIT = "t_yd_sp";

    // ========================
    // 权限
    // ========================
    // 账号表
    String ACCOUNT = "t_account";

    // 账号权限表
    String ACCOUNT_PRIVILEGE = "t_zh_qx";

    // 角色表
    String ROLE = "t_role";

    // 账号与角色的关联表
    String REL_ACCOUNT_ROLE = "t_zh_js";

    // 方法权限表
    String METHOD_PRIVILEGE = "t_rest_api";

    // 页面权限表
    String PAGE_PRIVILEGE = "t_ym_qx";

    // 页面权限与方法权限的关联表
    String REL_PAGE_METHOD_PRIVILEGE = "t_ymqx_faqx";

    //API接口与角色的关联
    String REL_METHODPRIVILEGE_ROLE = "t_api_role";

    //角色和页面之间的关联
    String REL_ROLE_PAGE = "t_role_page";

    // ========================
    // 预毕业
    // ========================
    // 预毕业信息配置表
    String PREGRADUATION_CONFIG = "t_yby_pz";

    // 预毕业信息申请表
    String PREGRADUATION_REQUEST = "t_yby_sq";

    // ==========================
    // 通知表
    String MESSAGE = "t_tz";

    String MESSAGE_ACCOUNT = "t_tz_zh";

    // 通知群表
    String MESSAGEGROUP = "t_tz_q";

    String MESSAGEGROUP_MEMBER = "t_tz_jsr";

    // 学籍异动临时表
    String TEMPORARY_STUDENT_CHANGE = "t_ls_xj_yd";

    /**
     * 缴费临时表
     * 为了从流水表中计算出学生缴纳学费的应缴总额和实缴总额而创建的临时表。
     */
    String TEMPORARY_FEE = "t_ls_fee";
    
    //学籍毕业离校审核表
    String LEAVE = "t_xj_bylx_sh";
}
