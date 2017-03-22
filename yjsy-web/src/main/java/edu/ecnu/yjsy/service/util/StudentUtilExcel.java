package edu.ecnu.yjsy.service.util;

import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ecnu.yjsy.model.staff.Staff;
import edu.ecnu.yjsy.model.staff.StaffRepository;
import edu.ecnu.yjsy.model.student.BachelorDiscipline;
import edu.ecnu.yjsy.model.student.BachelorDisciplineRepository;
import edu.ecnu.yjsy.model.student.Campus;
import edu.ecnu.yjsy.model.student.CampusRepository;
import edu.ecnu.yjsy.model.student.Degree;
import edu.ecnu.yjsy.model.student.DegreeLevel;
import edu.ecnu.yjsy.model.student.DegreeLevelRepository;
import edu.ecnu.yjsy.model.student.DegreeRepository;
import edu.ecnu.yjsy.model.student.DegreeType;
import edu.ecnu.yjsy.model.student.DegreeTypeRepository;
import edu.ecnu.yjsy.model.student.Discipline;
import edu.ecnu.yjsy.model.student.DisciplineRepository;
import edu.ecnu.yjsy.model.student.Education;
import edu.ecnu.yjsy.model.student.EducationMode;
import edu.ecnu.yjsy.model.student.EducationModeRepository;
import edu.ecnu.yjsy.model.student.EducationRepository;
import edu.ecnu.yjsy.model.student.Enrollment;
import edu.ecnu.yjsy.model.student.EnrollmentRepository;
import edu.ecnu.yjsy.model.student.Ethnic;
import edu.ecnu.yjsy.model.student.EthnicRepository;
import edu.ecnu.yjsy.model.student.Examination;
import edu.ecnu.yjsy.model.student.ExaminationRepository;
import edu.ecnu.yjsy.model.student.ExamineeOrigin;
import edu.ecnu.yjsy.model.student.ExamineeOriginRepository;
import edu.ecnu.yjsy.model.student.Gender;
import edu.ecnu.yjsy.model.student.GenderRepository;
import edu.ecnu.yjsy.model.student.Graduation;
import edu.ecnu.yjsy.model.student.GraduationRepository;
import edu.ecnu.yjsy.model.student.Marriage;
import edu.ecnu.yjsy.model.student.MarriageRepository;
import edu.ecnu.yjsy.model.student.Medical;
import edu.ecnu.yjsy.model.student.MedicalRepository;
import edu.ecnu.yjsy.model.student.Military;
import edu.ecnu.yjsy.model.student.MilitaryRepository;
import edu.ecnu.yjsy.model.student.Nation;
import edu.ecnu.yjsy.model.student.NationRepository;
import edu.ecnu.yjsy.model.student.PreStudy;
import edu.ecnu.yjsy.model.student.PreStudyRepository;
import edu.ecnu.yjsy.model.student.PregraduationStatus;
import edu.ecnu.yjsy.model.student.Railway;
import edu.ecnu.yjsy.model.student.RailwayRepository;
import edu.ecnu.yjsy.model.student.Region;
import edu.ecnu.yjsy.model.student.RegionRepository;
import edu.ecnu.yjsy.model.student.Religion;
import edu.ecnu.yjsy.model.student.ReligionRepository;
import edu.ecnu.yjsy.model.student.SpecialPlan;
import edu.ecnu.yjsy.model.student.SpecialPlanRepository;
import edu.ecnu.yjsy.model.student.SsnType;
import edu.ecnu.yjsy.model.student.SsnTypeRepository;
import edu.ecnu.yjsy.model.student.Student;
import edu.ecnu.yjsy.model.student.StudentRepository;
import edu.ecnu.yjsy.model.student.Unit;
import edu.ecnu.yjsy.model.student.UnitRepository;
import edu.ecnu.yjsy.model.student.University;
import edu.ecnu.yjsy.model.student.UniversityRepository;
import edu.ecnu.yjsy.service.BaseService;
import edu.ecnu.yjsy.service.auth.AccountService;
import edu.ecnu.yjsy.service.search.SearchSQLService;

@Service
public class StudentUtilExcel extends BaseService {

    private static final Logger LOG = LoggerFactory
            .getLogger(StudentUtilExcel.class);

    // FIXME:"预毕业审核日期" 这个暂时没有写上，因为暂时数据写死了，
    private static final String[] FILE_FIELDS = { "姓名", "姓名英文", "考生编号", "报名点代码", // 0-3
            "证件类型码", "证件号码", "出生日期", "国籍码", "民族码", "性别码", "婚姻状况码", "现役军人码", // 4-11
            "入伍批准书编号", "退出现役证编号", "政治面貌码", "籍贯所在地码", "出生地码", "户口所在地码", // 12-17
            "户口所在地详细地址", "家庭所在地码", "家庭所在地详细地址", "火车到站码", "考生档案所在地码", "考生档案所在单位", // 18-23
            "考生档案所在单位地址", "考生档案所在单位邮政编码", "入学前学习或工作单位性质", "入学前学习或工作单位所在地码", // 24-27
            "入学前学习或工作单位", "学习与工作经历", "家庭主要成员", "紧急联系人", "微信", "固定电话", "移动电话", // 28-34
            "电子信箱", "学号", "照片", "是否新生", "层次", "学位类型", "是否统招", "学习形式", "入学日期", // 35-43
            "年级", "学期", "当前学年应缴费", "学费总额", "缴费标准", "已缴费次数", "已缴费总额", "学费一", // 44-51
            "学费二", "学费三", "学费四", "学费五", "校区", "宿舍", "导师", "专业", "研究方向", "院系", // 52-61
            "学制", "专业排名", "硕博连读硕士阶段的入学年级", "奖惩", "医疗类型", "考试方式码", "专项计划码", // 62-68
            "专项计划说明", "录取类别码", "定向就业单位所在地码", "定向就业单位", "联合培养单位代码", "联合培养单位名称", // 69-74
            "跨专业信息", "毕业类型", "预计毕业日期", "实际毕业日期", "休学学期数", "正常毕业年限", "最长延期", // 75-81
            "预毕业审核状态", "预毕业审核日期", "授予学位名称", "授予学位日期", "毕业证领取状态", "毕业证编号", // 82-87
            "离校状态", "离校去向", "离校日期", "归档状态", "归档日期", "考生来源码", "获学士学位的单位代码", // 88-94
            "获学士学位的单位名称", "获学士学位专业代码", "获学士学位专业名称", "获学士学位日期", "学士学位证书编号", // 95-99
            "本科毕业单位代码", "本科毕业单位名称", "本科毕业专业代码", "本科毕业专业名称", "本科毕业日期", // 100-104
            "本科毕业证书编号", "取得本科学历的学习形式", "获硕士学位的单位代码", "获硕士学位的单位名称", "获硕士学位专业代码", // 105-109
            "获硕士学位专业名称", "获硕士学位日期", "硕士学位证书编号", "获硕士学位方式", "硕士毕业单位代码", // 110-114
            "硕士毕业单位名称", "硕士毕业专业代码", "硕士毕业专业名称", "硕士毕业日期", "硕士毕业证书编号", // 115-119
            "取得硕士学历的学习形式", "最后学位码", "最后学历码", "在校生注册学号", "获得最后学历毕业日期", // 120-124
            "取得最后学历的学习形式" };

    @Autowired
    private AccountService accountService;

    @Autowired
    private DisciplineRepository disciplineRepo;

    @Autowired
    private BachelorDisciplineRepository bachelorDisciplineRepo;

    @Autowired
    private EnrollmentRepository enrollmentRepo;

    @Autowired
    private EthnicRepository ethnicRepo;

    @Autowired
    private ExamineeOriginRepository examineeOriginRepo;

    @Autowired
    private NationRepository nationRepo;

    @Autowired
    private RegionRepository regionRepo;

    @Autowired
    private ReligionRepository religionRepo;

    @Autowired
    private UnitRepository unitRepo;

    @Autowired
    private SpecialPlanRepository specialPlanRepo;

    @Autowired
    private StaffRepository staffRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private SsnTypeRepository ssnTypeRepo;

    @Autowired
    private GenderRepository genderRepo;

    @Autowired
    private MarriageRepository marriageRepo;

    @Autowired
    private MilitaryRepository militaryRepo;

    @Autowired
    private RailwayRepository railwayRepo;

    @Autowired
    private PreStudyRepository prestudyRepo;

    @Autowired
    private DegreeLevelRepository degreeLevelRepo;

    @Autowired
    private DegreeTypeRepository degreeTypeRepo;

    @Autowired
    private CampusRepository campusRepo;

    @Autowired
    private MedicalRepository medicalRepo;

    @Autowired
    private ExaminationRepository examinationRepo;

    @Autowired
    private GraduationRepository graduationRepo;

    @Autowired
    private UniversityRepository universityRepo;

    @Autowired
    private EducationModeRepository educationModeRepo;

    @Autowired
    private DegreeRepository degreeRepo;

    @Autowired
    private EducationRepository educationRepo;

    @Override
    public void initConditionSearch(SearchSQLService searchSQLService) {
        // DO NOTHING
    }

    public Map<String, Object> load(InputStream in, String type)
            throws Exception {
        Workbook workbook = createWorkbook(in, type);
        Sheet sheet = workbook.getSheetAt(0);

        int error = 0;
        int total = 0;
        int success = 0;
        int failure = 0;
        Map<String, Object> result = new HashMap<>();

        // 检查文件格式
        Row titles = sheet.getRow(0);
        if (!checkFormat(titles, FILE_FIELDS)) {
            result.put("fileFormat", false);
            return result;
        }

        Map<String, Integer> schema = parseSchema(titles);
        result.put("fileFormat", true);

        List<Student> students = new ArrayList<Student>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            List<String> value = parseRow(sheet.getRow(i), FILE_FIELDS, schema);

            // 基本信息
            String name = value.get(0);// 姓名
            String nameEn = value.get(1);// 姓名英文
            String examineeNo = value.get(2);// 考生编号
            String examineeSignPlace = value.get(3);// 报名点代码
            SsnType ssnType = ssnTypeRepo.findByCode(value.get(4));// 证件类型码
            String ssn = value.get(5);// 证件号码

            Date birthdate = getDateByNYR(value.get(6));// 出生日期
            Nation nation = nationRepo.findByCode(value.get(7));// 国籍码
            Ethnic ethnic = ethnicRepo.findByCode(value.get(8));// 民族码
            Gender gender = genderRepo.findByCode(value.get(9));// 性别码
            Marriage marriage = marriageRepo.findByCode(value.get(10));// 婚姻状况码

            Military military = militaryRepo.findByCode(value.get(11));// 现役军人码
            String militaryJoinNo = null;// 入伍批准书编号
            String militaryQuitNo = null;// 退出现役证编号
            if (military != null) {
                militaryJoinNo = value.get(12);
                militaryQuitNo = value.get(13);
            }
            Religion religion = religionRepo.findByCode(value.get(14));// 政治面貌码
            Region hometown = regionRepo.findByCountyCode(value.get(15));// 籍贯所在地码

            Region birthplace = regionRepo.findByCountyCode(value.get(16));// 出生地码
            Region residence = regionRepo.findByCountyCode(value.get(17));// 户口所在地码
            String residenceAddress = value.get(18);// 户口所在地详细地址
            Region family = regionRepo.findByCountyCode(value.get(19));// 家庭所在地码
            String familyAddress = value.get(20);// 家庭所在地详细地址

            Railway railway = railwayRepo.findByStateAndName(
                    value.get(21).split("#")[0], value.get(21).split("#")[1]);
            Region examineeArchive = regionRepo.findByCountyCode(value.get(22));// 考生档案所在地码
            String examineeArchiveUnit = value.get(23);// 考生档案所在单位
            String examineeArchiveUnitAddress = value.get(24);// 考生档案所在单位地址
            String examineeArchiveUnitZipcode = this
                    .getZipcode(examineeArchive.getZipcode(), value.get(25));// 考生档案所在单位邮政编码

            PreStudy prestudyUnitNature = prestudyRepo
                    .findByCode(value.get(26));// 入学前学习或工作单位性质
            Region prestudy = regionRepo.findByCountyCode(value.get(27));// 入学前学习或工作单位所在地码
            String prestudyUnit = value.get(28);// 入学前学习或工作单位
            String experience = value.get(29);// 学习与工作经历
            String familyMember = value.get(30);// 家庭主要成员

            String emergencyContact = value.get(31);// 紧急联系人
            String weichat = value.get(32);// 微信
            String phone = value.get(33);// 固定电话
            String mobile = value.get(34);// 移动电话
            String email = value.get(35);// 电子信箱

            // 学籍信息
            String sno = value.get(36);// 学号
            String photo = value.get(37);// 照片
            boolean isNew = false;// 是否新生
            if (value.get(38) != null) {
                isNew = value.get(38).equals("是") ? true : false;
            }
            DegreeLevel degreeLevel = degreeLevelRepo.findByCode(value.get(39));// 层次
            DegreeType degreeType = degreeTypeRepo.findByCode(value.get(40));// 学位类型

            boolean isAdmissionUnified = false;// 是否统招
            if (value.get(41) != null) {
                isAdmissionUnified = value.get(41).equals("是") ? true : false;
            }
            EducationMode educationMode = educationModeRepo
                    .findByCode(value.get(42));// 学习形式
            Date admissionDate = getDateByNYR(value.get(43));;// 入学日期

            short grade = 0;// 年级
            if (value.get(44) != null) {
                grade = Short.parseShort(value.get(44));
            }

            int term = 0;// 学期
            if (value.get(45) != null) {
                term = Integer.parseInt(value.get(45));
            }

            String feeShallPay = value.get(46);// 当前学年应缴费
            String feeTotal = value.get(47);// 学费总额
            String feeStandard = value.get(48);// 缴费标准

            short feeTimes = 0;// 已缴费次数
            if (value.get(49) != null && !value.get(49).isEmpty()) {
                try {
                    feeTimes = Short.parseShort(value.get(49));
                } catch (Exception ex) {
                    LOG.error(ex.getMessage());
                }
            }

            String feeTotal2 = value.get(50);// 已缴费总额
            String fee1 = value.get(51);// 学费一
            String fee2 = value.get(52);// 学费二
            String fee3 = value.get(53);// 学费三
            String fee4 = value.get(54);// 学费四
            String fee5 = value.get(55);// 学费五

            Campus campus = campusRepo.findByCode(value.get(56));// 校区
            String domitory = value.get(57);// 宿舍
            Staff supervisor = staffRepo.findBySno(value.get(58));// 导师
            Discipline discipline = disciplineRepo
                    .findByMinorCode(value.get(59));// 专业
            String research = value.get(60);// 研究方向

            Unit unit = unitRepo.findByDepartmentCode(value.get(61));// 院系
            String los = value.get(62);// 学制
            String rank = value.get(63);// 专业排名
            String masterGrade = value.get(64);// 硕博连读硕士阶段的入学年级
            String awardAndPunishment = value.get(65);// 奖惩

            Medical medical = medicalRepo.findByCode(value.get(66));// 医疗类型
            Examination examination = examinationRepo.findByCode(value.get(67));// 考试方式码
            SpecialPlan specialPlan = specialPlanRepo.findByCode(value.get(68));// 专项计划码
            String specialPlanMemo = value.get(69);// 专项计划说明
            Enrollment enrollment = enrollmentRepo.findByCode(value.get(70));// 录取类别码

            Region directedRegion = regionRepo.findByCountyCode(value.get(71));// 定向就业单位所在地码
            String directedWork = value.get(72);// 定向就业单位
            String jointTraining = value.get(73);// 联合培养单位代码
            String jointTrainingWork = value.get(74);// 联合培养单位名称
            String crossDiscipline = value.get(75);// 跨专业信息

            Graduation graduation = graduationRepo.findByCode(value.get(76));// 毕业类型
            Date expectedGraduationDate = getDateByNYR(value.get(77));// 预计毕业日期
            Date actualGraduationDate = getDateByNYR(value.get(78));// 实际毕业日期
            String absenceTerms = value.get(79);// 休学学期数
            String duration = value.get(80);// 正常毕业年限
            String delay = value.get(81);// 最长延期

            PregraduationStatus pregraduationStatus = PregraduationStatus.空;
            Date pregraduationDate = getDateByNYR(value.get(83));// 预毕业审核日期
            if (pregraduationDate != null) {
                pregraduationStatus = PregraduationStatus.可修改;
            }

            String grantedDegree = value.get(84);// 授予学位名称
            Date grantedDegreeDate = getDateByNYR(value.get(85));// 授予学位日期
            boolean diplomaStatus = false;// 毕业证领取状态
            if (value.get(86) != null) {
                diplomaStatus = value.get(86).equals("是") ? true : false;
            }
            String diplomaNo = value.get(87);// 毕业证编号

            boolean leave = false;// 离校状态
            if (value.get(88) != null) {
                leave = value.get(88).equals("是") ? true : false;
            }
            String leaveTo = value.get(89);// 离校去向
            Date leaveDate = getDateByNYR(value.get(90));// 离校日期

            boolean archiveSatus = false;// 归档状态
            if (value.get(91) != null) {
                archiveSatus = value.get(91).equals("是") ? true : false;
            }
            Date archiveDate = getDateByNYR(value.get(92));// 归档日期

            // 来源信息
            ExamineeOrigin examineeOrigin = examineeOriginRepo
                    .findByCode(value.get(93));// 考生来源码

            University bachelorUnit = universityRepo.findByCode(value.get(94));// 获学士学位的单位代码
            String bachelorUnitName = this.getUniversity(bachelorUnit,
                    value.get(95));// 获学士学位的单位名称
            BachelorDiscipline bachelorDiscipline = bachelorDisciplineRepo
                    .findByMinorCode(value.get(96));// 获学士学位专业代码
            String bachelorDisciplineName = this
                    .getDiscipline(bachelorDiscipline, value.get(97));// 获学士学位专业名称
            Date bachelorDate = getDateByNYR(value.get(98));// 获学士学位日期
            String bachelorNo = value.get(99);// 学士学位证书编号

            University undergraduateUnit = universityRepo
                    .findByCode(value.get(100));// 本科毕业单位代码
            String undergraduateUnitName = this.getUniversity(undergraduateUnit,
                    value.get(101));// 本科毕业单位名称
            BachelorDiscipline undergraduateDiscipline = bachelorDisciplineRepo
                    .findByMinorCode(value.get(102));// 本科毕业专业代码
            String undergraduateDisciplineName = this
                    .getDiscipline(undergraduateDiscipline, value.get(103));// 本科毕业专业名称

            Date undergraduateDate = getDateByNYR(value.get(104));// 本科毕业日期
            String undergraduateNo = value.get(105);// 本科毕业证书编号

            EducationMode undergraduateMode = educationModeRepo
                    .findByCode(value.get(106));// 取得本科学历的学习形式

            University masterUnit = universityRepo.findByCode(value.get(107));// 获硕士学位的单位代码
            String masterUnitName = this.getUniversity(masterUnit,
                    value.get(108));// 获硕士学位的单位名称
            Discipline masterDiscipline = disciplineRepo
                    .findByMinorCode(value.get(109));// 获硕士学位专业代码
            String masterDisciplineName = this.getDiscipline(masterDiscipline,
                    value.get(110));// 获硕士学位专业名称

            Date masterDate = getDateByNYR(value.get(111));// 获硕士学位日期
            String masterNo = value.get(112);// 硕士学位证书编号

            EducationMode masterMode = educationModeRepo
                    .findByCode(value.get(113));// 获硕士学位方式

            University postgraduateUnit = universityRepo
                    .findByCode(value.get(114));// 硕士毕业单位代码
            String postgraduateUnitName = this.getUniversity(postgraduateUnit,
                    value.get(115));// 硕士毕业单位名称
            Discipline postgraduateDiscipline = disciplineRepo
                    .findByMinorCode(value.get(116));// 硕士毕业专业代码
            String postgraduateDisciplineName = this
                    .getDiscipline(postgraduateDiscipline, value.get(117));// 硕士毕业专业名称

            Date postgraduateDate = getDateByNYR(value.get(118));// 硕士毕业日期
            String postgraduateNo = value.get(119);// 硕士毕业证书编号

            EducationMode postgraduateMode = educationModeRepo
                    .findByCode(value.get(120));// 取得硕士学历的学习形式

            Degree finalDegree = degreeRepo.findByCode(value.get(121));// 最后学位码
            Education finalEducation = educationRepo.findByCode(value.get(122));// 最后学历码
            String finalSno = value.get(123);// 在校生注册学号

            Date finalEducationDate = getDateByNYR(value.get(124));// 获得最后学历毕业日期
            EducationMode finalEducationMode = educationModeRepo
                    .findByCode(value.get(125));// 取得最后学历的学习形式

            // 有些数据为空不允许存入数据库 这是解析时候得错误数据
            // if (name == null || sno == null || isNew == false || los == null)
            // {
            if (name == null || sno == null || los == null) {
                error++;
                continue;
            }

            Student student = new Student(name, nameEn, examineeNo,
                    examineeSignPlace, ssnType, ssn, birthdate, nation, ethnic,
                    gender, marriage, military, militaryJoinNo, militaryQuitNo,
                    religion, hometown, birthplace, residence, residenceAddress,
                    family, familyAddress, railway, examineeArchive,
                    examineeArchiveUnit, examineeArchiveUnitAddress,
                    examineeArchiveUnitZipcode, prestudyUnitNature, prestudy,
                    prestudyUnit, experience, familyMember, emergencyContact,
                    weichat, phone, mobile, email, sno, photo, isNew,
                    degreeLevel, degreeType, isAdmissionUnified, educationMode,
                    admissionDate, grade, term, feeShallPay, feeTotal,
                    feeStandard, feeTimes, feeTotal2, fee1, fee2, fee3, fee4,
                    fee5, campus, domitory, supervisor, discipline, research,
                    unit, los, rank, masterGrade, awardAndPunishment, medical,
                    examination, specialPlan, specialPlanMemo, enrollment,
                    directedRegion, directedWork, jointTraining,
                    jointTrainingWork, crossDiscipline, graduation,
                    expectedGraduationDate, actualGraduationDate, absenceTerms,
                    duration, delay, pregraduationStatus, pregraduationDate,
                    grantedDegree, grantedDegreeDate, diplomaStatus, diplomaNo,
                    leave, leaveTo, leaveDate, archiveSatus, archiveDate,
                    examineeOrigin, bachelorUnit, bachelorUnitName,
                    bachelorDiscipline, bachelorDisciplineName, bachelorDate,
                    bachelorNo, undergraduateUnit, undergraduateUnitName,
                    undergraduateDiscipline, undergraduateDisciplineName,
                    undergraduateDate, undergraduateNo, undergraduateMode,
                    masterUnit, masterUnitName, masterDiscipline,
                    masterDisciplineName, masterDate, masterNo, masterMode,
                    postgraduateUnit, postgraduateUnitName,
                    postgraduateDiscipline, postgraduateDisciplineName,
                    postgraduateDate, postgraduateNo, postgraduateMode,
                    finalDegree, finalEducation, finalSno, finalEducationDate,
                    finalEducationMode);

            students.add(student);
        }

        Set<String> snos = new HashSet<String>();
        Iterator<Student> iter = students.iterator();
        while (iter.hasNext()) {
            Student curStudent = iter.next();
            if (snos.contains(curStudent.getSno())
                    || studentRepo.findBySno(curStudent.getSno()) != null) {
                failure++;
                iter.remove();
                LOG.warn("研究生 {" + curStudent.getSno() + "} 已存在!");
            } else {
                snos.add(curStudent.getSno());
            }
        }

        for (Student newStudent : studentRepo.save(students)) {
            accountService.createStudentAccount(newStudent);
            success += students.size();
            students.clear();
        }
        total++;

        result.put("totalcount", sheet.getLastRowNum());
        result.put("errorcount", error);
        return result;
    }

    private String getZipcode(String zipcodeFromCode, String zipcode) {
        if (zipcode == null) {
            return zipcodeFromCode;
        } else if (!zipcodeFromCode.equals(zipcode)) {
            return zipcodeFromCode;
        } else {
            return zipcode;
        }
    }

    private String getUniversity(University university, String name) {
        if (university == null) return name;

        String nameFromCode = university.getName();
        if (name == null) {
            return nameFromCode;
        } else if (!nameFromCode.equals(name)) {
            return nameFromCode;
        } else {
            return name;
        }
    }

    private String getDiscipline(BachelorDiscipline discipline, String name) {
        if (discipline == null) return name;
        return this.getDiscipline(discipline.getMajor(), discipline.getMinor(),
                name);
    }

    private String getDiscipline(Discipline discipline, String name) {
        if (discipline == null) return name;
        return this.getDiscipline(discipline.getMajor(), discipline.getMinor(),
                name);
    }

    private String getDiscipline(String major, String minor, String name) {
        if (name == null) {
            if (minor == null) {
                return major;
            } else {
                return minor;
            }
        } else {
            if (minor == null) {
                return name;
            } else {
                if (!minor.equals(name)) { return minor; }
            }
            return name;
        }
    }

}
