package edu.ecnu.yjsy.model.constant;

/**
 * 定义字段名的常量。
 *
 * @author xulinhao
 */

public interface Column {
    String STAR = "*";

    // ========================
    // 元数据
    // ========================

    // 代码
    String META_CODE = "dm";

    // 名称
    String META_NAME = "mc";

    // 地区
    // 国家
    String META_NATION = "gj";

    // 省
    String META_STATE = "sf";

    String META_STATE_CODE = "sfdm";

    // 市
    String META_CITY = "cs";

    String META_CITY_CODE = "csdm";

    // 区县
    String META_COUNTY = "xq";

    String META_COUNTY_CODE = "xqdm";

    // 邮编
    String META_ZIPCODE = "yzbm";

    // 火车到站 - 拼音
    String META_SPELLING = "py";

    // 院系
    // 校级单位
    String META_DIVISION = "xjdwmc";

    String META_DIVISION_CODE = "xjdwdm";

    // 教学一级单位 - 部院
    String META_SCHOOL = "bymc";

    String META_SCHOOL_CODE = "bydm";

    String META_SCHOOL_TYPE = "bylx";

    // 教学二级单位 - 院系
    String META_DEPARTMENT = "yxmc";

    String META_DEPARTMENT_CODE = "yxdm";

    String META_DEPARTMENT_TYPE = "yxlx";

    // 启用年份
    String META_SINCE = "qynf";

    // 失效年份
    String META_UNTIL = "sxnf";

    // 代码来源
    String META_SOURCE = "lydm";

    // 代码去向
    String META_TARGET = "qxdm";

    // 专业
    // 门类
    String META_CATEGORY = "mlmc";

    String META_CATEGORY_CODE = "mldm";

    // 一级学科
    String META_MAJOR = "yjxkmc";

    String META_MAJOR_CODE = "yjxkdm";

    // 二级学科
    String META_MINOR = "zymc";

    String META_MINOR_CODE = "zydm";

    // 原专业代码
    String META_ORIGIN_CODE = "yzydm";

    // 层次
    String META_DEGREE_LEVEL = "cc";

    // 学位类型
    String META_DEGREE_TYPE = "xwlx";

    // 专业设置区域
    String META_AREA = "zyszqy";

    // 性质 - 用于高等院校或专业
    String META_NATURE = "xz";

    // 高等院校
    // 办学类型代码
    String META_OPERATION_CODE = "bxlxdm";

    // 办学类型
    String META_OPERATION_1 = "bxlxmc1";

    String META_OPERATION_2 = "bxlxmc2";

    String META_OPERATION_3 = "bxlxmc3";

    // 性质类别
    String META_NATURE_CODE = "xzdm";

    // 是否985
    String META_SF_985 = "sf985";

    // 是否211
    String META_SF_211 = "sf211";

    // ========================
    // 学籍
    // ========================

    // 1. ID
    String ID = "id";

    // 2. 姓名
    String NAME = "xm";

    // 3. 姓名英文
    String NAME_EN = "xmyw";

    // 4. 考生编号
    String EXAMINEE_NO = "ksbh";

    // 5. 报名点代码
    String EXAMINEE_SIGN = "bmdm";

    // 6. 证件类型码
    String SSN_TYPE = "zjlx";

    // 7. 证件号码
    String SSN = "zjhm";

    // 8. 出生日期
    String BIRTHDATE = "csrq";

    // 9. 国籍码
    String NATION = "fk_gj";

    // 10. 民族码
    String ETHNIC = "fk_mz";

    // 11. 性别码
    String GENDER = "fk_xb";

    // 12. 婚姻状况码
    String MARRIAGE = "fk_hy";

    // 13. 现役军人码
    String MILITARY = "fk_xyjr";

    // 14. 入伍批准书编号
    String MILITARY_JOIN_NO = "rwpzsbh";

    // 15. 退出现役证编号
    String MILITARY_QUIT_NO = "tcxyzdh";

    // 16. 政治面貌码
    String RELIGION = "fk_zzmm";

    // 17. 籍贯所在地码
    String HOMETOWN = "fk_jgszd";

    // 18. 出生地码
    String BIRTHPLACE = "fk_csd";

    // 19. 户口所在地码
    String RESIDENCE = "fk_hkszd";

    // 20. 户口所在地详细地址
    String RESIDENCE_ADDRESS = "hkszdxxdz";

    // 21. 家庭所在地码
    String FAMILY = "fk_jtszd";

    // 22. 家庭所在地详细地址
    String FAMILY_ADDRESS = "jtszdxxdz";

    // 23. 火车到站码
    String RAILWAY = "fk_hcdz";

    // 24. 考生档案所在地码
    String EXAMINEE_ARCHIVE = "fk_daszd";

    // 25. 考生档案所在单位
    String EXAMINEE_ARCHIVE_UNIT = "daszdw";

    // 26. 考生档案所在单位地址
    String EXAMINEE_ARCHIEVE_UNIT_ADDRESS = "daszdwdz";

    // 27. 考生档案所在单位邮政编码
    String EXAMINEE_ARCHIEVE_UNIT_ZIPCODE = "daszdwyzbm";

    // 28. 入学前学习或工作单位性质
    String PRESTUDY_UNIT_NATURE = "fk_rxqxxgzdwxz";

    // 29. 入学前学习或工作单位所在地码
    String PRESTUDY = "fk_rxqxxgzdwszd";

    // 30. 入学前学习或工作单位
    String PRESTUDY_UNIT = "rxqxxgzdw";

    // 31. 学习与工作经历
    String EXPERIENCE = "xxgzjl";

    // 32. 家庭主要成员
    String FAMILY_MEMBER = "jtcy";

    // 33. 紧急联系人
    String EMERGENCY_CONTACT = "jjlxr";

    // 34. 微信
    String WECHAT = "wx";

    // 35. 固定电话
    String PHONE = "lxdh";

    // 36. 移动电话
    String MOBILE = "yddh";

    // 37. 电子信箱
    String EMAIL = "dzxx";

    // 38. 学号
    String SNO = "xh";

    // 39. 照片
    String PHOTO = "zp";

    // 40. 是否新生
    String IS_NEW = "sfxs";

    // 41. 报到码
    String CHECKIN_NO = "bdm";

    // 42. 是否报到
    String IS_ADMISSION = "sfbd";

    // 43. 层次
    String DEGREE_LEVEL = "fk_cc";

    // 44. 学位类型
    String DEGREE_TYPE = "fk_xwlx";

    // 45. 是否统招
    String IS_ADMISSION_UNIFIED = "sftz";

    // 46. 学习形式
    String EDUCATION_MODE = "fk_xxxs";

    // 47. 入学日期
    String ADMISSION_DATE = "rxrq";

    // 48. 年级
    String GRADE = "nj";

    // 49. 学期
    String TERM = "xq";

    // 50. 报到
    String IS_CHECKIN = "sfbd";

    // 51. 注册
    String IS_REGISTER = "sfzc";

    // 52. 缴费
    String IS_FEE = "sfjf";

    // 53. 报到注册缴费 - 流水表

    // 54. 当前学年应缴费
    String FEE_SHALL_PAY = "dqxnyjf";

    // 55. 学费总额
    String FEE_TOTAL = "xfze";

    // 56. 缴费标准
    String FEE_STANDARD = "jfbz";

    // 57. 已缴费次数
    String FEE_TIMES = "yjfcs";

    // 58. 已缴费总额
    String FEE_TOTAL2 = "yjfze";

    // 59. 学费一
    String FEE1 = "xf1";

    // 60. 学费二
    String FEE2 = "xf2";

    // 61. 学费三
    String FEE3 = "xf3";

    // 62. 学费四
    String FEE4 = "xf4";

    // 63. 学费五
    String FEE5 = "xf5";

    // 64. 校区
    String CAMPUS = "fk_xq";

    // 65. 宿舍
    String DOMITORY = "ss";

    // 66. 导师
    String SUPERVISOR = "fk_ds";

    // 67. 专业
    String DISCIPLINE = "fk_zy";

    // 68. 研究方向
    String RESEARCH = "yjfx";

    // 69. 院系
    String UNIT = "fk_yx";

    // 70. 学制
    String LOS = "xz";

    // 71. 专业排名
    String RANK = "zypm";

    // 72. 硕博连读硕士阶段的入学年级
    String MASTER_GRADE = "sbldrxnj";

    // 73. 最近学籍变动情况 - 流水表

    // 74. 奖惩
    String AWARD_AND_PUNISHMENT = "jc";

    // 75. 医疗类型
    String MEDICAL = "fk_yllx";

    // 76. 困难认定 - 流水表

    // 77. 考试方式码
    String EXAMINATION = "fk_ksfs";

    // 78. 专项计划码
    String SPECIAL_PLAN = "fk_zxjh";

    // 79. 专项计划说明
    String SPECIAL_PLAN_MEMO = "zxjhsm";

    // 80. 录取类别码
    String ENROLLMENT = "fk_bklb";

    // 81. 定向就业单位所在地码
    String DIRECTED_REGION = "fk_dxwpdwszd";

    // 82. 定向就业单位
    String DIRECTED_WORK = "dxwpdw";

    // 83. 联合培养单位代码
    String JOINT_TRAINING = "lhpydwm";

    // 84. 联合培养单位名称
    String JOINT_TRAINING_WORK = "lhpydw";

    // 85. 跨专业信息
    String CROSS_DISCIPLINE = "kzyxx";

    // 86. 毕业类型
    String GRADUATION = "fk_bylx";

    // 87. 预计毕业日期
    String EXPECTED_GRADUATION_DATE = "yjbyrq";

    // 88. 实际毕业日期
    String ACTUAL_GRADUATION_DATE = "sjbyrq";

    // 89. 休学学期数
    String ABSENCE_TERMS = "xxxqs";

    // 90. 正常毕业年限
    String DURATION = "bynx";

    // 91. 最长延期
    String MAX_DURATION = "zcbynx";

    // @deprecated
    // 92. 预毕业审核状态
    String PREGRADUATION_STATUS = "ybyshzt";

    // 93. 预毕业审核日期
    String PREGRADUATION_DATE = "ybyshrq";

    // 94. 授予学位名称
    String GRANTED_DEGREE = "syxwmc";

    // 95. 授予学位日期
    String GRANTED_DEGREE_DATE = "syxwrq";

    // 96. 毕业证领取状态
    String DIPLOMA_STATUS = "byzlqzt";

    // 97. 毕业证编号
    String DIPLOMA_NO = "byzbh";

    // 98. 离校状态
    String LEAVE_STATUS = "lxzt";

    // 99. 离校去向
    String LEAVE_TO = "lxqx";

    // 100. 离校日期
    String LEAVE_DATE = "lxrq";

    // 101. 归档状态
    String ARCHIVE_STATUS = "gdzt";

    // 102. 归档日期
    String ARCHIVE_DATE = "gdrq";

    // 103. 考生来源码
    String EXAMINEE_ORIGIN = "fk_ksly";

    // 104. 获学士学位的单位代码
    String BACHELOR_UNIT = "fk_xsxwdw";

    // 105. 获学士学位的单位名称
    String BACHELOR_UNIT_NAME = "xsxwdw";

    // 106. 获学士学位专业代码
    String BACEHLOR_DISCIPLINE = "fk_xsxwzy";

    // 107. 获学士学位专业名称
    String BACHELOR_DISCIPLINE_NAME = "xsxwzy";

    // 108. 获学士学位日期
    String BACHELOR_DATE = "xsxwny";

    // 109. 学士学位证书编号
    String BACHELOR_NO = "xsxwzsbh";

    // 110. 本科毕业单位代码
    String UNDERGRADUATE_UNIT = "fk_bkbydw";

    // 111. 本科毕业单位名称
    String UNDERGRADUATE_UNIT_NAME = "bkbydw";

    // 112. 本科毕业专业代码
    String UNDERGRADUATE_DISCIPLINE = "fk_bkbyzy";

    // 113. 本科毕业专业名称
    String UNDERGRADUATE_DISCIPLINE_NAME = "bkbyzy";

    // 114. 本科毕业日期
    String UNDERGRADUATE_DATE = "bkbyzyny";

    // 115. 本科毕业证书编号
    String UNDERGRADUATE_NO = "bkbyzsbh";

    // 116. 取得本科学历的学习形式
    String UNDERGRADUATE_MODE = "fk_bkxxxs";

    // 117. 获硕士学位的单位代码
    String MASTER_UNIT = "fk_ssxwdw";

    // 118. 获硕士学位的单位名称
    String MASTER_UNIT_NAME = "ssxwdw";

    // 119. 获硕士学位专业代码
    String MASTER_DISCIPLINE = "fk_ssxwzy";

    // 120. 获硕士学位专业名称
    String MASTER_DISCIPLINE_NAME = "ssxwzy";

    // 121. 获硕士学位日期
    String MASTER_DATE = "ssxwny";

    // 122. 硕士学位证书编号
    String MASTER_NO = "ssxwzsbh";

    // 123. 获硕士学位方式
    String MASTER_MODE = "fk_ssxwfs";

    // 124. 硕士毕业单位代码
    String POSTGRADUATE_UNIT = "fk_ssbydw";

    // 125. 硕士毕业单位名称
    String POSTGRADUATE_UNIT_NAME = "ssbydw";

    // 126. 硕士毕业专业代码
    String POSTGRADUATE_DISCIPLINE = "fk_ssbyzy";

    // 127. 硕士毕业专业名称
    String POSTGRADUATE_DISCIPLINE_NAME = "ssbyzy";

    // 128. 硕士毕业日期
    String POSTGRADUATE_DATE = "ssbyny";

    // 129. 硕士毕业证书编号
    String POSTGRADUATE_NO = "ssbyzsbh";

    // 130. 取得硕士学历的学习形式
    String POSTGRADUATE_MODE = "fk_ssxxxs";

    // 131. 最后学位码
    String FINAL_DEGREE = "fk_zhxw";

    // 132. 最后学历码
    String FINAL_EDUCATION = "fk_zhxl";

    // 133. 在校生注册学号
    String FINAL_SNO = "zhzcxh";

    // 134. 获得最后学历毕业日期
    String FINAL_EDUCATION_DATE = "zhxlbyny";

    // 135. 取得最后学历的学习形式
    String FINAL_EDUCATION_MODE = "fk_zhxlxxxs";

    // 入学考试
    // 题目
    String TEST = "test";

    // 选项 A B C D
    String OPTION_A = "a";

    String OPTION_B = "b";

    String OPTION_C = "c";

    String OPTION_D = "d";

    // 答案
    String ANSWER = "answer";

    // 出題次數
    String TOTAL = "total";

    // 正確率 = 答對次數 / 出題次數
    String TOTAL_CORRECT = "total_correct";

    // 困难认定
    // 学年
    String YEAR = "xn";

    // 困难等级
    String LEVEL = "kndj";

    // 学籍外键
    String STUDENT = "fk_xj";

    // 报到注册缴费
    // 报到
    String CHECKIN = "bd";

    // 报到日期
    String CHECKIN_DATE = "bdrq";

    // 注冊
    String REGISTER = "zc";

    // 注册日期
    String REGISTER_DATE = "zcrq";

    // 缴费
    String FEE = "jf";

    // 缴费日期
    String FEE_DATE = "jfrq";

    //应缴学费
    String FEE_SHOULD_PAID = "yjxf";

    // 实缴学费
    String FEE_PAID = "sjxf";

    //欠费：应缴学费-实缴学费
    String FEE_OWN = "ls_qf";

    // ========================
    // 教职工
    // ========================

    String STAFF_SNO = "zgh";

    String STAFF_NAME = "name";

    // ========================
    // 异动
    // ========================

    // 审批模板的序号
    String SEQUENCE = "xh";

    String ROLE = "fk_js";

    // 异动类型外键
    String CHANGE_TYPE = "fk_yd_lx";

    // 申请理由
    String REASON = "ly";

    // 申请备注
    String COMMENT = "bz";

    // 申请时间
    String CREATED_AT = "cjrq";

    // 审批状态
    String AUDIT_STATUS = "spzt";

    // 审批日期
    String AUDIT_AT = "sprq";

    // 异动流程外键
    String AUDIT_WORKFLOW = "fk_yd_lc";

    // 异动申请外键
    String CHANGE_REQUEST = "fk_yd_sq";

    // 异动大类
    String MAJOR = "dl";

    String MAJOR_CODE = "dldm";

    // 异动小类
    String MINOR = "xl";

    String MINOR_CODE = "xldm";

    // 限制
    String RESTRICT = "ystj";

    // 备注
    String MEMO = "bz";

    // 学生是否可见
    String VISIBLE = "xssfkj";

    // 是否需要上传文件
    String ATTACHMENT = "scwj";

    // 异动后相关信息修改
    String MODIFICATION = "xxxg";

    // 学籍状态
    String STATUS = "xjzt";

    // 是否离校
    String QUIT = "sflx";

    // 是否归入终结生
    String TERMINATED = "sfzjs";

    // 邮件的回复内容（学生）
    String REPLY_STUDENT = "yjhfxs";

    // 邮件的回复内容（导师）
    String REPLY_SUPERVISOR = "yjhfds";

    // 是否学生可见
    String STATUS_CHANGE_VISIBLE = "kj";

    // 是否需要上传图片
    String STATUS_CHANGE_ISUPLOAD_FILE = "sctp";

    // 异动约束
    String STATUS_CHANGE_CONSTRAINT = "ydys";

    // 教职工外键
    String STAFF = "fk_jzg";

    // ========================
    // 权限
    // ========================

    // 帐号外键
    String ACCOUNT = "fk_zh";

    String ACCOUNT_STAFF = "fk_jzg";

    String ACCOUNT_STUDENT = "fk_xs";

    String ACCOUNT_USERNAME = "yhm";

    String ACCOUNT_PASSWD = "mm";

    String ACCOUNT_PRIVILEGE_UNIV = "xj";

    String ACCOUNT_PRIVILEGE_SCHOOL = "byj";

    String ACCOUNT_PRIVILEGE_DEPART = "yxj";

    String ACCOUNT_PRIVILEGE_SUPERVISOR = "dsj";

    String ACCOUNT_PRIVILEGE_STUDENT = "xsj";

    String ACCOUNT_PRIVILEGE_ROLE = "fk_js";

    String ACCOUNT_PRIVILEGE_ACCOUNT = "fk_zh";

    // ========================
    // 预毕业信息配置表
    // ========================

    String META_CLASSfIELD = "sxm";

    String META_FIELD = "sxmzw";

    String META_DISPLAY_FORMAT = "lx";

    String META_FRONTAPI = "qdhs";

    String META_BACK_GET_API = "hdhs1";

    String META_BACK_TO_STRING = "hdhs2";
    
    String META_BACK_SET_API = "hdhs3";

    String META_DATATYPE = "sjlx";

    String MODIFY_TYPE = "xgzt";

    String NOTES = "bz";

    String API_MAPPING = "fk_pz";

    String FIELDNAME = "lm";

    String FIELDDISPLAYNAME = "xsmc";

    String CURDISPLAYVALUE = "dqz";

    String NEWVALUE = "xgz";

    String NEWDISPLAYVALUE = "xgxsz";

    String CREATEDATE = "sqrq";

    String AUDITDATE = "sprq";

    String ISACCEPT = "spjg";

    // =========================
    // 通知表
    // =========================

    String MESSAGE_TITLE = "bt";

    String MESSAGE_BEGIN_TIME = "kssj";

    String MESSAGE_END_TIME = "jssj";

    String MESSAGE_CONTENT = "nr";

    String MESSAGE_LINK = "lj";

    String MESSAGE_TYPE = "zl";

    String MESSAGE_AUTHOR = "zz";

    // 学籍异动临时表
    String UPDATEJSON = "yd_xg";
    String DELFLG = "del_flg";
    
	//通知群表
    String MESSAGE_GROUP_NAME = "mc";

    String MESSAGE_GROUP_OWNER = "yyz";
    
    // 代申请人用户名
    String APPLICANT_USERNAME = "dsqryhm";
    
    // 代申请人姓名
    String APPLICANT_NAME = "dsqrxm";

    /**缴费临时表中的应缴学费总额字段*/
    String TEMPORARY_SHOULD_PAY = "s_p";

    /**缴费临时表中的实缴学费总额字段*/
    String TEMPORARY_PAY = "p";

    /**缴费临时表中的欠费总额字段*/
    String TEMPORARY_OWN_PAY = "o_p";
}
