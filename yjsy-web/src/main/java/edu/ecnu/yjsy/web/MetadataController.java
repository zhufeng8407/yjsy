package edu.ecnu.yjsy.web;

import static edu.ecnu.yjsy.constant.Constant.API_METADATA;
import static edu.ecnu.yjsy.constant.Constant.PAGE_PAGE;
import static edu.ecnu.yjsy.constant.Constant.PAGE_SIZE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ecnu.yjsy.model.change.StatusChangeType;
import edu.ecnu.yjsy.model.change.StatusChangeTypeCombine;
import edu.ecnu.yjsy.model.staff.Staff;
import edu.ecnu.yjsy.model.student.BachelorDiscipline;
import edu.ecnu.yjsy.model.student.Campus;
import edu.ecnu.yjsy.model.student.Degree;
import edu.ecnu.yjsy.model.student.DegreeLevel;
import edu.ecnu.yjsy.model.student.DegreeType;
import edu.ecnu.yjsy.model.student.Discipline;
import edu.ecnu.yjsy.model.student.Education;
import edu.ecnu.yjsy.model.student.EducationMode;
import edu.ecnu.yjsy.model.student.Enrollment;
import edu.ecnu.yjsy.model.student.Ethnic;
import edu.ecnu.yjsy.model.student.Examination;
import edu.ecnu.yjsy.model.student.ExamineeOrigin;
import edu.ecnu.yjsy.model.student.Gender;
import edu.ecnu.yjsy.model.student.Graduation;
import edu.ecnu.yjsy.model.student.Marriage;
import edu.ecnu.yjsy.model.student.Medical;
import edu.ecnu.yjsy.model.student.Military;
import edu.ecnu.yjsy.model.student.Nation;
import edu.ecnu.yjsy.model.student.PreStudy;
import edu.ecnu.yjsy.model.student.Railway;
import edu.ecnu.yjsy.model.student.Region;
import edu.ecnu.yjsy.model.student.Religion;
import edu.ecnu.yjsy.model.student.SpecialPlan;
import edu.ecnu.yjsy.model.student.SsnType;
import edu.ecnu.yjsy.model.student.Unit;
import edu.ecnu.yjsy.model.student.University;
import edu.ecnu.yjsy.service.MetadataService;

@RestController
@RequestMapping(API_METADATA)
public class MetadataController {

    private static final Sort SORT_ID_DESC = new Sort(Direction.ASC, "id");

    @Autowired
    private MetadataService service;

//    @RequestMapping(value = "/staff", method = GET)
//    @ResponseBody
//    public List<Staff> getStaff() {
//        return service.getStaff();
//    }

    @RequestMapping(value = "/api-mapping", method = GET)
    @ResponseBody
    public List<String> getAPIMapping() {
        return service.getAPIMapping();
    }

    @RequestMapping(value = "/ssn-type", method = GET)
    @ResponseBody
    public List<SsnType> getSsnType() {
        return service.getSsnType();
    }

    @RequestMapping(value = "/ssn-type-by-page", method = GET)
    @ResponseBody
    public Page<SsnType> getSsnType(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getSsnType(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/nation", method = GET)
    @ResponseBody
    public List<Nation> getNation() {
        return service.getNation();
    }

    @RequestMapping(value = "/nation-by-page", method = GET)
    @ResponseBody
    public Page<Nation> getNation(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getNation(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/ethnic", method = GET)
    @ResponseBody
    public List<Ethnic> getEthnic() {
        return service.getEthnic();
    }

    @RequestMapping(value = "/ethnic-by-page", method = GET)
    @ResponseBody
    public Page<Ethnic> getEthnic(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getEthnic(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/gender", method = GET)
    @ResponseBody
    public List<Gender> getGender() {
        return service.getGender();
    }

    @RequestMapping(value = "/gender-by-page", method = GET)
    @ResponseBody
    public Page<Gender> getGender(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getGender(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/marriage", method = GET)
    @ResponseBody
    public List<Marriage> getMarriage() {
        return service.getMarriage();
    }

    @RequestMapping(value = "/marriage-by-page", method = GET)
    @ResponseBody
    public Page<Marriage> getMarriage(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getMarriage(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/military", method = GET)
    @ResponseBody
    public List<Military> getMilitary() {
        return service.getMilitary();
    }

    @RequestMapping(value = "/military-by-page", method = GET)
    @ResponseBody
    public Page<Military> getMilitary(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getMilitary(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/religion", method = GET)
    @ResponseBody
    public List<Religion> getReligion() {
        return service.getReligion();
    }

    @RequestMapping(value = "/religion-by-page", method = GET)
    @ResponseBody
    public Page<Religion> getReligion(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getReligion(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/region-state", method = GET)
    @ResponseBody
    public List<Region> getRegionState() {
        return service.getRegionState();
    }

    @RequestMapping(value = "/region-city", method = GET)
    @ResponseBody
    public List<Region> getRegionCity(@RequestParam String stateCode) {
        return service.getRegionCity(stateCode);
    }

    @RequestMapping(value = "/region-county", method = GET)
    @ResponseBody
    public List<Region> getRegionCounty(@RequestParam String cityCode) {
        return service.getRegionCounty(cityCode);
    }

    @RequestMapping(value = "/region-by-page", method = GET)
    @ResponseBody
    public Page<Region> getRegion(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getRegion(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/railway-state", method = GET)
    @ResponseBody
    public List<Railway> getRailwayState() {
        List<Railway> list = service.getRailwayState();
        return list;
    }

    @RequestMapping(value = "/railway-station", method = GET)
    @ResponseBody
    public List<Railway> getRailwayStation(@RequestParam String state) {
        return service.getRailwayStation(state);
    }

    @RequestMapping(value = "/railway-by-page", method = GET)
    @ResponseBody
    public Page<Railway> getRailway(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getRailway(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/prestudy", method = GET)
    @ResponseBody
    public List<PreStudy> getPrestudy() {
        return service.getPrestudy();
    }

    @RequestMapping(value = "/prestudy-by-page", method = GET)
    @ResponseBody
    public Page<PreStudy> getPrestudy(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getPrestudy(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/degree-level", method = GET)
    @ResponseBody
    public List<DegreeLevel> getDegreeLevel() {
        return service.getDegreeLevel();
    }

    @RequestMapping(value = "/degree-level-by-page", method = GET)
    @ResponseBody
    public Page<DegreeLevel> getDegreeLevel(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service
                .getDegreeLevel(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/degree-type", method = GET)
    @ResponseBody
    public List<DegreeType> getDegreeType() {
        return service.getDegreeType();
    }

    @RequestMapping(value = "/degree-type-by-page", method = GET)
    @ResponseBody
    public Page<DegreeType> getDegreeType(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getDegreeType(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/campus", method = GET)
    @ResponseBody
    public List<Campus> getCampus() {
        return service.getCampus();
    }

    @RequestMapping(value = "/campus-by-page", method = GET)
    @ResponseBody
    public Page<Campus> getCampus(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getCampus(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/unit", method = GET)
    @ResponseBody
    public List<Unit> getUnit() {
        return service.getUnit();
    }

    @RequestMapping(value = "/unit-school", method = GET)
    @ResponseBody
    public List<Unit> getUnitSchool() {
        return service.getUnitSchool();
    }

    @RequestMapping(value = "/unit-department", method = GET)
    @ResponseBody
    public List<Unit> getUnitDepartment(@RequestParam String schoolCode) {
        return service.getUnitDepartment(schoolCode);
    }

    @RequestMapping(value = "/unit-by-page", method = GET)
    @ResponseBody
    public Page<Unit> getUnit(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getUnit(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/medical", method = GET)
    @ResponseBody
    public List<Medical> getMedical() {
        return service.getMedical();
    }

    @RequestMapping(value = "/medical-by-page", method = GET)
    @ResponseBody
    public Page<Medical> getMedical(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getMedical(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/examination", method = GET)
    @ResponseBody
    public List<Examination> getExamination() {
        return service.getExamination();
    }

    @RequestMapping(value = "/examination-by-page", method = GET)
    @ResponseBody
    public Page<Examination> getExamination(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service
                .getExamination(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/special-plan", method = GET)
    @ResponseBody
    public List<SpecialPlan> getSpecialPlan() {
        return service.getSpecialPlan();
    }

    @RequestMapping(value = "/special-plan-by-page", method = GET)
    @ResponseBody
    public Page<SpecialPlan> getSpecialPlan(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service
                .getSpecialPlan(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/enrollment", method = GET)
    @ResponseBody
    public List<Enrollment> getEnrollment() {
        return service.getEnrollment();
    }

    @RequestMapping(value = "/enrollment-by-page", method = GET)
    @ResponseBody
    public Page<Enrollment> getEnrollment(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getEnrollment(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/graduation", method = GET)
    @ResponseBody
    public List<Graduation> getGraduation() {
        return service.getGraduation();
    }

    @RequestMapping(value = "/graduation-by-page", method = GET)
    @ResponseBody
    public Page<Graduation> getGraduation(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getGraduation(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/examinee-origin", method = GET)
    @ResponseBody
    public List<ExamineeOrigin> getExamineeOrigin() {
        return service.getExamineeOrigin();
    }

    @RequestMapping(value = "/examinee-origin-by-page", method = GET)
    @ResponseBody
    public Page<ExamineeOrigin> getExamineeOrigin(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service
                .getExamineeOrigin(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/university", method = GET)
    @ResponseBody
    public List<University> getUniversity() {
        return service.getUniversity();
    }

    @RequestMapping(value = "/university-by-page", method = GET)
    @ResponseBody
    public Page<University> getUniversityByPage(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getUniversity(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/universityIdName", method = GET)
    @ResponseBody
    public Map<String, Object> getUniversityIdName(
            @RequestParam String bachelorUnitName) {
        return service.getUniversityIdName(bachelorUnitName);
    }

    @RequestMapping(value = "/discipline-by-page", method = GET)
    @ResponseBody
    public Page<Discipline> getDisciplineByPage(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service
                .getDisciplineByPage(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/discipline-category", method = GET)
    @ResponseBody
    public List<Discipline> getDisciplineCategory() {
        return service.getDisciplineCategory();
    }

    @RequestMapping(value = "/discipline-major", method = GET)
    @ResponseBody
    public List<Discipline> getDisciplineMajor(String categoryCode) {
        return service.getDisciplineMajor(categoryCode);
    }

    @RequestMapping(value = "/discipline-minor", method = GET)
    @ResponseBody
    public List<Discipline> getDisciplineMinor(String majorCode) {
        return service.getDisciplineMinor(majorCode);
    }

    @RequestMapping(value = "/bachelor-discipline", method = GET)
    @ResponseBody
    public List<BachelorDiscipline> getBachelorDiscipline() {
        return service.getBachelorCategory();
    }

    @RequestMapping(value = "/bachelor-discipline-major", method = GET)
    @ResponseBody
    public List<BachelorDiscipline> getBachelorDisciplineMajor(
            @RequestParam String categoryCode) {
        return service.getBachelorMajor(categoryCode);
    }

    @RequestMapping(value = "/bachelor-discipline-minor", method = GET)
    @ResponseBody
    public List<BachelorDiscipline> getBachelorDisciplineMinor(
            @RequestParam String majorCode) {
        return service.getBachelorMinor(majorCode);
    }

    @RequestMapping(value = "/bachelor-discipline-by-page", method = GET)
    @ResponseBody
    public Page<BachelorDiscipline> getBachelorDisciplineByPage(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getBachelorDiscipline(
                new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/education-mode", method = GET)
    @ResponseBody
    public List<EducationMode> getEducationMode() {
        return service.getEducationMode();
    }

    @RequestMapping(value = "/education-mode-by-page", method = GET)
    @ResponseBody
    public Page<EducationMode> getEducationMode(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service
                .getEducationMode(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/degree", method = GET)
    @ResponseBody
    public List<Degree> getDegree() {
        return service.getDegree();
    }

    @RequestMapping(value = "/degree-by-page", method = GET)
    @ResponseBody
    public Page<Degree> getDegree(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getDegree(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/education", method = GET)
    @ResponseBody
    public List<Education> getEducation() {
        return service.getEducation();
    }

    @RequestMapping(value = "/education-by-page", method = GET)
    @ResponseBody
    public Page<Education> getEducation(
            @RequestParam(value = "page",
                    defaultValue = PAGE_PAGE) Integer page,
            @RequestParam(value = "size",
                    defaultValue = PAGE_SIZE) Integer size) {
        return service.getEducation(new PageRequest(page, size, SORT_ID_DESC));
    }

    @RequestMapping(value = "/status-change-type", method = GET)
    @ResponseBody
    public List<StatusChangeType> statusChangeType() {
        return service.getStatusChangeType();
    }

    @RequestMapping(value = "/status-change-type-combine", method = GET)
    @ResponseBody
    public List<StatusChangeTypeCombine> statusChangeTypeCombine() {
        return service.getStatusChangeTypeCombine();
    }

    @RequestMapping(value = "/status-change-type-by-minor", method = GET)
    @ResponseBody
    public StatusChangeType statusChangeTypeByMinor(
            @RequestParam String minor) {
        return service.getStatusChangeTypeByMinor(minor);
    }

    @RequestMapping(value = "/status-change-type-major", method = GET)
    @ResponseBody
    public List<List<String>> statusChangeTypeMajor() {
        return service.getMajorCodeAndName();
    }

    @RequestMapping(value = "/status-change-type-minor-by-major", method = GET)
    @ResponseBody
    public List<List<String>> statusChangeTypeMinorByMajor(
            @RequestParam String major) {
        return service.getMinorCodeAndName(major);
    }

    @RequestMapping(value = "/status-change-type-save", method = POST)
    @ResponseBody
    public Map<String, String> saveStatusChangeType(
            @RequestParam String statusChangeType) {
        return service.saveStatusChangeType(statusChangeType);
    }

    @RequestMapping(value = "/image", method = GET)
    @ResponseBody
    public byte[] getImage(@RequestParam String imagePath) {
        return service.getImage(imagePath);
    }

}