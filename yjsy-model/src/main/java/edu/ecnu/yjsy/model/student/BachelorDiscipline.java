package edu.ecnu.yjsy.model.student;

import static edu.ecnu.yjsy.model.constant.Column.META_CATEGORY;
import static edu.ecnu.yjsy.model.constant.Column.META_CATEGORY_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_MAJOR;
import static edu.ecnu.yjsy.model.constant.Column.META_MAJOR_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_MINOR;
import static edu.ecnu.yjsy.model.constant.Column.META_MINOR_CODE;
import static edu.ecnu.yjsy.model.constant.Table.BACHELOR_DISCIPLINE;

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
@Table(name = BACHELOR_DISCIPLINE,
        uniqueConstraints = { @UniqueConstraint(columnNames = { META_MINOR_CODE }) })
public class BachelorDiscipline extends EntityId {

    /**
     * 
     */
    private static final long serialVersionUID = 5729005029709014390L;

    // 门类 - 13大类 - 每个包括 N 个一级学科
    @Column(name = META_CATEGORY, nullable = false, length = 40)
    private String category;

    @Column(name = META_CATEGORY_CODE, nullable = false, length = 10)
    private String categoryCode;

    // 一级学科 - 每个包括 M 个专业
    @Column(name = META_MAJOR, length = 40)
    private String major;

    @Column(name = META_MAJOR_CODE, length = 10)
    private String majorCode;

    // 二级学科(专业) -
    @Column(name = META_MINOR, nullable = false, length = 40)
    private String minor;

    @Column(name = META_MINOR_CODE, nullable = false, length = 10)
    private String minorCode;

    // ---------------------

    public BachelorDiscipline() {

    }

    public BachelorDiscipline(String categoryCode, String category,
            String majorCode, String major, String minorCode, String minor) {
        this.categoryCode = categoryCode;
        this.category = category;
        this.majorCode = majorCode;
        this.major = major;
        this.minorCode = minorCode;
        this.minor = minor;
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
    
    @Override
    public String toString() {
        return category + major + minor;
    }

}