package edu.ecnu.yjsy.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import edu.ecnu.yjsy.model.auth.DomainLevel;
import edu.ecnu.yjsy.model.auth.Page;
import edu.ecnu.yjsy.model.auth.PageRepository;
import edu.ecnu.yjsy.model.auth.RestAPI;
import edu.ecnu.yjsy.model.auth.RestAPIRepository;
import edu.ecnu.yjsy.model.auth.Role;
import edu.ecnu.yjsy.model.auth.RoleRepository;
import edu.ecnu.yjsy.model.change.StatusChangeType;
import edu.ecnu.yjsy.model.change.StatusChangeTypeRepository;
import edu.ecnu.yjsy.model.pregraduation.APIMapping;
import edu.ecnu.yjsy.model.pregraduation.APIMappingReposipory;
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
import edu.ecnu.yjsy.model.student.Unit;
import edu.ecnu.yjsy.model.student.UnitRepository;
import edu.ecnu.yjsy.model.student.University;
import edu.ecnu.yjsy.model.student.UniversityRepository;
import edu.ecnu.yjsy.service.auth.AccountService;

/**
 * FIXME: xiafan: 建议对这个类中的接口进行分装,主要为抽象出以下及各类: 1. RowMeta:
 * 用于记录表头信息以及表头和列索引之间的映射关系 2. Row的类: 记录每行具体的数据,提供通过表头和列索引两种方式获取数据
 * 因为数据加载的使用频率比较低, 这个修改优先级比较低。
 */
@Service
public class MetadataUtil {

    private static final Logger LOG = LoggerFactory
            .getLogger(MetadataUtil.class);

    private static final String PATH = "metadata";

    private static final String CSV_SSN = PATH + "/006-ssn-type.csv";
    private static final String CSV_NATION = PATH + "/009-nation.csv";
    private static final String CSV_ETHNIC = PATH + "/010-ethnic.csv";
    private static final String CSV_GENDER = PATH + "/011-gender.csv";
    private static final String CSV_MILITARY = PATH + "/013-military.csv";
    private static final String CSV_MARRIAGE = PATH + "/012-marriage.csv";
    private static final String CSV_RELIGION = PATH + "/016-religion.csv";
    private static final String CSV_REGION = PATH + "/017-region.csv";
    private static final String CSV_RAILWAY = PATH + "/023-railway.csv";
    private static final String CSV_PRESTUDY = PATH + "/028-prestudy.csv";
    private static final String CSV_DEGREE_LEVEL = PATH
            + "/043-degree-level.csv";
    private static final String CSV_DEGREE_TYPE = PATH + "/044-degree-type.csv";
    private static final String CSV_CAMPUS = PATH + "/064-campus.csv";
    private static final String CSV_DISCIPLINE = PATH + "/067-discipline.csv";
    private static final String CSV_UNIT = PATH + "/068-unit.csv";
    private static final String CSV_MEDICAL = PATH + "/074-medical.csv";
    private static final String CSV_EXAMINATION = PATH + "/077-examination.csv";
    private static final String CSV_SPECIAL_PLAN = PATH
            + "/078-special-plan.csv";
    private static final String CSV_ENROLLMENT = PATH + "/080-enrollment.csv";
    private static final String CSV_GRADUATION = PATH + "/086-graduation.csv";
    private static final String CSV_EXAMINEE_ORIGIN = PATH
            + "/103-examinee-origin.csv";
    private static final String CSV_UNIVERSITY = PATH + "/104-university.csv";
    private static final String CSV_BACHELOR_DISCIPLINE = PATH
            + "/106-bachelor-discipline.csv";
    private static final String CSV_EDUCATION_MODE = PATH
            + "/116-education-mode.csv";
    private static final String CSV_DEGREE = PATH + "/131-degree.csv";
    private static final String CSV_EDUCATION = PATH + "/132-education.csv";
    private static final String CSV_STATUS_CHANGE_TYPE = PATH
            + "/200-status-change-type-full.csv";

    private static final String CSV_ROLE = PATH + "/role.csv";
    private static final String CSV_STAFF = PATH + "/staff.csv";
    private static final String CSV_MENU = PATH + "/menu.csv";
    private static final String CSV_API = PATH + "/api.csv";
    private static final String API_ROLE_MAPPING = PATH
            + "/api-role-mapping.csv";
    private static final String ROLE_PAGE_MAPPING = PATH
            + "/role-page-mapping.csv";
    private static final String ROLE_PRIV_MAPPING = "auth/权限-职工.csv";
    private static final String API_PAGE_MAPPING = PATH
            + "/api-page-mapping.csv";

    private static final String CSV_API_MAPPING = PATH + "/yby-api-mapping.csv";

    @Autowired
    private SsnTypeRepository ssnRepo;

    @Autowired
    private NationRepository nationRepo;

    @Autowired
    private EthnicRepository ethnicRepo;

    @Autowired
    private GenderRepository genderRepo;

    @Autowired
    private MarriageRepository marriageRepo;

    @Autowired
    private MilitaryRepository militaryRepo;

    @Autowired
    private ReligionRepository religionRepo;

    @Autowired
    private RegionRepository regionRepo;

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
    private UnitRepository unitRepo;

    @Autowired
    private MedicalRepository medicalRepo;

    @Autowired
    private ExaminationRepository examinationRepo;

    @Autowired
    private SpecialPlanRepository specialPlanRepo;

    @Autowired
    private EnrollmentRepository enrollmentRepo;

    @Autowired
    private GraduationRepository graduationRepository;

    @Autowired
    private ExamineeOriginRepository examineeOriginRepo;

    @Autowired
    private UniversityRepository universityRepo;

    @Autowired
    private BachelorDisciplineRepository bachelorRepo;

    @Autowired
    private EducationModeRepository educationModeRepo;

    @Autowired
    private DegreeRepository degreeRepo;

    @Autowired
    private EducationRepository educationRepo;

    @Autowired
    private StaffRepository staffRepo;

    @Autowired
    private DisciplineRepository disciplineRepo;

    @Autowired
    private StatusChangeTypeRepository statusChangeTypeRepo;

    @Autowired
    private PageRepository pagePrivilegeRepo;

    @Autowired
    private RestAPIRepository restAPIRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private APIMappingReposipory apiMappingRepo;

    @Autowired
    private AccountService accountService;

    @SuppressWarnings("unchecked")
    public void load() {
        // load metadata
        ssnRepo.save(parse(CSV_SSN, "parseSsnType"));
        nationRepo.save(parse(CSV_NATION, "parseNation"));
        ethnicRepo.save(parse(CSV_ETHNIC, "parseEthnic"));
        genderRepo.save(parse(CSV_GENDER, "parseGender"));
        marriageRepo.save(parse(CSV_MARRIAGE, "parseMarriage"));
        militaryRepo.save(parse(CSV_MILITARY, "parseMilitary"));
        religionRepo.save(parse(CSV_RELIGION, "parseReligion"));
        regionRepo.save(parse(CSV_REGION, "parseRegion"));
        railwayRepo.save(parse(CSV_RAILWAY, "parseRailway"));
        prestudyRepo.save(parse(CSV_PRESTUDY, "parsePrestudy"));
        degreeLevelRepo.save(parse(CSV_DEGREE_LEVEL, "parseDegreeLevel"));
        degreeTypeRepo.save(parse(CSV_DEGREE_TYPE, "parseDegreeType"));
        campusRepo.save(parse(CSV_CAMPUS, "parseCampus"));
        disciplineRepo.save(parse(CSV_DISCIPLINE, "parseDiscipline"));
        unitRepo.save(parse(CSV_UNIT, "parseUnit"));
        medicalRepo.save(parse(CSV_MEDICAL, "parseMedical"));
        examinationRepo.save(parse(CSV_EXAMINATION, "parseExamination"));
        specialPlanRepo.save(parse(CSV_SPECIAL_PLAN, "parseSpecialPlan"));
        enrollmentRepo.save(parse(CSV_ENROLLMENT, "parseEnrollment"));
        graduationRepository.save(parse(CSV_GRADUATION, "parseGraduation"));
        examineeOriginRepo
                .save(parse(CSV_EXAMINEE_ORIGIN, "parseExamineeOrigin"));
        universityRepo.save(parse(CSV_UNIVERSITY, "parseUniversity"));
        bachelorRepo.save(parse(CSV_BACHELOR_DISCIPLINE, "parseBachelor"));
        educationModeRepo.save(parse(CSV_EDUCATION_MODE, "parseEducationMode"));
        degreeRepo.save(parse(CSV_DEGREE, "parseDegree"));
        educationRepo.save(parse(CSV_EDUCATION, "parseEducation"));

        /**
         * 以下几项与权限相关，需要保证以下顺序： 1. 先导入角色数据 2. 在导入页面信息 3. 导入API信息 4. 导入API和角色之间的关联
         * 5. 导入角色和页面之间的关联
         */
        roleRepo.save(parse(CSV_ROLE, "parseRole"));
        parse(CSV_API, "parseAPI");
        parse(CSV_MENU, "parsePagePrivilege");
        parse(API_PAGE_MAPPING, "parseAPIPageMapping");
        parse(API_ROLE_MAPPING, "parseAPIRoleMapping");
        parse(ROLE_PAGE_MAPPING, "parseRolePageMapping");

        // 保证权限的元数据已经导入完毕;先导入角色数据，在导入角色的权限数据
        parse(CSV_STAFF, "parseStaff");
        loadStaffPrivilege();

        // load status change types
        parse(CSV_STATUS_CHANGE_TYPE, "parseStatusChangeType");

        // load pregraduation api mapping
        apiMappingRepo.save(parse(CSV_API_MAPPING, "parseAPIMapping"));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List parse(String file, String method) {
        ClassPathResource resource = new ClassPathResource(file);
        if (!resource.exists()) {
            LOG.error("Not found {}", file);
        }

        List result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(resource.getFile()), "UTF-8"));) {
            String line = br.readLine(); // ignore the first line

            while ((line = br.readLine()) != null) {
                // line = line.replaceAll("\\s*", "");
                line = line.trim();
                String[] values = line.split(",", -1);
                result.add(this.getClass().getMethod(method, values.getClass())
                        .invoke(this, (Object) values));
            }
        } catch (Exception e) {
            LOG.error("{}", e);
            throw new RuntimeException(e);
        }

        return result;
    }

    public void loadStaffPrivilege() {
        ClassPathResource resource = new ClassPathResource(ROLE_PRIV_MAPPING);
        if (!resource.exists()) {
            LOG.error("Not found {}", ROLE_PRIV_MAPPING);
        }
        try {
            byte[] data = new byte[(int) resource.getFile().length()];
            new FileInputStream(resource.getFile()).read(data);
            accountService.uploadStaffPrivilege(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // 加载教师
    // =========================

    public Staff parseStaff(String[] splits) {
        Staff staff = new Staff();
        staff.setName(splits[0]);
        staff.setSno(splits[1]);
        staff = staffRepo.save(staff);
        accountService.createStaffAccount(staff);
        return staff;
    }

    // =========================
    // 加载角色
    // =========================

    public Role parseRole(String[] splits) {
        Role role = new Role();
        role.setName(splits[0]);
        role.setDomainLevel(DomainLevel.valueOf(splits[1].toUpperCase()));
        return role;
    }

    public Page parsePagePrivilege(String[] splits) {
        try {
            Page page = new Page();
            page.setId(Long.parseLong(splits[0]));
            page.setIconClass(splits[1]);
            page.setPageName(splits[2]);
            page.setParentPageID(Long.parseLong(splits[3]));
            page.setUrl(splits[4]);
            page.setDescription(splits[5]);
            page.setAnnotation(splits[6]);
            return pagePrivilegeRepo.save(page);
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }
        return null;
    }

    public RestAPI parseAPI(String[] splits) {
        RestAPI restAPI = new RestAPI();
        restAPI.setApi(splits[0]);
        restAPI.setHttpMethod(splits[1]);
        restAPI.setDescription(splits[2]);
        restAPI.setExample(splits[3]);
        return restAPIRepo.save(restAPI);
    }

    /**
     * 保存api和页面的对应关系
     *
     * @param splits
     * @return
     */
    public RestAPI parseAPIPageMapping(String[] splits) {
        RestAPI restAPI = restAPIRepo.findByApiAndHttpMethod(splits[0],
                splits[1]);
        if (restAPI == null) {
            LOG.error("api is not found:" + StringUtils.join(splits, ","));
        }

        if (splits.length > 2 && !splits[2].isEmpty()) {
            Page pp = pagePrivilegeRepo
                    .findWithApisById(Long.parseLong(splits[2]));
            if (pp != null) {
                pp.getRestAPIs().add(restAPI);
                pagePrivilegeRepo.save(pp);
            } else {
                // FIXME
                // Should use library like apache.common to deal with this array
                StringBuffer sb = new StringBuffer();
                for (String split : splits) {
                    sb.append(split).append(", ");
                }
                LOG.error("Not found page when setting up method privilege: "
                        + sb.toString());
            }
        }
        restAPI = restAPIRepo.save(restAPI);
        return restAPI;
    }

    public RestAPI parseAPIRoleMapping(String[] splits) {
        RestAPI restAPI = restAPIRepo.findByApiAndHttpMethod(splits[0],
                splits[1]);
        if (restAPI == null) {
            restAPI = new RestAPI();
            restAPI.setApi(splits[0]);
            restAPI.setHttpMethod(splits[1]);
            restAPI.setRoles(new HashSet<>());
            restAPI = restAPIRepo.save(restAPI);
        }

        if (splits.length > 2) {
            try {
                String[] roleNames = splits[2].split("_");
                for (String roleName : roleNames) {
                    Role role = roleRepo.findByName(roleName);
                    if (role != null) {
                        restAPI.getRoles().add(role);
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        try {
            restAPIRepo.save(restAPI);
        } catch (DataIntegrityViolationException ex) {
            LOG.error(ex.getMessage());
        }
        return restAPI;
    }

    /**
     * 需要保证角色数据和菜单数据已经导入
     *
     * @param splits
     */
    public void parseRolePageMapping(String[] splits) {
        try {
            Role role = roleRepo.findPagesByName(splits[0]);
            // FIXME: 最好避免读取pages之后在保存回去,是否有custom method直接可以添加关联
            if (role.getPages() == null) role.setPages(new HashSet<>());
            Page page = new Page();
            page.setId(Long.parseLong(splits[1]));
            role.getPages().add(page);
            roleRepo.save(role);
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }
    }

    // =========================
    // 加载元数据
    // =========================

    public SsnType parseSsnType(String[] values) {
        return new SsnType(values[0], values[1]);
    }

    public Nation parseNation(String[] values) {
        return new Nation(values[0], values[1]);
    }

    public Ethnic parseEthnic(String[] values) {
        return new Ethnic(values[0], values[1]);
    }

    public Gender parseGender(String[] values) {
        return new Gender(values[0], values[1]);
    }

    public Marriage parseMarriage(String[] values) {
        return new Marriage(values[0], values[1]);
    }

    public Military parseMilitary(String[] values) {
        return new Military(values[0], values[1]);
    }

    public Religion parseReligion(String[] values) {
        return new Religion(values[0], values[1]);
    }

    public Region parseRegion(String[] values) {
        return new Region(values[1], values[2], values[3], values[4], values[5],
                values[6], values[7], values[8]);
    }

    public Railway parseRailway(String[] values) {
        return new Railway(values[0], values[1], values[2], values[3]);
    }

    public PreStudy parsePrestudy(String[] values) {
        return new PreStudy(values[0], values[1]);
    }

    public DegreeLevel parseDegreeLevel(String[] values) {
        return new DegreeLevel(values[0], values[1]);
    }

    public DegreeType parseDegreeType(String[] values) {
        return new DegreeType(values[0], values[1]);
    }

    public Campus parseCampus(String[] values) {
        return new Campus(values[0], values[1]);
    }

    private String convertDegreeLevel(String level) {
        if (level.equals("a")) {
            return "12";
        } else if (level.equals("s")) {
            return "1";
        } else if (level.equals("b")) {
            return "2";
        } else {
            // 华师大自设专业
            return "0";
        }
    }

    private String convertDegreeType(String type, String minorCode) {
        boolean isScientific = !(minorCode.substring(2, 3).equals("5"));
        if (isScientific && type.equals("xs")) {
            return "1";
        } else if (!isScientific && type.equals("zx")) {
            return "2";
        } else {
            LOG.error("Unknown or invalid degree type: {}", type);
            return "0";
        }
    }

    public Discipline parseDiscipline(String[] values) {
        return new Discipline(
                // MLDM, MLMC, YJXKDM, YJXKMC, ZYDM, ZYMC
                values[0], values[1], values[2], values[3], values[4],
                values[5],
                // YZYDM
                values[6],
                // FIXME: 硬编码问题
                // XWLX (@see DegreeType), CC (@see DegreeLevel)
                this.convertDegreeType(values[7], values[4]),
                this.convertDegreeLevel(values[8]),
                // ZYSZQY, XZ
                values[9], values[10],
                // SINCE
                values[11],
                // UNTIL, ZYLYDM, ZYQXDM
                null, null, null);
    }

    public Unit parseUnit(String[] values) {
        String[] temp = new String[12];

        for (int i = 0; i < values.length; i++) {
            temp[i] = values[i];
        }

        return new Unit(
                // XJDWMC, XJDWDM
                temp[0], temp[1],
                // BYMC, BYDM, BYLX
                temp[2], temp[3], temp[4],
                // YXMC, YXDM, YXLX
                temp[5], temp[6], temp[7],
                // FIXME: 硬编码问题
                // SINCE
                "2016",
                // UNTIL, ZYLYDM, ZYQXDM
                null, null, null);
    }

    public Medical parseMedical(String[] values) {
        return new Medical(values[0], values[1]);
    }

    public Examination parseExamination(String[] values) {
        return new Examination(values[0], values[1]);
    }

    public SpecialPlan parseSpecialPlan(String[] values) {
        return new SpecialPlan(values[0], values[1]);
    }

    public Enrollment parseEnrollment(String[] values) {
        return new Enrollment(values[0], values[1]);
    }

    public Graduation parseGraduation(String[] values) {
        return new Graduation(values[0], values[1]);
    }

    public ExamineeOrigin parseExamineeOrigin(String[] values) {
        return new ExamineeOrigin(values[0], values[1]);
    }

    // FIXME: HARDCODE TO PARSE BXLX
    private String[] parseOperation(String operation) {
        String[] values = operation.split(":");
        String[] ops = new String[] { "", "", "" };
        if (values.length == 1) {
            ops[0] = operation;
        } else if (values.length == 2) {
            ops[0] = values[0];
            ops[1] = values[1];
            ops[2] = null;
        } else {
            ops[0] = values[0];
            ops[1] = values[1];
            ops[2] = values[2];
        }
        return ops;
    }

    private boolean trueOrFalse(String value) {
        if (value.equals("是")) {
            return true;
        } else {
            return false;
        }
    }

    public University parseUniversity(String[] values) {
        String[] ops = this.parseOperation(values[5]);
        return new University(
                // YXDM, YXMC,
                values[0], values[1],
                // SSDM, SSMC
                values[2], values[3],
                // BXLXDM, BXLXMC
                values[4], ops[0], ops[1], ops[2],
                // XZLBDM, XZLBMC
                values[6], values[7],
                // SF211, SF985
                this.trueOrFalse(values[8]), this.trueOrFalse(values[9]));
    }

    public BachelorDiscipline parseBachelor(String[] values) {
        return new BachelorDiscipline(values[0], values[1], values[2],
                values[3], values[4], values[5]);
    }

    public EducationMode parseEducationMode(String[] values) {
        return new EducationMode(values[0], values[1]);
    }

    public Degree parseDegree(String[] values) {
        return new Degree(values[0], values[1]);
    }

    public Education parseEducation(String[] values) {
        return new Education(values[0], values[1]);
    }

    // =========================
    // 加载学籍异动
    // =========================

    public void parseStatusChangeType(String[] values) {
        StatusChangeType statusChangeType = new StatusChangeType();

        statusChangeType.setMajorCode(Short.parseShort(values[0]));
        statusChangeType.setMajor(values[1]);
        statusChangeType.setMinorCode(Short.parseShort(values[2]));
        statusChangeType.setMinor(values[3]);
        statusChangeType.setRestrict(values[4]);

        // FIXME
        // 定义私有函数处理类似的判断
        if (values[6].equals("是")) {
            statusChangeType.setVisible(true);
        } else {
            statusChangeType.setVisible(false);
        }

        if (values[7].contains("需要")) {
            statusChangeType.setVisible(true);
        } else {
            statusChangeType.setVisible(false);
        }

        statusChangeType.setModification(values[8]);
        statusChangeType.setMemo(values[9]);
        statusChangeType.setStatus(values[10]);

        if (values[11].equals("是")) {
            statusChangeType.setQuit(true);
        } else {
            statusChangeType.setQuit(false);
        }

        if (values[12].equals("是")) {
            statusChangeType.setTerminated(true);
        } else {
            statusChangeType.setTerminated(false);
        }

        statusChangeType.setReplyToStudent(values[13]);
        statusChangeType.setReplyToSupervisor(values[14]);

        statusChangeTypeRepo.save(statusChangeType);
    }

    // =========================
    // 加载预毕业的API映射
    // =========================

    public APIMapping parseAPIMapping(String[] values) {
        values[2] = values[2].replaceFirst(values[2].substring(0, 1),
                values[2].substring(0, 1).toLowerCase());
        return new APIMapping(values[1], values[2], values[3], values[4]);
    }

}
