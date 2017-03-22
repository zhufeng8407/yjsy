package edu.ecnu.yjsy.service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import edu.ecnu.yjsy.model.change.AuditWorkflow;
import edu.ecnu.yjsy.model.change.AuditWorkflowRepository;
import edu.ecnu.yjsy.model.change.StatusChangeType;
import edu.ecnu.yjsy.model.change.StatusChangeTypeCombine;
import edu.ecnu.yjsy.model.change.StatusChangeTypeRepository;
import edu.ecnu.yjsy.model.pregraduation.APIMappingReposipory;
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
import net.sf.json.JSONObject;

/**
 * Used to provide two types of restful apis: one for form and the other for
 * metadata management (pageable).
 *
 * @author xulinhao
 */

@Service
public class MetadataService {

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
    private SpecialPlanRepository planRepo;

    @Autowired
    private EnrollmentRepository enrollmentRepo;

    @Autowired
    private GraduationRepository graduationRepo;

    @Autowired
    private ExamineeOriginRepository examineeRepo;

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
    private StatusChangeTypeRepository statusChangeTypeRepo;

    @Autowired
    private AuditWorkflowRepository auditWorkflowRepo;

    @Autowired
    private APIMappingReposipory apiRepo;

    @Autowired
    private DisciplineRepository disciplineRepo;

    private static final String IMAGEPATH = "images/";

    public List<String> getAPIMapping() {
        return apiRepo.findFields();
    }

    public List<SsnType> getSsnType() {
        return ssnRepo.findAll();
    }

    public Page<SsnType> getSsnType(PageRequest pageable) {
        return ssnRepo.findAll(pageable);
    }

    public List<Nation> getNation() {
        return nationRepo.findAll();
    }

    public Page<Nation> getNation(PageRequest pageable) {
        return nationRepo.findAll(pageable);
    }

    public List<Ethnic> getEthnic() {
        return ethnicRepo.findAll();
    }

    public Page<Ethnic> getEthnic(PageRequest pageable) {
        return ethnicRepo.findAll(pageable);
    }

    public List<Gender> getGender() {
        return genderRepo.findAll();
    }

    public Page<Gender> getGender(PageRequest pageable) {
        return genderRepo.findAll(pageable);
    }

    public List<Marriage> getMarriage() {
        return marriageRepo.findAll();
    }

    public Page<Marriage> getMarriage(PageRequest pageable) {
        return marriageRepo.findAll(pageable);
    }

    public List<Military> getMilitary() {
        return militaryRepo.findAll();
    }

    public Page<Military> getMilitary(PageRequest pageable) {
        return militaryRepo.findAll(pageable);
    }

    public List<Religion> getReligion() {
        return religionRepo.findAll();
    }

    public Page<Religion> getReligion(PageRequest pageable) {
        return religionRepo.findAll(pageable);
    }
    // 地区的一级是省份，还是返回String

    public List<Region> getRegionState() {
        return regionRepo.findStates();
    }

    // 地区的二级是城市，用省份查询，还是返回String
    public List<Region> getRegionCity(String stateCode) {
        return regionRepo.findCities(stateCode);
    }

    public List<Region> getRegionCounty(String cityCode) {
        return regionRepo.findCounties(cityCode);
    }

    public Page<Region> getRegion(PageRequest pageable) {
        return regionRepo.findAll(pageable);
    }

    public List<Railway> getRailwayState() {
        return railwayRepo.findStates();
    }

    public List<Railway> getRailwayStation(String state) {
        return railwayRepo.findStations(state);
    }

    public Page<Railway> getRailway(PageRequest pageable) {
        return railwayRepo.findAll(pageable);
    }

    public List<PreStudy> getPrestudy() {
        return prestudyRepo.findAll();
    }

    public Page<PreStudy> getPrestudy(PageRequest pageable) {
        return prestudyRepo.findAll(pageable);
    }

    public List<DegreeLevel> getDegreeLevel() {
        return degreeLevelRepo.findAll();
    }

    public Page<DegreeLevel> getDegreeLevel(PageRequest pageable) {
        return degreeLevelRepo.findAll(pageable);
    }

    public List<DegreeType> getDegreeType() {
        return degreeTypeRepo.findAll();
    }

    public Page<DegreeType> getDegreeType(PageRequest pageable) {
        return degreeTypeRepo.findAll(pageable);
    }

    public List<Campus> getCampus() {
        return campusRepo.findAll();
    }

    public Page<Campus> getCampus(PageRequest pageable) {
        return campusRepo.findAll(pageable);
    }

    public Page<Discipline> getDisciplineByPage(PageRequest pageable) {
        return disciplineRepo.findAll(pageable);
    }

    // 学科的门类
    public List<Discipline> getDisciplineCategory() {
        return disciplineRepo.findCategories();
    }

    // 学科的一级的学科
    public List<Discipline> getDisciplineMajor(String categoryCode) {
        return disciplineRepo.findMajors(categoryCode);
    }

    // 学科的二级学科
    public List<Discipline> getDisciplineMinor(String majorCode) {
        return disciplineRepo.findMinors(majorCode);
    }

    public Page<Discipline> getDiscipline(PageRequest pageable) {
        return disciplineRepo.findAll(pageable);
    }

    public List<Unit> getUnit() {
        return unitRepo.findAll();
    }

    public List<Unit> getUnitSchool() {
        return unitRepo.findSchools();
    }

    public List<Unit> getUnitDepartment(String schoolCode) {
        return unitRepo.findDepartments(schoolCode);
    }

    public Page<Unit> getUnit(PageRequest pageable) {
        return unitRepo.findAll(pageable);
    }

    public List<Medical> getMedical() {
        return medicalRepo.findAll();
    }

    public Page<Medical> getMedical(PageRequest pageable) {
        return medicalRepo.findAll(pageable);
    }

    // FIXME
    // QUERY BY TYPE: 1 - MASTER, 2 - DOCTOR
    public List<Examination> getExamination() {
        return examinationRepo.findAll();
    }

    public Page<Examination> getExamination(PageRequest pageable) {
        return examinationRepo.findAll(pageable);
    }

    // FIXME
    // QUERY BY TYPE: JOIN WITH CODE OF ENROLLMENT
    public List<SpecialPlan> getSpecialPlan() {
        return planRepo.findAll();
    }

    public Page<SpecialPlan> getSpecialPlan(PageRequest pageable) {
        return planRepo.findAll(pageable);
    }

    public List<Enrollment> getEnrollment() {
        return enrollmentRepo.findAll();
    }

    public Page<Enrollment> getEnrollment(PageRequest pageable) {
        return enrollmentRepo.findAll(pageable);
    }

    public List<Graduation> getGraduation() {
        return graduationRepo.findAll();
    }

    public Page<Graduation> getGraduation(PageRequest pageable) {
        return graduationRepo.findAll(pageable);
    }

    // FIXME
    // QUERY BY TYPE: 1 - MASTER, 2 - DOCTOR
    public List<ExamineeOrigin> getExamineeOrigin() {
        return examineeRepo.findAll();
    }

    public Page<ExamineeOrigin> getExamineeOrigin(PageRequest pageable) {
        return examineeRepo.findAll(pageable);
    }

    public List<University> getUniversity() {
        return universityRepo.findAll();
    }

    public Page<University> getUniversity(PageRequest pageable) {
        return universityRepo.findAll(pageable);
    }

    public Map<String, Object> getUniversityIdName(String bachelorUnitName) {
        Map<String, Object> res = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();
        bachelorUnitName = "%" + bachelorUnitName + "%";
        for (University university : universityRepo
                .findIdAndName(bachelorUnitName)) {
            Map<String, Object> tmp = new HashMap<>();
            tmp.put("id", university.getId());
            tmp.put("name", university.getName());
            data.add(tmp);
        }
        res.put("data", data);
        return res;
    }

    public List<BachelorDiscipline> getBachelorCategory() {
        return bachelorRepo.findCategories();
    }

    public List<BachelorDiscipline> getBachelorMajor(String categoryCode) {
        return bachelorRepo.findMajors(categoryCode);
    }

    public List<BachelorDiscipline> getBachelorMinor(String majorCode) {
        return bachelorRepo.findMinors(majorCode);
    }

    public Page<BachelorDiscipline> getBachelorDiscipline(
            PageRequest pageable) {
        return bachelorRepo.findAll(pageable);
    }

    // FIXME
    // QUERY BY TYPE: 1 - MASTER, 2 - DOCTOR, 3 - BACHELOR
    public List<EducationMode> getEducationMode() {
        return educationModeRepo.findAll();
    }

    public Page<EducationMode> getEducationMode(PageRequest pageable) {
        return educationModeRepo.findAll(pageable);
    }

    public List<Degree> getDegree() {
        return degreeRepo.findAll();
    }

    public Page<Degree> getDegree(PageRequest pageable) {
        return degreeRepo.findAll(pageable);
    }

    public List<Education> getEducation() {
        return educationRepo.findAll();
    }

    public Page<Education> getEducation(PageRequest pageable) {
        return educationRepo.findAll(pageable);
    }

    // ==========================
    // 学籍异动相关的接口
    // ==========================

    public List<StatusChangeType> getStatusChangeType() {
        return statusChangeTypeRepo.findByOrderByMajorAscMinorAsc();
    }

    /**
     * FIXME: 是否是孙晨添加的?
     *
     * @return
     */
    public List<StatusChangeTypeCombine> getStatusChangeTypeCombine() {
        List<StatusChangeTypeCombine> res = new ArrayList<StatusChangeTypeCombine>();
        List<String> majors = statusChangeTypeRepo.findMajor();
        for (String major : majors) {
            StatusChangeTypeCombine type = new StatusChangeTypeCombine();
            List<String> minors = statusChangeTypeRepo.findMinorByMajor(major);
            List<StatusChangeType> statusChangeTypes = new ArrayList<StatusChangeType>();
            List<String> auditFlow = new ArrayList<String>();
            type.setName(major);
            type.setCount(minors.size());
            for (String minor : minors) {
                StatusChangeType statusChangeType = getStatusChangeTypeByMinor(
                        minor);
                statusChangeTypes.add(statusChangeType);
                List<AuditWorkflow> flow = auditWorkflowRepo
                        .findByType(statusChangeType);
                String tempFlow = "";
                for (int i = 0; i < flow.size(); i++) {
                    tempFlow += flow.get(i).getRole().getName();
                    if (i != flow.size() - 1) {
                        tempFlow += "->";
                    }
                }
                auditFlow.add(tempFlow);
            }
            type.setStatusChangeTypes(statusChangeTypes);
            type.setAuditWorkflow(auditFlow);
            res.add(type);
        }
        return res;
    }

    // 肯据StatusChangeType id来查找异动类型
    public StatusChangeType getStatusChangeTypeById(String id) {
        return statusChangeTypeRepo.findById(new Long(id));
    }

    // FIXME
    // 建议用 minorCode 查询
    public StatusChangeType getStatusChangeTypeByMinor(String minor) {
        return statusChangeTypeRepo.findByMinor(minor);
    }

    public StatusChangeType getStatusChangeTypeByMinor(String majorCode,
            String minorCode) {
        return statusChangeTypeRepo.findByMajorCodeAndMinorCode(
                new Short(majorCode), new Short(minorCode));
    }

    // FIXME
    // 返回 StatusChangeType 对象 里面包括代码字段
    public List<String> getStatusChangeTypeMajor() {
        return statusChangeTypeRepo.findMajor();
    }

    public List<List<String>> getMajorCodeAndName() {
        List<StatusChangeType> statusChangeTypeList = statusChangeTypeRepo
                .findMajorCodeAndName();

        List<List<String>> resultList = new ArrayList<List<String>>();

        List<String> arrList = null;
        for (StatusChangeType status : statusChangeTypeList) {
            arrList = new ArrayList<String>();
            arrList.add(String.valueOf(status.getMajorCode()));
            arrList.add(status.getMajor());
            resultList.add(arrList);
        }

        return resultList;
    }

    // FIXME
    // 上面的方法 getStatusChangeTypeMajor 是否包含这个功能
    public short getStatusChangeTypeMajorCode(String major) {
        return statusChangeTypeRepo.findMajorCodeByMajor(major);
    }

    // FIXME
    // 是否可以配置对应的 majorCode 和 minorCode 为自增长 @GeneratedValue
    // 多人同时增加异动类型会有不一致的危险
    public short getStatusChangeTypeMaxMajorCode() {
        return statusChangeTypeRepo.findMaxMajorCode();
    }

    // FIXME
    // 如果不考虑传入 majorCode，有什么特定的意义吗？
    public List<String> getStatusChangeTypeMinor() {
        return statusChangeTypeRepo.findMinor();
    }

    // FIXME
    // 建议用 majorCode 查询
    // 返回 StatusChangeType 对象 里面包括代码字段
    public List<String> getStatusChangeTypeMinorByMajor(String major) {
        return statusChangeTypeRepo.findMinorByMajor(major);
    }

    // 返回 StatusChangeType 对象 里面的小分类包括代码字段
    public List<List<String>> getMinorCodeAndName(String majorCode) {
        List<StatusChangeType> statusChangeTypeList = statusChangeTypeRepo
                .findMinorByMajorCode(new Short(majorCode));

        List<List<String>> resultList = new ArrayList<List<String>>();

        List<String> arrList = null;
        for (StatusChangeType status : statusChangeTypeList) {
            arrList = new ArrayList<String>();
            arrList.add(String.valueOf(status.getMinorCode()));
            arrList.add(status.getMinor());
            resultList.add(arrList);
        }

        return resultList;
    }

    // FIXME
    // 是否可以配置对应的 majorCode 和 minorCode 为自增长 @GeneratedValue
    // 多人同时增加异动类型会有不一致的危险
    public short getStatusChangeTypeMaxMinorCodeByMajor(String major) {
        return statusChangeTypeRepo.findMaxMinorCodeByMajor(major);
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> saveStatusChangeType(String statusChangeType) {
        Map<String, Object> map = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(statusChangeType), Map.class);
        String major = map.get("major").toString();
        String minor = map.get("minor").toString();
        short majorCode = 0;
        short minorCode = 0;

        // FIXME
        // 编程逻辑需要改一下，现在的逻辑不好读，虽然对
        // if (!this.getStatusChangeTypeMajor().contains(major)) {
        // majorCode = (short) (this.getStatusChangeTypeMaxMajorCode() + 1);
        // minorCode = 1;
        // } else if (this.getStatusChangeTypeMinor().contains(minor)) {
        // res.put("status", "您所输入的小类已存在");
        // return res;
        // } else {
        // majorCode = this.getStatusChangeTypeMajorCode(major);
        // minorCode = (short) (this
        // .getStatusChangeTypeMaxMinorCodeByMajor(major) + 1);
        // }

        Map<String, String> res = new HashMap<String, String>();
        if (!this.getStatusChangeTypeMajor().contains(major)) {
            majorCode = (short) (this.getStatusChangeTypeMaxMajorCode() + 1);
            minorCode = 1;
        } else if (this.getStatusChangeTypeMinor().contains(minor)) {
            res.put("status", "您所输入的小类已存在");
            return res;
        } else {
            majorCode = this.getStatusChangeTypeMajorCode(major);
            minorCode = (short) (this
                    .getStatusChangeTypeMaxMinorCodeByMajor(major) + 1);
        }
        StatusChangeType type = new StatusChangeType();
        type.setMajor(major);
        type.setMajorCode(majorCode);
        type.setMinor(minor);
        type.setMinorCode(minorCode);
        // FIXME
        // 还需要其他信息，要参考异动设计文档
        type.setMyConstraint(map.get("myConstraint") != null
                ? map.get("myConstraint").toString() : null);
        type.setMemo(
                map.get("memo") != null ? map.get("memo").toString() : null);
        type.setVisible(map.get("isVisible") != null
                ? (Boolean) map.get("isVisible") : null);
        type.setUploadFile(map.get("isUploadFile") != null
                ? (Boolean) map.get("isUploadFile") : null);
        statusChangeTypeRepo.save(type);

        // FIXME
        // 用 ObjectMapper
        res.put("status", "保存成功");
        return res;
    }

    public byte[] getImage(String imagePath) {
        ClassPathResource resource = new ClassPathResource(
                IMAGEPATH + imagePath);
        byte[] buffer = null;
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(resource.getFile());
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                output.write(b, 0, n);
            }
            buffer = output.toByteArray();
            output.close();
            fis.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return buffer;
    }

}
