package edu.ecnu.yjsy.service.xj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import edu.ecnu.yjsy.model.EntityId;
import edu.ecnu.yjsy.model.student.Gender;
import edu.ecnu.yjsy.model.student.GenderRepository;
import edu.ecnu.yjsy.model.student.Region;
import edu.ecnu.yjsy.model.student.Student;
import edu.ecnu.yjsy.model.student.StudentRepository;
import edu.ecnu.yjsy.service.BaseService;
import edu.ecnu.yjsy.service.auth.AccountService;
import edu.ecnu.yjsy.service.search.SearchSQLService;
import edu.ecnu.yjsy.service.search.processor.CountProcessor;
import edu.ecnu.yjsy.service.search.processor.SupervisorProcessor;
import edu.ecnu.yjsy.service.util.StudentUtilExcel;
import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONObject;

@Service
public class StudentService extends BaseService {

    private static final Logger LOG = LoggerFactory
            .getLogger(StudentService.class);

    private static final DecimalFormat DF = new DecimalFormat("0");

    private static final DateFormat FMT_NYR = new SimpleDateFormat(
            "yyyy-MM-dd");

    @Autowired
    private AccountService accountService;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private GenderRepository genderRepo;

    @Autowired
    private StudentUtilExcel util;

    @Autowired
    public void initConditionSearch(SearchSQLService searchSQLService) {
        exec = searchSQLService.defaultSearch().addProcessor(new SupervisorProcessor()).create();
        countExec = searchSQLService.defaultSearch().addProcessor(new SupervisorProcessor())
                .addProcessor(new CountProcessor()).create();
    }

    public Map<String, Object> getStudentBriefInfo(Student student) {
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("id", student.getId());
        ret.put("name", student.getName());
        ret.put("sno", student.getSno());
        ret.put("new", student.isNew());
        ret.put("photo", student.getPhoto());

        if (student.getUnit() != null) {
            ret.put("school", student.getUnit().getSchool());
            ret.put("department", student.getUnit().getDepartment());
        }

        ret.put("grade", student.getGrade());
        if (student.getDiscipline() != null) {
            ret.put("minor", student.getDiscipline().getMinor());
        }

        if (student.getDegreeType() != null) {
            ret.put("degreeType", student.getDegreeType().toString());
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public boolean save(String sno, String json)
            throws ParseException, IOException {
        Map<String, Object> map = (Map<String, Object>) JSONObject
                .toBean(JSONObject.fromObject(json), Map.class);
        // 地址解析
        String family = "";
        if (map.get("familyRegion") instanceof MorphDynaBean) {
            Region familyRegion = (Region) JSONObject.toBean(
                    JSONObject.fromObject(map.get("familyRegion")),
                    Region.class);
            family = familyRegion.getState() + "#" + familyRegion.getCity()
                    + "#" + familyRegion.getCounty() + "#";
        }
        if (map.get("detailedFamily") != null) {
            family += map.get("detailedFamily").toString();
        }

        // 时间属性
        Date birthdayDate = null;
        Date leaveDate = null;
        Date archiveDate = null;
        Date admissionDate = null;
        Date expectedGraduationDate = null;
        Date actualGraduationDate = null;
        Date pregraduationDate = null;
        Date grantedDegreeDate = null;
        Date bachelorDate = null;
        Date undergradudateDate = null;
        Date masterDate = null;
        Date postgraduateDate = null;
        Date finalEducationDate = null;
        try {
            birthdayDate = map.get("birthdate") != null
                    ? getDateByNYR(map.get("birthdate").toString()) : null;
            leaveDate = map.get("leaveDate") != null
                    ? getDateByNYR(map.get("leaveDate").toString()) : null;
            archiveDate = map.get("archiveDate") != null
                    ? getDateByNYR(map.get("archiveDate").toString()) : null;
            admissionDate = map.get("admissionDate") != null
                    ? getDateByNYR(map.get("admissionDate").toString()) : null;
            expectedGraduationDate = map.get("expectedGraduationDate") != null
                    ? getDateByNYR(map.get("expectedGraduationDate").toString())
                    : null;
            actualGraduationDate = map.get("actualGraduationDate") != null
                    ? getDateByNYR(map.get("actualGraduationDate").toString())
                    : null;
            pregraduationDate = map.get("pregraduationDate") != null
                    ? getDateByNYR(map.get("pregraduationDate").toString())
                    : null;
            grantedDegreeDate = map.get("grantedDegreeDate") != null
                    ? getDateByNYR(map.get("grantedDegreeDate").toString())
                    : null;
            bachelorDate = map.get("bachelorDate") != null
                    ? getDateByNYR(map.get("bachelorDate").toString()) : null;
            undergradudateDate = map.get("undergradudateDate") != null
                    ? getDateByNYR(map.get("undergradudateDate").toString())
                    : null;
            masterDate = map.get("masterDate") != null
                    ? getDateByNYR(map.get("masterDate").toString()) : null;
            postgraduateDate = map.get("postgraduateDate") != null
                    ? getDateByNYR(map.get("postgraduateDate").toString())
                    : null;
            finalEducationDate = map.get("finalEducationDate") != null
                    ? getDateByNYR(map.get("finalEducationDate").toString())
                    : null;
        } catch (Exception e) {
            LOG.error("{}", e);
            throw new ParseException(e.getMessage(), 0);
        }

        // 非元数据基本信息
        String name = map.get("name") != null ? map.get("name").toString() : "";
        String nameEn = map.get("nameEn") != null ? map.get("nameEn").toString()
                : "";
        String examineeNo = map.get("examineeNo") != null
                ? map.get("examineeNo").toString() : "";
        String examineeSign = map.get("examineeSign") != null
                ? map.get("examineeSign").toString() : "";
        String ssn = map.get("ssn") != null ? map.get("ssn").toString() : "";
        String militaryJoinNo = map.get("militaryJoinNo") != null
                ? map.get("militaryJoinNo").toString() : "";
        String militaryQuitNo = map.get("militaryQuitNo") != null
                ? map.get("militaryQuitNo").toString() : "";
        String residenceAddress = map.get("residenceAddress") != null
                ? map.get("residenceAddress").toString() : "";
        String familyAddress = map.get("familyAddress") != null
                ? map.get("familyAddress").toString() : "";
        String examineeArchiveUnit = map.get("examineeArchiveUnit") != null
                ? map.get("examineeArchiveUnit").toString() : "";
        String examineeArchiveUnitAddress = map
                .get("examineeArchiveUnitAddress") != null
                        ? map.get("examineeArchiveUnitAddress").toString() : "";
        String examineeArchiveUnitZipcode = map
                .get("examineeArchiveUnitZipcode") != null
                        ? map.get("examineeArchiveUnitZipcode").toString() : "";
        String prestudyUnit = map.get("prestudyUnit") != null
                ? map.get("prestudyUnit").toString() : "";
        String experience = map.get("experience") != null
                ? map.get("experience").toString() : "";
        String familyMember = map.get("familyMember") != null
                ? map.get("familyMember").toString() : "";
        String emergencyContact = map.get("emergencyContact") != null
                ? map.get("emergencyContact").toString() : "";
        String wechat = map.get("wechat") != null ? map.get("wechat").toString()
                : "";
        String phone = map.get("phone") != null ? map.get("phone").toString()
                : "";
        String mobile = map.get("mobile") != null ? map.get("mobile").toString()
                : "";
        String email = map.get("email") != null ? map.get("email").toString()
                : "";

        // 非元数据学籍信息
        String photo = map.get("photo") != null ? map.get("photo").toString()
                : "";
        boolean isNew = Boolean.parseBoolean(map.get("IS_NEW").toString());
        // String checkinNo = map.get("checkinNo") != null ?
        // map.get("checkinNo").toString() :
        // "";
        boolean isAdmissionUnified = Boolean
                .parseBoolean(map.get("isAdmissionUnified").toString());
        short grade = map.get("grade") != null
                ? Short.parseShort(map.get("grade").toString()) : 0;
        short term = map.get("term") != null
                ? Short.parseShort(map.get("term").toString()) : 0;
        String feeShallPay = map.get("feeShallPay") != null
                ? map.get("feeShallPay").toString() : "";
        String feeTotal = map.get("feeTotal") != null
                ? map.get("feeTotal").toString() : "";
        String feeStandard = map.get("feeStandard") != null
                ? map.get("feeStandard").toString() : "";
        short feeTimes = map.get("feeTimes") != null
                ? Short.parseShort(map.get("feeTimes").toString()) : 0;
        String feeTotal2 = map.get("feeTotal2") != null
                ? map.get("feeTotal2").toString() : "";
        String fee1 = map.get("fee1") != null ? map.get("fee1").toString() : "";
        String fee2 = map.get("fee2") != null ? map.get("fee2").toString() : "";
        String fee3 = map.get("fee3") != null ? map.get("fee3").toString() : "";
        String fee4 = map.get("fee4") != null ? map.get("fee4").toString() : "";
        String fee5 = map.get("fee5") != null ? map.get("fee5").toString() : "";
        String domitory = map.get("domitory") != null
                ? map.get("domitory").toString() : "";
        String research = map.get("research") != null
                ? map.get("research").toString() : "";
        String los = map.get("los") != null ? map.get("los").toString() : "";
        String rank = map.get("rank") != null ? map.get("rank").toString() : "";
        String masterGrade = map.get("masterGrade") != null
                ? map.get("masterGrade").toString() : "";
        String awardAndPunishment = map.get("awardAndPunishment") != null
                ? map.get("awardAndPunishment").toString() : "";
        String specialPlanMemo = map.get("specialPlanMemo") != null
                ? map.get("specialPlanMemo").toString() : "";
        String directedWork = map.get("directedWork") != null
                ? map.get("directedWork").toString() : "";
        String jointTraining = map.get("jointTraining") != null
                ? map.get("jointTraining").toString() : "";
        String jointTrainingWork = map.get("jointTrainingWork") != null
                ? map.get("jointTrainingWork").toString() : "";
        String crossDiscipline = map.get("crossDiscipline") != null
                ? map.get("crossDiscipline").toString() : "";
        String absenceTerms = map.get("absenceTerms") != null
                ? map.get("absenceTerms").toString() : "";
        String duration = map.get("duration") != null
                ? map.get("duration").toString() : "";
        String delay = map.get("delay") != null ? map.get("delay").toString()
                : "";
        String grantedDegree = map.get("grantedDegree") != null
                ? map.get("grantedDegree").toString() : "";
        boolean diplomaStatus = Boolean
                .parseBoolean(map.get("diplomaStatus").toString());
        String diplomaNo = map.get("diplomaNo") != null
                ? map.get("diplomaNo").toString() : "";
        boolean leaveStatus = Boolean
                .parseBoolean(map.get("leaveStatus").toString());
        String leaveTo = map.get("leaveTo") != null
                ? map.get("leaveTo").toString() : "";
        boolean archiveStatus = Boolean
                .parseBoolean(map.get("archiveStatus").toString());

        // 非元数据来源信息
        String bachelorUnitName = map.get("bachelorUnitName") != null
                ? map.get("bachelorUnitName").toString() : "";
        String bachelorDisciplineName = map
                .get("bachelorDisciplineName") != null
                        ? map.get("bachelorDisciplineName").toString() : "";
        String bachelorNo = map.get("bachelorNo") != null
                ? map.get("bachelorNo").toString() : "";
        String undergradudateUnitName = map
                .get("undergradudateUnitName") != null
                        ? map.get("undergradudateUnitName").toString() : "";
        String undergradudateDisciplineName = map
                .get("undergradudateDisciplineName") != null
                        ? map.get("undergradudateDisciplineName").toString()
                        : "";
        String undergradudateNo = map.get("undergradudateNo") != null
                ? map.get("undergradudateNo").toString() : "";
        String masterUnitName = map.get("masterUnitName") != null
                ? map.get("masterUnitName").toString() : "";
        String masterDisciplineName = map.get("masterDisciplineName") != null
                ? map.get("masterDisciplineName").toString() : "";
        String masterNo = map.get("masterNo") != null
                ? map.get("masterNo").toString() : "";
        String postgraduateUnitName = map.get("postgraduateUnitName") != null
                ? map.get("postgraduateUnitName").toString() : "";
        String postgraduateDisciplineName = map
                .get("postgraduateDisciplineName") != null
                        ? map.get("postgraduateDisciplineName").toString() : "";
        String postgraduateNo = map.get("postgraduateNo") != null
                ? map.get("postgraduateNo").toString() : "";
        String finalSno = map.get("finalSno") != null
                ? map.get("finalSno").toString() : "";

        Student newStudent = null;
        if (sno != null) {
            newStudent = studentRepo.findBySno(sno);
        } else {
            newStudent = new Student();
        }

        if (newStudent == null) {
            LOG.error("save student fails for sno: " + sno);
            return false;
        }

        Object[][] fields = new Object[][] { { "ssnType", null, null },
                { "nation", null, null }, { "ethnic", null, null },
                { "gender", null, null }, { "marriage", null, null },
                { "religion", null, null },
                { "hometown", null, "edu.ecnu.yjsy.model.xj.Region" },
                { "birthplace", null, "edu.ecnu.yjsy.model.xj.Region" },
                { "residence", null, "edu.ecnu.yjsy.model.xj.Region" },
                { "family", null, "edu.ecnu.yjsy.model.xj.Region" },
                { "railway", null, null },
                { "examineeArchive", null, "edu.ecnu.yjsy.model.xj.Region" },
                { "prestudyUnitNature", null,
                        "edu.ecnu.yjsy.model.xj.PreStudy" },
                { "prestudy", null, "edu.ecnu.yjsy.model.xj.Region" },
                { "degreeLevel", null, null }, { "degreeType", null, null },
                { "studyMode", null, null }, { "campus", null, null },
                { "supervisor", null, "edu.ecnu.yjsy.model.Staff" },
                { "discipline", null, "edu.ecnu.yjsy.model.xj.Discipline" },
                { "unit", null, "edu.ecnu.yjsy.model.xj.Unit" },
                { "medical", null, null }, { "examination", null, null },
                { "specialPlan", null, null }, { "enrollment", null, null },
                { "directedRegion", null, "edu.ecnu.yjsy.model.xj.Region" },
                { "graduation", null, null }, { "examineeOrigin", null, null },
                { "bachelorUnit", null, "edu.ecnu.yjsy.model.xj.University" },
                { "bachelorDiscipline", null,
                        "edu.ecnu.yjsy.model.xj.BachelorDiscipline" },
                { "undergraduateUnit", null,
                        "edu.ecnu.yjsy.model.xj.University" },
                { "undergraduateDiscipline", null,
                        "edu.ecnu.yjsy.model.xj.BachelorDiscipline" },
                { "undergraduateMode", null,
                        "edu.ecnu.yjsy.model.xj.EducationMode" },
                { "masterUnit", null, "edu.ecnu.yjsy.model.xj.University" },
                { "masterDiscipline", null,
                        "edu.ecnu.yjsy.model.xj.Discipline" },
                { "masterMode", null, "edu.ecnu.yjsy.model.xj.EducationMode" },
                { "postgraduateUnit", null,
                        "edu.ecnu.yjsy.model.xj.University" },
                { "postgraduateDiscipline", null,
                        "edu.ecnu.yjsy.model.xj.Discipline" },
                { "postgraduateMode", null,
                        "edu.ecnu.yjsy.model.xj.EducationMode" },
                { "finalDegree", null, "edu.ecnu.yjsy.model.xj.Degree" },
                { "finalEducation", null, "edu.ecnu.yjsy.model.xj.Education" },
                { "finalEducationMode", null,
                        "edu.ecnu.yjsy.model.xj.EducationMode" }, };

        for (Object[] field : fields) {
            Long id = null;
            if (fields[1] != null) {
                id = (Long) field[1];
            }

            if (map.containsKey(field[0]))
                id = Long.parseLong(map.get(field[0]).toString());

            // 将首字母大写
            char[] chars = field[0].toString().toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            String className = new String(chars);

            // 获取参数对应的entity类
            @SuppressWarnings("rawtypes")
            Class entityClass = null;
            String actualClassName = "edu.ecnu.yjsy.model.xj." + className;
            if (field[2] != null) {
                actualClassName = field[2].toString();
            }
            try {
                entityClass = Class.forName(actualClassName);
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }

            // 如果前端传了ID值，设置参数entity对应的ID
            EntityId entity = null;
            if (id != null) {
                try {
                    entity = (EntityId) entityClass.newInstance();
                    entity.setId(id);
                } catch (InstantiationException | IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            try {
                Method method = Student.class.getMethod("set" + className,
                        entityClass);
                method.invoke(newStudent, entity);// 調用Student類的set方法
            } catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // 基本信息
        newStudent.setName(name);
        newStudent.setNameEn(nameEn);
        newStudent.setExamineeNo(examineeNo);
        newStudent.setExamineeSignPlace(examineeSign);
        newStudent.setSsn(ssn);
        newStudent.setBirthdate(birthdayDate);
        newStudent.setMilitaryJoinNo(militaryJoinNo);
        newStudent.setMilitaryQuitNo(militaryQuitNo);
        newStudent.setResidenceAddress(residenceAddress);
        newStudent.setFamilyAddress(familyAddress);
        newStudent.setExamineeArchiveUnit(examineeArchiveUnit);
        newStudent.setExamineeArchiveUnitAddress(examineeArchiveUnitAddress);
        newStudent.setExamineeArchiveUnitZipcode(examineeArchiveUnitZipcode);
        newStudent.setPrestudyUnit(prestudyUnit);
        newStudent.setExperience(experience);
        newStudent.setFamilyMember(familyMember);
        newStudent.setEmergencyContact(emergencyContact);
        newStudent.setWeichat(wechat);
        newStudent.setPhone(phone);
        newStudent.setMobile(mobile);
        newStudent.setEmail(email);
        // 学籍信息
        newStudent.setSno(sno);
        newStudent.setPhoto(photo);
        newStudent.setNew(isNew);
        newStudent.setAdmissionUnified(isAdmissionUnified);
        newStudent.setAdmissionDate(admissionDate);
        newStudent.setGrade(grade);
        newStudent.setTerm(term);
        newStudent.setFeeShallPay(feeShallPay);
        newStudent.setFeeTotal(feeTotal);
        newStudent.setFeeStandard(feeStandard);
        newStudent.setFeeTimes(feeTimes);
        newStudent.setFeeTotal2(feeTotal2);
        newStudent.setFee1(fee1);
        newStudent.setFee2(fee2);
        newStudent.setFee3(fee3);
        newStudent.setFee4(fee4);
        newStudent.setFee5(fee5);
        newStudent.setDomitory(domitory);
        newStudent.setResearch(research);
        newStudent.setLos(los);
        newStudent.setRank(rank);
        newStudent.setMasterGrade(masterGrade);
        newStudent.setAwardAndPunishment(awardAndPunishment);
        newStudent.setSpecialPlanMemo(specialPlanMemo);
        newStudent.setDirectedWork(directedWork);
        newStudent.setJointTraining(jointTraining);
        newStudent.setJointTrainingWork(jointTrainingWork);
        newStudent.setCrossDiscipline(crossDiscipline);
        newStudent.setExpectedGraduationDate(expectedGraduationDate);
        newStudent.setActualGraduationDate(actualGraduationDate);
        newStudent.setAbsenceTerms(absenceTerms);
        newStudent.setDuration(duration);
        newStudent.setDelay(delay);
        newStudent.setPregraduationDate(pregraduationDate);
        newStudent.setGrantedDegree(grantedDegree);
        newStudent.setGrantedDegreeDate(grantedDegreeDate);
        newStudent.setDiplomaStatus(diplomaStatus);
        newStudent.setDiplomaNo(diplomaNo);
        newStudent.setLeave(leaveStatus);
        newStudent.setLeaveTo(leaveTo);
        newStudent.setLeaveDate(leaveDate);
        newStudent.setArchiveSatus(archiveStatus);
        newStudent.setArchiveDate(archiveDate);
        // 来源信息
        newStudent.setBachelorUnitName(bachelorUnitName);
        newStudent.setBachelorDisciplineName(bachelorDisciplineName);
        newStudent.setBachelorDate(bachelorDate);
        newStudent.setBachelorNo(bachelorNo);
        newStudent.setUndergraduateUnitName(undergradudateUnitName);
        newStudent.setUndergraduateDisciplineName(undergradudateDisciplineName);
        newStudent.setUndergraduateDate(undergradudateDate);
        newStudent.setUndergraduateNo(undergradudateNo);
        newStudent.setMasterUnitName(masterUnitName);
        newStudent.setMasterDisciplineName(masterDisciplineName);
        newStudent.setMasterDate(masterDate);
        newStudent.setMasterNo(masterNo);
        newStudent.setPostgraduateUnitName(postgraduateUnitName);
        newStudent.setPostgraduateDisciplineName(postgraduateDisciplineName);
        newStudent.setPostgraduateDate(postgraduateDate);
        newStudent.setPostgraduateNo(postgraduateNo);
        newStudent.setFinalSno(finalSno);
        newStudent.setFinalEducationDate(finalEducationDate);

        studentRepo.save(newStudent);

        // 创建学生帐号
        accountService.createStudentAccount(newStudent);

        return true;
    }

    public Map<String, Object> upload(InputStream data, String fileType) {
        try {
            return util.load(data, fileType);
        } catch (Exception e) {
            LOG.error("{}", e);
            return null;
        }
    }

    public Map<String, String> imageUpload(InputStream data, String sno,
            String grade) throws IOException {
        Map<String, String> s = new HashMap<String, String>();
        Student student = studentRepo.findBySno(sno);
        if (student.getPhoto() != null) {
            ClassPathResource tempResource = new ClassPathResource(
                    student.getPhoto());
            File tempFile = new File(tempResource.getPath());
            if (tempFile.exists()) {
                tempFile.delete();
                delEmptyPath(tempFile.getParent());
            }
        }
        ClassPathResource resource = new ClassPathResource(
                "/src/main/resources/images");
        File file = new File(resource.getPath() + "/" + grade);
        if (!file.exists()) file.mkdirs();
        FileOutputStream outputStream = new FileOutputStream(
                file + "/" + sno + ".jpg");
        int ch = data.read();
        while (ch != -1) {
            outputStream.write(ch);
            ch = data.read();
        }
        outputStream.close();
        String path = "/images/" + grade + "/" + sno + ".jpg";
        student.setPhoto(path);
        studentRepo.save(student);
        s.put("image1", path);
        System.out.println("图片存储成功！");
        return s;
    }

    public void delEmptyPath(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if ((files != null && files.length > 0)
                    || file.equals(new File("images")))
                return;
            else {
                file.delete();
                delEmptyPath(file.getParent());
            }
        }
    }

    public Student getStudentBySno(String sno) {
        Student student = studentRepo.findBySno(sno);
        return student;
    }

    public boolean batchUpdate(InputStream data) {
        try {
            Map<String, String> items = new HashMap<String, String>();
            items.put("年级", "grade");
            items.put("性别", "gender");
            items.put("出生日期", "birthday");
            List<String> attributes = new ArrayList<String>();
            Workbook workbook = createWorkbook(data);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            for (int i = 1; i < row.getLastCellNum(); i++) {
                for (String key : items.keySet()) {
                    if (row.getCell(i).toString().equals(key)) {
                        attributes.add(items.get(key));
                    }
                }
            }

            List<Student> students = new ArrayList<Student>();
            for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                Row r = sheet.getRow(j);
                Student student = studentRepo.findOne(Long.parseLong(
                        DF.format(r.getCell(0).getNumericCellValue())));
                for (int k = 0; k < attributes.size(); k++) {
                    Field field = Student.class
                            .getDeclaredField(attributes.get(k));
                    field.setAccessible(true);
                    getCellValue(field, r.getCell(k + 1), student);
                    students.add(student);
                }
            }
            studentRepo.save(students);
            return true;
        } catch (Exception e) {
            LOG.error("{}", e);
            return false;
        }
    }

    public void getCellValue(Field field, Cell cell, Student student)
            throws NumberFormatException, IllegalArgumentException,
            IllegalAccessException, ParseException {
        if (field.getType() == short.class) {
            field.set(student,
                    Short.parseShort(DF.format(cell.getNumericCellValue())));
        } else if (field.getType() == float.class) {
            field.set(student,
                    Float.parseFloat(DF.format(cell.getNumericCellValue())));
        } else if (field.getType() == String.class) {
            field.set(student, cell.toString());
        } else if (field.getType() == Gender.class) {
            field.set(student, genderRepo.findByName(cell.toString()));
        } else if (field.getType() == Date.class) {
            Date date = new Date(FMT_NYR
                    .parse(FMT_NYR.format(cell.getDateCellValue())).getTime());
            field.set(student, date);
        }
    }

}