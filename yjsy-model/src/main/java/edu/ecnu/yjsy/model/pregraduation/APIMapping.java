package edu.ecnu.yjsy.model.pregraduation;

import static edu.ecnu.yjsy.model.constant.Column.META_CLASSfIELD;
import static edu.ecnu.yjsy.model.constant.Column.META_DISPLAY_FORMAT;
import static edu.ecnu.yjsy.model.constant.Column.META_FIELD;
import static edu.ecnu.yjsy.model.constant.Column.META_FRONTAPI;
import static edu.ecnu.yjsy.model.constant.Column.MODIFY_TYPE;
import static edu.ecnu.yjsy.model.constant.Column.NOTES;
import static edu.ecnu.yjsy.model.constant.Table.APIMAPPING;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import edu.ecnu.yjsy.model.EntityId;

/**
 * 预毕业学籍字段修改状态的元数据
 * 
 * @author PYH
 */
@Entity
@Table(name = APIMAPPING,
        uniqueConstraints = @UniqueConstraint(columnNames = { META_FIELD }))
public class APIMapping extends EntityId {

    private static final long serialVersionUID = -3462138333098552389L;

    // 字段中文名 eg1：姓名 eg2：民族
    @Column(nullable = false, length = 50, name = META_FIELD)
    private String field;

    // 字段名称 eg1：Name eg2：Ethnic
    @Column(length = 50, name = META_CLASSfIELD)
    private String classField;

    // 前端获取字段内容的函数名 eg1:null eg2：getEthnic
    @Column(length = 50, name = META_FRONTAPI)
    private String frontGetApi;

    // 字段的前端显示类型(input select redio date)
    @Column(length = 10, name = META_DISPLAY_FORMAT)
    private String displayFormat;
    
    @Column(name = MODIFY_TYPE)
    private PregraduateModifyType modifyType;
    
    // 备注
    @Column(length = 100, name = NOTES)
    private String notes;

    public APIMapping() {}

    public APIMapping(String field, String classField, String frontGetApi,
            String displayFormat) {
        super();
        this.field = field;
        this.classField = classField;
        this.frontGetApi = frontGetApi;
        this.displayFormat = displayFormat;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getClassField() {
        return classField;
    }

    public void setClassField(String classField) {
        this.classField = classField;
    }

    public String getFrontGetApi() {
        return frontGetApi;
    }

    public void setFrontGetApi(String frontGetApi) {
        this.frontGetApi = frontGetApi;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }

    public PregraduateModifyType getModifyType() {
        return modifyType;
    }

    public void setModifyType(PregraduateModifyType modifyType) {
        this.modifyType = modifyType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
