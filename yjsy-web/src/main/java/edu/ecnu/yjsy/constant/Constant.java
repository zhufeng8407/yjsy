package edu.ecnu.yjsy.constant;

public class Constant {

	public static final String API = "/api";

	public static final String API_METADATA = API + "/metadata";

	public static final String API_STAFF = API + "/staff";

	public static final String API_XJ = API + "/xj";

	public static final String API_YD = API + "/yd";

	public static final String API_YBY = API + "/yby";

	public static final String API_AUTH = API + "/auth";

	public static final String API_TZ = API + "/tz";

	// -------------------

	public static final String PAGE_PAGE = "0";

	public static final String PAGE_SIZE = "15";

	public static final int PAGE_PAGE_INT = Integer.parseInt(PAGE_PAGE);

	public static final int PAGE_SIZE_INT = Integer.parseInt(PAGE_SIZE);

	// -------------------

	/**
	 * The number of exams to be produced in an exam paper.
	 */
	public static final String EXAM_COUNT = "30";

	public static final String ERROR_CODE_FILE_INVALID = "1001";
	public static final String ERROR_CODE_DATA_PARSE = "1002";
	public static final String ERROR_CODE_DATA_STORE = "1003";
	public static final String ERROR_CODE_DB_CONN_TIMEOUT = "1004";
	public static final String ERROR_DES_FILE_INVALID = "文件无效";
	public static final String ERROR_DES_DATA_PARSE = "数据解析错误";
	public static final String ERROR_DES_DATA_STORE = "数据存储失败";
	public static final String ERROR_DES_DB_CONN_TIMEOUT = "数据库连接超时";

	/** 延期大分类code */
	public static final short STATUS_CHANGE_TYPE_MAJOR_CODE_1 = 1;

	/** 休学大分类code */
	public static final short STATUS_CHANGE_TYPE_MAJOR_CODE_2 = 2;

	/** 复学大分类code */
	public static final short STATUS_CHANGE_TYPE_MAJOR_CODE_3 = 3;

	/** 退学大分类code */
	public static final short STATUS_CHANGE_TYPE_MAJOR_CODE_4 = 4;

	/** 提前毕业大分类code */
	public static final short STATUS_CHANGE_TYPE_MAJOR_CODE_5 = 5;

	/** 放弃入学大分类code */
	public static final short STATUS_CHANGE_TYPE_MAJOR_CODE_6 = 6;

	/** 取消入学资格大分类code */
	public static final short STATUS_CHANGE_TYPE_MAJOR_CODE_7 = 7;

	/** 转导师大分类code */
	public static final short STATUS_CHANGE_TYPE_MAJOR_CODE_8 = 8;

	/** 结业大分类code */
	public static final short STATUS_CHANGE_TYPE_MAJOR_CODE_9 = 9;

	/** 结业后答辩大分类code */
	public static final short STATUS_CHANGE_TYPE_MAJOR_CODE_10 = 10;

	/** 肄业大分类code */
	public static final short STATUS_CHANGE_TYPE_MAJOR_CODE_11 = 11;

	/** 硕博连读转硕大分类code */
	public static final short STATUS_CHANGE_TYPE_MAJOR_CODE_13 = 13;
	
	/** 转院系大分类code */
	public static final short STATUS_CHANGE_TYPE_MAJOR_CODE_14 = 14;
	
	/** 转院系大分类code */
	public static final short STATUS_CHANGE_TYPE_MAJOR_CODE_15 = 15;
	
	/** 请长假大分类code */
	public static final short STATUS_CHANGE_TYPE_MAJOR_CODE_16 = 16;
	
	/** 推迟入学大分类code */
	public static final short STATUS_CHANGE_TYPE_MAJOR_CODE_17 = 17;
	
	/** 异动小分类code为1 */
	public static final short STATUS_CHANGE_TYPE_MINOR_CODE_1 = 1;
	
	/** 异动小分类code为2 */
	public static final short STATUS_CHANGE_TYPE_MINOR_CODE_2 = 2;
	
	/** 毕业类型是结业场合code */
	public static final String GRADUATION_CODE_2 = "2";
	
	/** 毕业类型是肄业场合code */
	public static final String GRADUATION_CODE_3 = "3";
	
	/** 毕业类型是放弃入学资格场合code */
	public static final String GRADUATION_CODE_4 = "4";
	
	/** 毕业类型是取消入学资格场合code */
	public static final String GRADUATION_CODE_5 = "5";
	
	/** 毕业类型是自动退学场合code */
	public static final String GRADUATION_CODE_6 = "6";
	
	/** 毕业类型是勒令退学场合code */
	public static final String GRADUATION_CODE_7 = "7";
	
	/** 审核记录ID */
	public static final String AUDIT_ID = "auditId";
	
	/** 申请记录ID */
        public static final String REQUEST_ID = "requestId";
	
	/** 学期数为1的定数 */
	public static final String XQS_1 = "1";
	
	/** 学期数为2的定数 */
	public static final String XQS_2 = "2";
	
	/** 学期数为3的定数 */
	public static final String XQS_3 = "3";

	/** 异动审批状态 --等待审批 */
	public static final short AUDIT_STATUS_1 = 1;

	/** 异动审批状态 --审批中 */
	public static final short AUDIT_STATUS_2 = 2;

	/** 删除flag --默认状态 */
	public static final String DEL_FLG_0 = "0";

	/** 删除flag --物理删除状态 (只有在提出申请以后改变流程才会触发)*/
	public static final String DEL_FLG_1 = "1";
	
	/** 学籍异动 --更新条件导师key */
	public static final String STAFF = "staff";

	/** 学籍异动 --更新条件硕博连读专硕选择日期key */
	public static final String TRANSMIDATE = "transmitdate";
	
	/** 学籍异动 --更新条件转专业专业key */
	public static final String DISCIPLINE = "discipline";
	
	/** 学籍异动 --更新条件转院系专业key */
	public static final String UNIT = "unit";
	
	/** 学籍异动 --更新条件请长假开始日期key */
	public static final String LEAVEDATEFROM = "leaveDateFrom";
	
	/** 学籍异动 --更新条件请长假结束日期key */
	public static final String LEAVEDATETO = "leaveDateTo";
	
	/** 常量空字符串 */
	public static final String EMPTY_STRING = "";
	
	/** 流程序号:1 */
        public static final short SEQUENCE_1 = 1;
}
