package edu.ecnu.yjsy.model.student;

import static edu.ecnu.yjsy.model.constant.Column.META_AREA;
import static edu.ecnu.yjsy.model.constant.Column.META_CATEGORY;
import static edu.ecnu.yjsy.model.constant.Column.META_CATEGORY_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_DEGREE_LEVEL;
import static edu.ecnu.yjsy.model.constant.Column.META_DEGREE_TYPE;
import static edu.ecnu.yjsy.model.constant.Column.META_MAJOR;
import static edu.ecnu.yjsy.model.constant.Column.META_MAJOR_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_MINOR;
import static edu.ecnu.yjsy.model.constant.Column.META_MINOR_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_NATURE;
import static edu.ecnu.yjsy.model.constant.Column.META_ORIGIN_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_SINCE;
import static edu.ecnu.yjsy.model.constant.Column.META_SOURCE;
import static edu.ecnu.yjsy.model.constant.Column.META_TARGET;
import static edu.ecnu.yjsy.model.constant.Column.META_UNTIL;
import static edu.ecnu.yjsy.model.constant.Table.DISCIPLINE;

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
@Table(name = DISCIPLINE, uniqueConstraints = {
        @UniqueConstraint(columnNames = { META_MINOR_CODE }) })
public class Discipline extends EntityId {

    /**
     * 
     */
    private static final long serialVersionUID = 6572594572318892844L;

    // 门类 - 13大类 - 每个包括 N 个一级学科
    @Column(name = META_CATEGORY, nullable = false, length = 40)
    private String category;

    @Column(name = META_CATEGORY_CODE, nullable = false, length = 10)
    private String categoryCode;

    // 一级学科 - 每个包括 M 个专业
    @Column(name = META_MAJOR, nullable = false, length = 40)
    private String major;

    @Column(name = META_MAJOR_CODE, nullable = false, length = 10)
    private String majorCode;

    // 二级学科(专业) -
    @Column(name = META_MINOR, nullable = false, length = 40)
    private String minor;

    @Column(name = META_MINOR_CODE, nullable = false, length = 10)
    private String minorCode;

    // 原专业代码
    @Column(name = META_ORIGIN_CODE, length = 10)
    private String originCode;

    // 层次 - 博士 硕士 两者皆可
    @Column(name = META_DEGREE_LEVEL, length = 2)
    private String degreeLevel;

    // 学位类型
    @Column(name = META_DEGREE_TYPE, length = 1)
    private String degreeType;

    // 专业设置区域
    @Column(name = META_AREA, length = 2)
    private String area;

    // 性质 - 国家标准 华师大自设
    @Column(name = META_NATURE, length = 10)
    private String nature;

    // 启用年份
    @Column(name = META_SINCE, nullable = false, length = 4)
    private String since;

    // 失效年份
    @Column(name = META_UNTIL, length = 4)
    private String until;

    // 专业代码来源
    @Column(name = META_SOURCE, length = 10)
    private String source;

    // 专业代码去向
    @Column(name = META_TARGET, length = 10)
    private String target;

    // ---------------------

    public Discipline() {

    }

    public Discipline(String categoryCode, String category, String majorCode,
            String major, String minorCode, String minor, String originCode,
            String degreeType, String degreeLevel, String area, String nature,
            String since, String until, String source, String target) {
        this.categoryCode = categoryCode;
        this.category = category;
        this.majorCode = majorCode;
        this.major = major;
        this.minorCode = minorCode;
        this.minor = minor;
        this.originCode = originCode;
        this.degreeLevel = degreeLevel;
        this.degreeType = degreeType;
        this.area = area;
        this.nature = nature;
        this.since = since;
        this.until = until;
        this.source = source;
        this.target = target;
    }

    // ---------------------

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getMinorCode() {
        return minorCode;
    }

    public void setMinorCode(String minorCode) {
        this.minorCode = minorCode;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getDegreeLevel() {
        return degreeLevel;
    }

    public void setDegreeLevel(String degreeLevel) {
        this.degreeLevel = degreeLevel;
    }

    public String getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(String degreeType) {
        this.degreeType = degreeType;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
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
        return category + major + minor;
    }

}
