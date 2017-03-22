package edu.ecnu.yjsy.model.student;

import static edu.ecnu.yjsy.model.constant.Column.META_DEPARTMENT;
import static edu.ecnu.yjsy.model.constant.Column.META_DEPARTMENT_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_DEPARTMENT_TYPE;
import static edu.ecnu.yjsy.model.constant.Column.META_DIVISION;
import static edu.ecnu.yjsy.model.constant.Column.META_DIVISION_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_SCHOOL;
import static edu.ecnu.yjsy.model.constant.Column.META_SCHOOL_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_SCHOOL_TYPE;
import static edu.ecnu.yjsy.model.constant.Column.META_SINCE;
import static edu.ecnu.yjsy.model.constant.Column.META_SOURCE;
import static edu.ecnu.yjsy.model.constant.Column.META_TARGET;
import static edu.ecnu.yjsy.model.constant.Column.META_UNTIL;
import static edu.ecnu.yjsy.model.constant.Table.UNIT;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import edu.ecnu.yjsy.model.EntityId;

/**
 * @author songshubin
 * @author xiafan
 * @author xulinhao
 */

@Entity
@Table(name = UNIT,
        uniqueConstraints = { @UniqueConstraint(
                columnNames = { META_DIVISION, META_SCHOOL, META_DEPARTMENT }),
                @UniqueConstraint(columnNames = { META_DIVISION_CODE,
                        META_SCHOOL_CODE, META_DEPARTMENT_CODE }) })
public class Unit extends EntityId {

    /**
     * 
     */
    private static final long serialVersionUID = 7017287281915794672L;

    // 校级单位
    @Column(name = META_DIVISION, nullable = false, length = 40)
    private String division;

    @Column(name = META_DIVISION_CODE, nullable = false, length = 10)
    private String divisionCode;

    // 一级单位 - 部院
    @Column(name = META_SCHOOL, length = 80)
    private String school;

    @Column(name = META_SCHOOL_CODE, length = 10)
    private String schoolCode;

    // 一级单位类别 - 实体 或 虚体
    @Column(name = META_SCHOOL_TYPE, length = 10)
    private String schoolType;

    // 二级单位 - 院系
    @Column(name = META_DEPARTMENT, length = 80)
    private String department;

    @Column(name = META_DEPARTMENT_CODE, length = 10)
    private String departmentCode;

    // 二级单位类别 - 实体 或 虚体
    @Column(name = META_DEPARTMENT_TYPE, length = 10)
    private String departmentType;

    // 启用年份
    @Column(name = META_SINCE, nullable = false, length = 4)
    private String since;

    // 失效年份
    @Column(name = META_UNTIL, length = 4)
    private String until;

    // 单位代码来源
    @Column(name = META_SOURCE, length = 10)
    private String source;

    // 单位代码去向
    @Column(name = META_TARGET, length = 10)
    private String target;

    // --------------------

    public Unit() {

    }

    public Unit(String division, String divisionCode) {
        this.division = division;
        this.divisionCode = divisionCode;
    }

    public Unit(String division, String divisionCode, String school,
            String schoolCode, String schoolType, String department,
            String departmentCode, String departmentType, String since,
            String until, String source, String target) {
        this.division = division;
        this.divisionCode = divisionCode;
        this.school = school;
        this.schoolCode = schoolCode;
        this.schoolType = schoolType;
        this.department = department;
        this.departmentCode = departmentCode;
        this.departmentType = departmentType;
        this.since = since;
        this.until = until;
        this.source = source;
        this.target = target;
    }

    // --------------------

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDivisionCode() {
        return divisionCode;
    }

    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(String departmentType) {
        this.departmentType = departmentType;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return school + department;
    }

}
