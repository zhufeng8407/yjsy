package edu.ecnu.yjsy.model.student;

import static edu.ecnu.yjsy.model.constant.Column.*;
import static edu.ecnu.yjsy.model.constant.Table.STUDENT;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.ecnu.yjsy.model.EntityId;
import edu.ecnu.yjsy.model.change.StatusChangeRequest;
import edu.ecnu.yjsy.model.staff.Staff;

/**
 * 学籍表
 *
 * @author songshubin
 * @author xiafan
 * @author xulinhao
 * @see <a href=
 * "http://58.198.176.57/xulinhao/yjsy-wiki/blob/master/documentation/E-数据/xueji-datamodel.xls">学籍数据模型定义</a>
 */

@Entity
@Table(name = STUDENT, uniqueConstraints = @UniqueConstraint(columnNames = {
        "xh" }))
@NamedEntityGraphs({
        @NamedEntityGraph(name = "student.staff.unit", attributeNodes = {
                @NamedAttributeNode(value = "unit"),
                @NamedAttributeNode(value = "supervisor") }) })
public class Student extends EntityId {

    // ---------------------------------------------------
    // 基本信息
    // ---------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 5214253919907168973L;

    // 2. 姓名
    @Column(nullable = false, length = 40, name = NAME)
    private String name;

    // 3. 姓名 英文或拼音
    @Column(length = 80, name = NAME_EN)
    private String nameEn;

    // 4. 考生编号
    @Column(length = 15, name = EXAMINEE_NO)
    private String examineeNo;

    // 5. 报名点代码
    @Column(length = 4, name = EXAMINEE_SIGN)
    private String examineeSignPlace;

    // 6. 证件类型码 单向多对一
    @ManyToOne
    @JoinColumn(name = SSN_TYPE)
    private SsnType ssnType;

    // 7. 证件号码
    @Column(length = 20, name = SSN)
    private String ssn;

    // 8. 出生日期
    @Column(name = BIRTHDATE)
    private Date birthdate;

    // 9. 国籍码 单向多对一
    @ManyToOne
    @JoinColumn(name = NATION)
    private Nation nation;

    // 10. 民族码 单向多对一
    @ManyToOne
    @JoinColumn(name = ETHNIC)
    private Ethnic ethnic;

    // 11. 性別码 单向多对一
    @ManyToOne
    @JoinColumn(name = GENDER)
    private Gender gender;

    // 12. 婚姻码 单向多对一
    @ManyToOne
    @JoinColumn(name = MARRIAGE)
    private Marriage marriage;

    // 13. 现役军人码 单向多对一
    @ManyToOne
    @JoinColumn(name = MILITARY)
    private Military military;

    // 14. 入伍批准书编号
    @Column(length = 40, name = MILITARY_JOIN_NO)
    private String militaryJoinNo;

    // 15. 退出现役证编号
    @Column(length = 40, name = MILITARY_QUIT_NO)
    private String militaryQuitNo;

    // 16. 政治面貌码 = 宗教码 单向多对一
    @ManyToOne
    @JoinColumn(name = RELIGION)
    private Religion religion;

    // 17. 籍贯所在地码 单向多对一
    @ManyToOne
    @JoinColumn(name = HOMETOWN)
    private Region hometown;

    // 18. 出生地码 单向多对一
    @ManyToOne
    @JoinColumn(name = BIRTHPLACE)
    private Region birthplace;

    // 19. 户口所在地码 单向多对一
    @ManyToOne
    @JoinColumn(name = RESIDENCE)
    private Region residence;

    // 20. 户口所在地详细地址
    @Column(length = 100, name = RESIDENCE_ADDRESS)
    private String residenceAddress;

    // 21. 家庭所在地码 单向多对一
    @ManyToOne
    @JoinColumn(name = FAMILY)
    private Region family;

    // 22. 家庭所在地详细地址
    @Column(length = 100, name = FAMILY_ADDRESS)
    private String familyAddress;

    // 23. 火车到站码
    @ManyToOne
    @JoinColumn(name = RAILWAY)
    private Railway railway;

    // 24. 考生档案所在地码 单向多对一
    @ManyToOne
    @JoinColumn(name = EXAMINEE_ARCHIVE)
    private Region examineeArchive;

    // 25. 考生档案所在单位
    @Column(length = 60, name = EXAMINEE_ARCHIVE_UNIT)
    private String examineeArchiveUnit;

    // 26. 考生档案所在单位地址
    @Column(length = 100, name = EXAMINEE_ARCHIEVE_UNIT_ADDRESS)
    private String examineeArchiveUnitAddress;

    // 27. 考生档案所在单位邮政编码
    @Column(length = 6, name = EXAMINEE_ARCHIEVE_UNIT_ZIPCODE)
    private String examineeArchiveUnitZipcode;

    // 28. 入学前学习或工作单位性质
    @ManyToOne
    @JoinColumn(name = PRESTUDY_UNIT_NATURE)
    private PreStudy preStudyUnitNature;

    // 29. 入学前学习或工作单位所在地码
    @ManyToOne
    @JoinColumn(name = PRESTUDY)
    private Region prestudy;

    // 30. 入学前学习或工作单位
    @Column(length = 60, name = PRESTUDY_UNIT)
    private String prestudyUnit;

    // 31. 学习与工作经历
    @Column(length = 220, name = EXPERIENCE)
    private String experience;

    // 32. 家庭主要成员
    @Column(length = 220, name = FAMILY_MEMBER)
    private String familyMember;

    // 33. 紧急联系人
    @Column(length = 60, name = EMERGENCY_CONTACT)
    private String emergencyContact;

    // 34. 微信
    @Column(length = 20, name = WECHAT)
    private String weichat;

    // 35. 固定电话
    @Column(length = 12, name = PHONE)
    private String phone;

    // 36. 移动电话
    @Column(length = 11, name = MOBILE)
    private String mobile;

    // 37. 电子信箱
    @Column(length = 40, name = EMAIL)
    private String email;

    // ---------------------------------------------------
    // 学籍信息
    // ---------------------------------------------------

    // 38. 学号
    @Column(nullable = false, length = 20, name = SNO)
    private String sno;

    // 39. 照片
    @Column(length = 200, name = PHOTO)
    private String photo;

    // 40. 是否新生
    @Column(nullable = false, name = IS_NEW)
    private boolean isNew;

    // 41. 报到码
    // @Deprecated 移到报到注册流水表中
    // @Column(length = 20, name = CHECKIN_NO)
    // private String checkinNo;

    // 42. 是否报到
    // @Deprecated 需要查询报到缴费注册流水表
    // @Column(name = "sfbd", nullable = false)
    // private boolean isAdmission;

    // 43. 层次
    @ManyToOne
    @JoinColumn(name = DEGREE_LEVEL)
    private DegreeLevel degreeLevel;

    // 44. 学位类型
    @ManyToOne
    @JoinColumn(name = DEGREE_TYPE)
    private DegreeType degreeType;

    // 45. 是否統招
    @Column(name = IS_ADMISSION_UNIFIED)
    private boolean isAdmissionUnified;

    // 46. 学习形式
    @ManyToOne
    @JoinColumn(name = EDUCATION_MODE)
    private EducationMode educationMode;

    // 47. 入学年月
    @Column(name = ADMISSION_DATE)
    private Date admissionDate;

    // 48. 年级
    @Column(name = GRADE)
    private short grade;

    // 49. 学期
    @Column(name = TERM)
    private int term;

    // 50. 报到
    // @Column(nullable = false, name = IS_CHECKIN)
    // private boolean isCheckin;

    // 51. 注册
    // @Column(nullable = false, name = IS_REGISTER)
    // private boolean isRegister;

    // 52. 缴费
    // @Column(nullable = false, name = IS_FEE)
    // private boolean isFee;

    // 53. 报到注册缴费 流水表
    @OneToMany(mappedBy = "student")
    private Set<Registration> registrations = new HashSet<>();

    // 54. 当前学年应缴费
    @Column(length = 10, name = FEE_SHALL_PAY)
    private String feeShallPay;

    // 55. 学费总额
    // 根据缴费流水表计算得出，用于通知财务处进行核对
    @Column(length = 10, name = FEE_TOTAL)
    private String feeTotal;

    // 56. 缴费标准
    @Column(length = 10, name = FEE_STANDARD)
    private String feeStandard;

    // 57. 已缴费次数
    @Column(name = FEE_TIMES)
    private short feeTimes;

    // 58. 已缴费总额
    // 每学年基于财务处数据导入更新
    @Column(length = 10, name = FEE_TOTAL2)
    private String feeTotal2;

    // 59. 学费一
    @Column(length = 10, name = FEE1)
    private String fee1;

    // 60. 学费二
    @Column(length = 10, name = FEE2)
    private String fee2;

    // 61. 学费三
    @Column(length = 10, name = FEE3)
    private String fee3;

    // 62. 学费四
    @Column(length = 10, name = FEE4)
    private String fee4;

    // 63. 学费五
    @Column(length = 10, name = FEE5)
    private String fee5;

    // 64. 校区
    @ManyToOne
    @JoinColumn(name = CAMPUS)
    private Campus campus;

    // 65. 宿舍
    @Column(length = 60, name = DOMITORY)
    private String domitory;

    // 66. 导师
    @ManyToOne
    @JoinColumn(name = SUPERVISOR)
    private Staff supervisor;

    // 67. 专业 学位学科
    @ManyToOne
    @JoinColumn(nullable = false, name = DISCIPLINE)
    private Discipline discipline;

    // 68. 研究方向
    @Column(length = 30, name = RESEARCH)
    private String research;

    // 69. 院系
    @ManyToOne
    @JoinColumn(nullable = false, name = UNIT)
    private Unit unit;

    // 70. 学制
    @Column(nullable = false, length = 3, name = LOS)
    private String los; // length of schooling

    // 71. 专业排名
    @Column(length = 10, name = RANK)
    private String rank;

    // 72. 硕博连读硕士阶段的入学年级
    @Column(length = 4, name = MASTER_GRADE)
    private String masterGrade;

    // 73. 最近学籍变动情况
    @OneToMany(mappedBy = "student")
    private Set<StatusChangeRequest> requests = new HashSet<>();

    // 74. 奖惩
    @Column(length = 200, name = AWARD_AND_PUNISHMENT)
    private String awardAndPunishment;

    // 75. 医疗类型
    @ManyToOne
    @JoinColumn(name = MEDICAL)
    private Medical medical;

    // 76. 困难认定
    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private Set<Difficulty> difficulties = new HashSet<>();

    // 77. 考试方式码
    @ManyToOne
    @JoinColumn(name = EXAMINATION)
    private Examination examination;

    // 78. 专项计划码
    @ManyToOne
    @JoinColumn(name = SPECIAL_PLAN)
    private SpecialPlan specialPlan;

    // 79. 专项计划说明
    @Column(length = 60, name = SPECIAL_PLAN_MEMO)
    private String specialPlanMemo;

    // 80. 录取类别码
    @ManyToOne
    @JoinColumn(name = ENROLLMENT)
    private Enrollment enrollment;

    // 81. 定向就业单位所在地码
    @ManyToOne
    @JoinColumn(name = DIRECTED_REGION)
    private Region directedRegion;

    // 82. 定向就业单位
    @Column(length = 60, name = DIRECTED_WORK)
    private String directedWork;

    // 83. 联合培养单位代码
    @Column(length = 6, name = JOINT_TRAINING)
    private String jointTraining;

    // 84. 联合培养单位名称
    @Column(length = 60, name = JOINT_TRAINING_WORK)
    private String jointTrainingWork;

    // 85. 跨专业信息
    @Column(length = 18, name = CROSS_DISCIPLINE)
    private String crossDiscipline;

    // 86. 毕业类型
    @ManyToOne
    @JoinColumn(name = GRADUATION)
    private Graduation graduation;

    // 87. 预计毕业日期
    @Column(name = EXPECTED_GRADUATION_DATE)
    private Date expectedGraduationDate;

    // 88. 实际毕业日期
    @Column(name = ACTUAL_GRADUATION_DATE)
    private Date actualGraduationDate;

    // 89. 休学学期数
    @Column(length = 3, name = ABSENCE_TERMS)
    private String absenceTerms;

    // 90. 正常毕业年限
    @Column(length = 3, name = DURATION)
    private String duration;

    // 91. 最长延期 - 最长可以延长多少学年
    @Column(length = 3, name = MAX_DURATION)
    private String delay;

    // 92. 预毕业审核状态
    @Column(name = PREGRADUATION_STATUS)
    private PregraduationStatus pregraduationStatus;

    // 93. 预毕业审核日期
    @Column(name = PREGRADUATION_DATE)
    private Date pregraduationDate;

    // 94. 授予学位名称
    @Column(length = 60, name = GRANTED_DEGREE)
    private String grantedDegree;

    // 95. 授予学位日期
    @Column(name = GRANTED_DEGREE_DATE)
    private Date grantedDegreeDate;

    // 96. 毕业证领取状态
    @Column(name = DIPLOMA_STATUS)
    private boolean diplomaStatus;

    // 97. 毕业证编号
    @Column(length = 20, name = DIPLOMA_NO)
    private String diplomaNo;

    // 98. 离校状态
    @Column(name = LEAVE_STATUS)
    private boolean leave;

    // 99. 离校去向
    @Column(length = 60, name = LEAVE_TO)
    private String leaveTo;

    // 100. 离校日期
    @Column(name = LEAVE_DATE)
    private Date leaveDate;

    // 101. 归档状态
    @Column(name = ARCHIVE_STATUS)
    private boolean archiveSatus;

    // 102. 归档日期
    @Column(name = ARCHIVE_DATE)
    private Date archiveDate;

    // ---------------------------------------------------
    // 来源信息
    // ---------------------------------------------------

    // 103. 考生来源码 单向多对一
    @ManyToOne
    @JoinColumn(name = EXAMINEE_ORIGIN)
    private ExamineeOrigin examineeOrigin;

    // 104. 获学士学位的单位代码 单向多对一
    @ManyToOne
    @JoinColumn(name = BACHELOR_UNIT)
    private University bachelorUnit;

    // 105. 获学士学位的单位名称
    @Column(length = 60, name = BACHELOR_UNIT_NAME)
    private String bachelorUnitName;

    // 106. 获学士学位专业代码 单向多对一
    @ManyToOne
    @JoinColumn(name = BACEHLOR_DISCIPLINE)
    private BachelorDiscipline bachelorDiscipline;

    // 107. 获学士学位专业名称
    @Column(length = 60, name = BACHELOR_DISCIPLINE_NAME)
    private String bachelorDisciplineName;

    // 108. 获学士学位日期
    @Column(name = BACHELOR_DATE)
    private Date bachelorDate;

    // 109. 学士学位证书编号
    @Column(length = 20, name = BACHELOR_NO)
    private String bachelorNo;

    // 110. 本科毕业单位代码 单向多对一
    @ManyToOne
    @JoinColumn(name = UNDERGRADUATE_UNIT)
    private University undergraduateUnit;

    // 111. 本科毕业单位名称
    @Column(length = 60, name = UNDERGRADUATE_UNIT_NAME)
    private String undergraduateUnitName;

    // 112. 本科毕业专业代码 单向多对一
    @ManyToOne
    @JoinColumn(name = UNDERGRADUATE_DISCIPLINE)
    private BachelorDiscipline undergraduateDiscipline;

    // 113. 本科毕业专业名称
    @Column(length = 60, name = UNDERGRADUATE_DISCIPLINE_NAME)
    private String undergraduateDisciplineName;

    // 114. 本科毕业日期
    @Column(name = UNDERGRADUATE_DATE)
    private Date undergraduateDate;

    // 115. 本科毕业证书编号
    @Column(length = 20, name = UNDERGRADUATE_NO)
    private String undergraduateNo;

    // 116. 取得本科学历的学习形式 单向多对一
    @ManyToOne
    @JoinColumn(name = UNDERGRADUATE_MODE)
    private EducationMode undergraduateMode;

    // 117. 获硕士学位的单位代码 单向多对一
    @ManyToOne
    @JoinColumn(name = MASTER_UNIT)
    private University masterUnit;

    // 118. 获硕士学位的单位名称
    @Column(length = 60, name = MASTER_UNIT_NAME)
    private String masterUnitName;

    // 119. 获硕士学位专业代码 单向多对一
    @ManyToOne
    @JoinColumn(name = MASTER_DISCIPLINE)
    private Discipline masterDiscipline;

    // 120. 获硕士学位专业名称
    @Column(length = 60, name = MASTER_DISCIPLINE_NAME)
    private String masterDisciplineName;

    // 121. 获硕士学位日期
    @Column(name = MASTER_DATE)
    private Date masterDate;

    // 122. 硕士学位证书编号
    @Column(length = 20, name = MASTER_NO)
    private String masterNo;

    // 123. 获硕士学位方式 单向多对一
    @ManyToOne
    @JoinColumn(name = MASTER_MODE)
    private EducationMode masterMode;

    // 124. 硕士毕业单位代码 单向多对一
    @ManyToOne
    @JoinColumn(name = POSTGRADUATE_UNIT)
    private University postgraduateUnit;

    // 125. 硕士毕业单位名称
    @Column(length = 60, name = POSTGRADUATE_UNIT_NAME)
    private String postgraduateUnitName;

    // 126. 硕士毕业专业代码 单向多对一
    @ManyToOne
    @JoinColumn(name = POSTGRADUATE_DISCIPLINE)
    private Discipline postgraduateDiscipline;

    // 127. 硕士毕业专业名称
    @Column(length = 60, name = POSTGRADUATE_DISCIPLINE_NAME)
    private String postgraduateDisciplineName;

    // 128. 硕士毕业日期
    @Column(name = POSTGRADUATE_DATE)
    private Date postgraduateDate;

    // 129. 硕士毕业证书编号
    @Column(length = 20, name = POSTGRADUATE_NO)
    private String postgraduateNo;

    // 130. 取得硕士学历的学习形式 单向多对一
    @ManyToOne
    @JoinColumn(name = POSTGRADUATE_MODE)
    private EducationMode postgraduateMode;

    // 131. 最后学位码 单向多对一
    @ManyToOne
    @JoinColumn(name = FINAL_DEGREE)
    private Degree finalDegree;

    // 132. 最后学历码 单向多对一
    @ManyToOne
    @JoinColumn(name = FINAL_EDUCATION)
    private Education finalEducation;

    // 133. 在校生注册学号
    @Column(length = 20, name = FINAL_SNO)
    private String finalSno;

    // 134. 获得最后学历毕业日期
    @Column(name = FINAL_EDUCATION_DATE)
    private Date finalEducationDate;

    // 135. 取得最后学历的学习形式 单向多对一
    @ManyToOne
    @JoinColumn(name = FINAL_EDUCATION_MODE)
    private EducationMode finalEducationMode;

    // --------------------
    // CONSTRUCTORS
    // --------------------

    public Student() {
    }

    public Student(String name, String nameEn, String examineeNo,
            String examineeSignPlace, SsnType ssnType, String ssn,
            Date birthdate, Nation nation, Ethnic ethnic, Gender gender,
            Marriage marriage, Military military, String militaryJoinNo,
            String militaryQuitNo, Religion religion, Region hometown,
            Region birthplace, Region residence, String residenceAddress,
            Region family, String familyAddress, Railway railway,
            Region examineeArchive, String examineeArchiveUnit,
            String examineeArchiveUnitAddress,
            String examineeArchiveUnitZipcode, PreStudy preStudyUnitNature,
            Region prestudy, String prestudyUnit, String experience,
            String familyMember, String emergencyContact, String weichat,
            String phone, String mobile, String email, String sno, String photo,
            boolean isNew, DegreeLevel degreeLevel, DegreeType degreeType,
            boolean isAdmissionUnified, EducationMode educationMode,
            Date admissionDate, short grade, int term, String feeShallPay,
            String feeTotal, String feeStandard, short feeTimes,
            String feeTotal2, String fee1, String fee2, String fee3,
            String fee4, String fee5, Campus campus, String domitory,
            Staff supervisor, Discipline discipline, String research, Unit unit,
            String los, String rank, String masterGrade,
            String awardAndPunishment, Medical medical, Examination examination,
            SpecialPlan specialPlan, String specialPlanMemo,
            Enrollment enrollment, Region directedRegion, String directedWork,
            String jointTraining, String jointTrainingWork,
            String crossDiscipline, Graduation graduation,
            Date expectedGraduationDate, Date actualGraduationDate,
            String absenceTerms, String duration, String delay,
            PregraduationStatus pregraduationStatus, Date pregraduationDate,
            String grantedDegree, Date grantedDegreeDate, boolean diplomaStatus,
            String diplomaNo, boolean leave, String leaveTo, Date leaveDate,
            boolean archiveSatus, Date archiveDate,
            ExamineeOrigin examineeOrigin, University bachelorUnit,
            String bachelorUnitName, BachelorDiscipline bachelorDiscipline,
            String bachelorDisciplineName, Date bachelorDate, String bachelorNo,
            University undergraduateUnit, String undergraduateUnitName,
            BachelorDiscipline undergraduateDiscipline,
            String undergraduateDisciplineName, Date undergraduateDate,
            String undergraduateNo, EducationMode undergraduateMode,
            University masterUnit, String masterUnitName,
            Discipline masterDiscipline, String masterDisciplineName,
            Date masterDate, String masterNo, EducationMode masterMode,
            University postgraduateUnit, String postgraduateUnitName,
            Discipline postgraduateDiscipline,
            String postgraduateDisciplineName, Date postgraduateDate,
            String postgraduateNo, EducationMode postgraduateMode,
            Degree finalDegree, Education finalEducation, String finalSno,
            Date finalEducationDate, EducationMode finalEducationMode) {
        super();
        this.name = name;
        this.nameEn = nameEn;
        this.examineeNo = examineeNo;
        this.examineeSignPlace = examineeSignPlace;
        this.ssnType = ssnType;
        this.ssn = ssn;
        this.birthdate = birthdate;
        this.nation = nation;
        this.ethnic = ethnic;
        this.gender = gender;
        this.marriage = marriage;
        this.military = military;
        this.militaryJoinNo = militaryJoinNo;
        this.militaryQuitNo = militaryQuitNo;
        this.religion = religion;
        this.hometown = hometown;
        this.birthplace = birthplace;
        this.residence = residence;
        this.residenceAddress = residenceAddress;
        this.family = family;
        this.familyAddress = familyAddress;
        this.railway = railway;
        this.examineeArchive = examineeArchive;
        this.examineeArchiveUnit = examineeArchiveUnit;
        this.examineeArchiveUnitAddress = examineeArchiveUnitAddress;
        this.examineeArchiveUnitZipcode = examineeArchiveUnitZipcode;
        this.preStudyUnitNature = preStudyUnitNature;
        this.prestudy = prestudy;
        this.prestudyUnit = prestudyUnit;
        this.experience = experience;
        this.familyMember = familyMember;
        this.emergencyContact = emergencyContact;
        this.weichat = weichat;
        this.phone = phone;
        this.mobile = mobile;
        this.email = email;
        this.sno = sno;
        this.photo = photo;
        this.isNew = isNew;
        this.degreeLevel = degreeLevel;
        this.degreeType = degreeType;
        this.isAdmissionUnified = isAdmissionUnified;
        this.educationMode = educationMode;
        this.admissionDate = admissionDate;
        this.grade = grade;
        this.term = term;
        this.feeShallPay = feeShallPay;
        this.feeTotal = feeTotal;
        this.feeStandard = feeStandard;
        this.feeTimes = feeTimes;
        this.feeTotal2 = feeTotal2;
        this.fee1 = fee1;
        this.fee2 = fee2;
        this.fee3 = fee3;
        this.fee4 = fee4;
        this.fee5 = fee5;
        this.campus = campus;
        this.domitory = domitory;
        this.supervisor = supervisor;
        this.discipline = discipline;
        this.research = research;
        this.unit = unit;
        this.los = los;
        this.rank = rank;
        this.masterGrade = masterGrade;
        this.awardAndPunishment = awardAndPunishment;
        this.medical = medical;
        this.examination = examination;
        this.specialPlan = specialPlan;
        this.specialPlanMemo = specialPlanMemo;
        this.enrollment = enrollment;
        this.directedRegion = directedRegion;
        this.directedWork = directedWork;
        this.jointTraining = jointTraining;
        this.jointTrainingWork = jointTrainingWork;
        this.crossDiscipline = crossDiscipline;
        this.graduation = graduation;
        this.expectedGraduationDate = expectedGraduationDate;
        this.actualGraduationDate = actualGraduationDate;
        this.absenceTerms = absenceTerms;
        this.duration = duration;
        this.delay = delay;
        this.pregraduationStatus = pregraduationStatus;
        this.pregraduationDate = pregraduationDate;
        this.grantedDegree = grantedDegree;
        this.grantedDegreeDate = grantedDegreeDate;
        this.diplomaStatus = diplomaStatus;
        this.diplomaNo = diplomaNo;
        this.leave = leave;
        this.leaveTo = leaveTo;
        this.leaveDate = leaveDate;
        this.archiveSatus = archiveSatus;
        this.archiveDate = archiveDate;
        this.examineeOrigin = examineeOrigin;
        this.bachelorUnit = bachelorUnit;
        this.bachelorUnitName = bachelorUnitName;
        this.bachelorDiscipline = bachelorDiscipline;
        this.bachelorDisciplineName = bachelorDisciplineName;
        this.bachelorDate = bachelorDate;
        this.bachelorNo = bachelorNo;
        this.undergraduateUnit = undergraduateUnit;
        this.undergraduateUnitName = undergraduateUnitName;
        this.undergraduateDiscipline = undergraduateDiscipline;
        this.undergraduateDisciplineName = undergraduateDisciplineName;
        this.undergraduateDate = undergraduateDate;
        this.undergraduateNo = undergraduateNo;
        this.undergraduateMode = undergraduateMode;
        this.masterUnit = masterUnit;
        this.masterUnitName = masterUnitName;
        this.masterDiscipline = masterDiscipline;
        this.masterDisciplineName = masterDisciplineName;
        this.masterDate = masterDate;
        this.masterNo = masterNo;
        this.masterMode = masterMode;
        this.postgraduateUnit = postgraduateUnit;
        this.postgraduateUnitName = postgraduateUnitName;
        this.postgraduateDiscipline = postgraduateDiscipline;
        this.postgraduateDisciplineName = postgraduateDisciplineName;
        this.postgraduateDate = postgraduateDate;
        this.postgraduateNo = postgraduateNo;
        this.postgraduateMode = postgraduateMode;
        this.finalDegree = finalDegree;
        this.finalEducation = finalEducation;
        this.finalSno = finalSno;
        this.finalEducationDate = finalEducationDate;
        this.finalEducationMode = finalEducationMode;
    }

    // --------------------
    // METHODS
    // --------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getExamineeNo() {
        return examineeNo;
    }

    public void setExamineeNo(String examineeNo) {
        this.examineeNo = examineeNo;
    }

    public String getExamineeSignPlace() {
        return examineeSignPlace;
    }

    public void setExamineeSignPlace(String examineeSignPlace) {
        this.examineeSignPlace = examineeSignPlace;
    }

    public SsnType getSsnType() {
        return ssnType;
    }

    public void setSsnType(SsnType ssnType) {
        this.ssnType = ssnType;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Nation getNation() {
        return nation;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }

    public Ethnic getEthnic() {
        return ethnic;
    }

    public void setEthnic(Ethnic ethnic) {
        this.ethnic = ethnic;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Marriage getMarriage() {
        return marriage;
    }

    public void setMarriage(Marriage marriage) {
        this.marriage = marriage;
    }

    public Military getMilitary() {
        return military;
    }

    public void setMilitary(Military military) {
        this.military = military;
    }

    public String getMilitaryJoinNo() {
        return militaryJoinNo;
    }

    public void setMilitaryJoinNo(String militaryJoinNo) {
        this.militaryJoinNo = militaryJoinNo;
    }

    public String getMilitaryQuitNo() {
        return militaryQuitNo;
    }

    public void setMilitaryQuitNo(String militaryQuitNo) {
        this.militaryQuitNo = militaryQuitNo;
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public Region getHometown() {
        return hometown;
    }

    public void setHometown(Region hometown) {
        this.hometown = hometown;
    }

    public Region getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(Region birthplace) {
        this.birthplace = birthplace;
    }

    public Region getResidence() {
        return residence;
    }

    public void setResidence(Region residence) {
        this.residence = residence;
    }

    public String getResidenceAddress() {
        return residenceAddress;
    }

    public void setResidenceAddress(String residenceAddress) {
        this.residenceAddress = residenceAddress;
    }

    public Region getFamily() {
        return family;
    }

    public PregraduationStatus getPregraduationStatus() {
        return pregraduationStatus;
    }

    public void setPregraduationStatus(
            PregraduationStatus pregraduationStatus) {
        this.pregraduationStatus = pregraduationStatus;
    }

    public void setFamily(Region family) {
        this.family = family;
    }

    public String getFamilyAddress() {
        return familyAddress;
    }

    public void setFamilyAddress(String familyAddress) {
        this.familyAddress = familyAddress;
    }

    public Railway getRailway() {
        return railway;
    }

    public void setRailway(Railway railway) {
        this.railway = railway;
    }

    public Region getExamineeArchive() {
        return examineeArchive;
    }

    public void setExamineeArchive(Region examineeArchive) {
        this.examineeArchive = examineeArchive;
    }

    public String getExamineeArchiveUnit() {
        return examineeArchiveUnit;
    }

    public void setExamineeArchiveUnit(String examineeArchiveUnit) {
        this.examineeArchiveUnit = examineeArchiveUnit;
    }

    public String getExamineeArchiveUnitAddress() {
        return examineeArchiveUnitAddress;
    }

    public void setExamineeArchiveUnitAddress(
            String examineeArchiveUnitAddress) {
        this.examineeArchiveUnitAddress = examineeArchiveUnitAddress;
    }

    public String getExamineeArchiveUnitZipcode() {
        return examineeArchiveUnitZipcode;
    }

    public void setExamineeArchiveUnitZipcode(
            String examineeArchiveUnitZipcode) {
        this.examineeArchiveUnitZipcode = examineeArchiveUnitZipcode;
    }

    public PreStudy getPreStudyUnitNature() {
        return preStudyUnitNature;
    }

    public void setPreStudyUnitNature(PreStudy preStudyUnitNature) {
        this.preStudyUnitNature = preStudyUnitNature;
    }

    public Region getPrestudy() {
        return prestudy;
    }

    public void setPrestudy(Region prestudy) {
        this.prestudy = prestudy;
    }

    public String getPrestudyUnit() {
        return prestudyUnit;
    }

    public void setPrestudyUnit(String prestudyUnit) {
        this.prestudyUnit = prestudyUnit;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getFamilyMember() {
        return familyMember;
    }

    public void setFamilyMember(String familyMember) {
        this.familyMember = familyMember;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getWeichat() {
        return weichat;
    }

    public void setWeichat(String weichat) {
        this.weichat = weichat;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public DegreeLevel getDegreeLevel() {
        return degreeLevel;
    }

    public void setDegreeLevel(DegreeLevel degreeLevel) {
        this.degreeLevel = degreeLevel;
    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    public boolean isAdmissionUnified() {
        return isAdmissionUnified;
    }

    public void setAdmissionUnified(boolean isAdmissionUnified) {
        this.isAdmissionUnified = isAdmissionUnified;
    }

    public EducationMode getEducationMode() {
        return educationMode;
    }

    public void setEducationMode(EducationMode educationMode) {
        this.educationMode = educationMode;
    }

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public short getGrade() {
        return grade;
    }

    public void setGrade(short grade) {
        this.grade = grade;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public Set<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(Set<Registration> registrations) {
        this.registrations = registrations;
    }

    public String getFeeShallPay() {
        return feeShallPay;
    }

    public void setFeeShallPay(String feeShallPay) {
        this.feeShallPay = feeShallPay;
    }

    public String getFeeTotal() {
        return feeTotal;
    }

    public void setFeeTotal(String feeTotal) {
        this.feeTotal = feeTotal;
    }

    public String getFeeStandard() {
        return feeStandard;
    }

    public void setFeeStandard(String feeStandard) {
        this.feeStandard = feeStandard;
    }

    public short getFeeTimes() {
        return feeTimes;
    }

    public void setFeeTimes(short feeTimes) {
        this.feeTimes = feeTimes;
    }

    public String getFeeTotal2() {
        return feeTotal2;
    }

    public void setFeeTotal2(String feeTotal2) {
        this.feeTotal2 = feeTotal2;
    }

    public String getFee1() {
        return fee1;
    }

    public void setFee1(String fee1) {
        this.fee1 = fee1;
    }

    public String getFee2() {
        return fee2;
    }

    public void setFee2(String fee2) {
        this.fee2 = fee2;
    }

    public String getFee3() {
        return fee3;
    }

    public void setFee3(String fee3) {
        this.fee3 = fee3;
    }

    public String getFee4() {
        return fee4;
    }

    public void setFee4(String fee4) {
        this.fee4 = fee4;
    }

    public String getFee5() {
        return fee5;
    }

    public void setFee5(String fee5) {
        this.fee5 = fee5;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public String getDomitory() {
        return domitory;
    }

    public void setDomitory(String domitory) {
        this.domitory = domitory;
    }

    public Staff getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Staff supervisor) {
        this.supervisor = supervisor;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public String getResearch() {
        return research;
    }

    public void setResearch(String research) {
        this.research = research;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getLos() {
        return los;
    }

    public void setLos(String los) {
        this.los = los;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getMasterGrade() {
        return masterGrade;
    }

    public void setMasterGrade(String masterGrade) {
        this.masterGrade = masterGrade;
    }

    public Set<StatusChangeRequest> getRequests() {
        return requests;
    }

    public void setRequests(Set<StatusChangeRequest> requests) {
        this.requests = requests;
    }

    public String getAwardAndPunishment() {
        return awardAndPunishment;
    }

    public void setAwardAndPunishment(String awardAndPunishment) {
        this.awardAndPunishment = awardAndPunishment;
    }

    public Medical getMedical() {
        return medical;
    }

    public void setMedical(Medical medical) {
        this.medical = medical;
    }

    public Set<Difficulty> getDifficulties() {
        return difficulties;
    }

    public void setDifficulties(Set<Difficulty> difficulties) {
        this.difficulties = difficulties;
    }

    public Examination getExamination() {
        return examination;
    }

    public void setExamination(Examination examination) {
        this.examination = examination;
    }

    public SpecialPlan getSpecialPlan() {
        return specialPlan;
    }

    public void setSpecialPlan(SpecialPlan specialPlan) {
        this.specialPlan = specialPlan;
    }

    public String getSpecialPlanMemo() {
        return specialPlanMemo;
    }

    public void setSpecialPlanMemo(String specialPlanMemo) {
        this.specialPlanMemo = specialPlanMemo;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public Region getDirectedRegion() {
        return directedRegion;
    }

    public void setDirectedRegion(Region directedRegion) {
        this.directedRegion = directedRegion;
    }

    public String getDirectedWork() {
        return directedWork;
    }

    public void setDirectedWork(String directedWork) {
        this.directedWork = directedWork;
    }

    public String getJointTraining() {
        return jointTraining;
    }

    public void setJointTraining(String jointTraining) {
        this.jointTraining = jointTraining;
    }

    public String getJointTrainingWork() {
        return jointTrainingWork;
    }

    public void setJointTrainingWork(String jointTrainingWork) {
        this.jointTrainingWork = jointTrainingWork;
    }

    public String getCrossDiscipline() {
        return crossDiscipline;
    }

    public void setCrossDiscipline(String crossDiscipline) {
        this.crossDiscipline = crossDiscipline;
    }

    public Graduation getGraduation() {
        return graduation;
    }

    public void setGraduation(Graduation graduation) {
        this.graduation = graduation;
    }

    public Date getExpectedGraduationDate() {
        return expectedGraduationDate;
    }

    public void setExpectedGraduationDate(Date expectedGraduationDate) {
        this.expectedGraduationDate = expectedGraduationDate;
    }

    public Date getActualGraduationDate() {
        return actualGraduationDate;
    }

    public void setActualGraduationDate(Date actualGraduationDate) {
        this.actualGraduationDate = actualGraduationDate;
    }

    public String getAbsenceTerms() {
        return absenceTerms;
    }

    public void setAbsenceTerms(String absenceTerms) {
        this.absenceTerms = absenceTerms;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public Date getPregraduationDate() {
        return pregraduationDate;
    }

    public void setPregraduationDate(Date pregraduationDate) {
        this.pregraduationDate = pregraduationDate;
    }

    public String getGrantedDegree() {
        return grantedDegree;
    }

    public void setGrantedDegree(String grantedDegree) {
        this.grantedDegree = grantedDegree;
    }

    public Date getGrantedDegreeDate() {
        return grantedDegreeDate;
    }

    public void setGrantedDegreeDate(Date grantedDegreeDate) {
        this.grantedDegreeDate = grantedDegreeDate;
    }

    public boolean isDiplomaStatus() {
        return diplomaStatus;
    }

    public void setDiplomaStatus(boolean diplomaStatus) {
        this.diplomaStatus = diplomaStatus;
    }

    public String getDiplomaNo() {
        return diplomaNo;
    }

    public void setDiplomaNo(String diplomaNo) {
        this.diplomaNo = diplomaNo;
    }

    public boolean isLeave() {
        return leave;
    }

    public void setLeave(boolean leave) {
        this.leave = leave;
    }

    public String getLeaveTo() {
        return leaveTo;
    }

    public void setLeaveTo(String leaveTo) {
        this.leaveTo = leaveTo;
    }

    public Date getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    public boolean isArchiveSatus() {
        return archiveSatus;
    }

    public void setArchiveSatus(boolean archiveSatus) {
        this.archiveSatus = archiveSatus;
    }

    public Date getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(Date archiveDate) {
        this.archiveDate = archiveDate;
    }

    public ExamineeOrigin getExamineeOrigin() {
        return examineeOrigin;
    }

    public void setExamineeOrigin(ExamineeOrigin examineeOrigin) {
        this.examineeOrigin = examineeOrigin;
    }

    public University getBachelorUnit() {
        return bachelorUnit;
    }

    public void setBachelorUnit(University bachelorUnit) {
        this.bachelorUnit = bachelorUnit;
    }

    public String getBachelorUnitName() {
        return bachelorUnitName;
    }

    public void setBachelorUnitName(String bachelorUnitName) {
        this.bachelorUnitName = bachelorUnitName;
    }

    public BachelorDiscipline getBachelorDiscipline() {
        return bachelorDiscipline;
    }

    public void setBachelorDiscipline(BachelorDiscipline bachelorDiscipline) {
        this.bachelorDiscipline = bachelorDiscipline;
    }

    public String getBachelorDisciplineName() {
        return bachelorDisciplineName;
    }

    public void setBachelorDisciplineName(String bachelorDisciplineName) {
        this.bachelorDisciplineName = bachelorDisciplineName;
    }

    public Date getBachelorDate() {
        return bachelorDate;
    }

    public void setBachelorDate(Date bachelorDate) {
        this.bachelorDate = bachelorDate;
    }

    public String getBachelorNo() {
        return bachelorNo;
    }

    public void setBachelorNo(String bachelorNo) {
        this.bachelorNo = bachelorNo;
    }

    public University getUndergraduateUnit() {
        return undergraduateUnit;
    }

    public void setUndergraduateUnit(University undergraduateUnit) {
        this.undergraduateUnit = undergraduateUnit;
    }

    public String getUndergraduateUnitName() {
        return undergraduateUnitName;
    }

    public void setUndergraduateUnitName(String undergraduateUnitName) {
        this.undergraduateUnitName = undergraduateUnitName;
    }

    public BachelorDiscipline getUndergraduateDiscipline() {
        return undergraduateDiscipline;
    }

    public void setUndergraduateDiscipline(
            BachelorDiscipline undergraduateDiscipline) {
        this.undergraduateDiscipline = undergraduateDiscipline;
    }

    public String getUndergraduateDisciplineName() {
        return undergraduateDisciplineName;
    }

    public void setUndergraduateDisciplineName(
            String undergraduateDisciplineName) {
        this.undergraduateDisciplineName = undergraduateDisciplineName;
    }

    public Date getUndergraduateDate() {
        return undergraduateDate;
    }

    public void setUndergraduateDate(Date undergraduateDate) {
        this.undergraduateDate = undergraduateDate;
    }

    public String getUndergraduateNo() {
        return undergraduateNo;
    }

    public void setUndergraduateNo(String undergraduateNo) {
        this.undergraduateNo = undergraduateNo;
    }

    public EducationMode getUndergraduateMode() {
        return undergraduateMode;
    }

    public void setUndergraduateMode(EducationMode undergraduateMode) {
        this.undergraduateMode = undergraduateMode;
    }

    public University getMasterUnit() {
        return masterUnit;
    }

    public void setMasterUnit(University masterUnit) {
        this.masterUnit = masterUnit;
    }

    public String getMasterUnitName() {
        return masterUnitName;
    }

    public void setMasterUnitName(String masterUnitName) {
        this.masterUnitName = masterUnitName;
    }

    public Discipline getMasterDiscipline() {
        return masterDiscipline;
    }

    public void setMasterDiscipline(Discipline masterDiscipline) {
        this.masterDiscipline = masterDiscipline;
    }

    public String getMasterDisciplineName() {
        return masterDisciplineName;
    }

    public void setMasterDisciplineName(String masterDisciplineName) {
        this.masterDisciplineName = masterDisciplineName;
    }

    public Date getMasterDate() {
        return masterDate;
    }

    public void setMasterDate(Date masterDate) {
        this.masterDate = masterDate;
    }

    public String getMasterNo() {
        return masterNo;
    }

    public void setMasterNo(String masterNo) {
        this.masterNo = masterNo;
    }

    public EducationMode getMasterMode() {
        return masterMode;
    }

    public void setMasterMode(EducationMode masterMode) {
        this.masterMode = masterMode;
    }

    public University getPostgraduateUnit() {
        return postgraduateUnit;
    }

    public void setPostgraduateUnit(University postgraduateUnit) {
        this.postgraduateUnit = postgraduateUnit;
    }

    public String getPostgraduateUnitName() {
        return postgraduateUnitName;
    }

    public void setPostgraduateUnitName(String postgraduateUnitName) {
        this.postgraduateUnitName = postgraduateUnitName;
    }

    public Discipline getPostgraduateDiscipline() {
        return postgraduateDiscipline;
    }

    public void setPostgraduateDiscipline(Discipline postgraduateDiscipline) {
        this.postgraduateDiscipline = postgraduateDiscipline;
    }

    public String getPostgraduateDisciplineName() {
        return postgraduateDisciplineName;
    }

    public void setPostgraduateDisciplineName(
            String postgraduateDisciplineName) {
        this.postgraduateDisciplineName = postgraduateDisciplineName;
    }

    public Date getPostgraduateDate() {
        return postgraduateDate;
    }

    public void setPostgraduateDate(Date postgraduateDate) {
        this.postgraduateDate = postgraduateDate;
    }

    public String getPostgraduateNo() {
        return postgraduateNo;
    }

    public void setPostgraduateNo(String postgraduateNo) {
        this.postgraduateNo = postgraduateNo;
    }

    public EducationMode getPostgraduateMode() {
        return postgraduateMode;
    }

    public void setPostgraduateMode(EducationMode postgraduateMode) {
        this.postgraduateMode = postgraduateMode;
    }

    public Degree getFinalDegree() {
        return finalDegree;
    }

    public void setFinalDegree(Degree finalDegree) {
        this.finalDegree = finalDegree;
    }

    public Education getFinalEducation() {
        return finalEducation;
    }

    public void setFinalEducation(Education finalEducation) {
        this.finalEducation = finalEducation;
    }

    public String getFinalSno() {
        return finalSno;
    }

    public void setFinalSno(String finalSno) {
        this.finalSno = finalSno;
    }

    public Date getFinalEducationDate() {
        return finalEducationDate;
    }

    public void setFinalEducationDate(Date finalEducationDate) {
        this.finalEducationDate = finalEducationDate;
    }

    public EducationMode getFinalEducationMode() {
        return finalEducationMode;
    }

    public void setFinalEducationMode(EducationMode finalEducationMode) {
        this.finalEducationMode = finalEducationMode;
    }

}