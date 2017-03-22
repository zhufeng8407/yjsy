package edu.ecnu.yjsy.model.student;

import static edu.ecnu.yjsy.model.constant.Column.META_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_NAME;
import static edu.ecnu.yjsy.model.constant.Column.META_NATURE;
import static edu.ecnu.yjsy.model.constant.Column.META_NATURE_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_OPERATION_1;
import static edu.ecnu.yjsy.model.constant.Column.META_OPERATION_2;
import static edu.ecnu.yjsy.model.constant.Column.META_OPERATION_3;
import static edu.ecnu.yjsy.model.constant.Column.META_OPERATION_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_SF_211;
import static edu.ecnu.yjsy.model.constant.Column.META_SF_985;
import static edu.ecnu.yjsy.model.constant.Column.META_STATE;
import static edu.ecnu.yjsy.model.constant.Column.META_STATE_CODE;
import static edu.ecnu.yjsy.model.constant.Table.UNIVERSITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import edu.ecnu.yjsy.model.MetaCode;

/**
 * @author songshubin
 * @author xiafan
 * @author xulinhao
 */

@Entity
@Table(name = UNIVERSITY,
        uniqueConstraints = { @UniqueConstraint(columnNames = { META_CODE }),
                @UniqueConstraint(columnNames = { META_NAME }) })
public class University extends MetaCode {

    // 院校代码 - code

    // 院校名称 - name

    /**
     * 
     */
    private static final long serialVersionUID = -480685718311898712L;

    // 省市代码
    @Column(name = META_STATE_CODE, nullable = false, length = 4)
    private String stateCode;

    // 省市名称
    @Column(name = META_STATE, nullable = false, length = 40)
    private String state;

    // 办学类型代码
    @Column(name = META_OPERATION_CODE, nullable = false, length = 4)
    private String operationCode;

    // 办学类型
    @Column(name = META_OPERATION_1, length = 20)
    private String operation1;

    @Column(name = META_OPERATION_2, length = 20)
    private String operation2;

    @Column(name = META_OPERATION_3, length = 20)
    private String operation3;

    // 性质类别名称
    @Column(name = META_NATURE, nullable = false, length = 20)
    private String nature;

    // 性质类别
    @Column(name = META_NATURE_CODE, nullable = false, length = 4)
    private String natureCode;

    // 是否985
    @Column(name = META_SF_985, nullable = false)
    private boolean is985;

    // 是否211
    @Column(name = META_SF_211, nullable = false)
    private boolean is211;

    // --------------------

    public University() {

    }

    public University(String code, String name, String stateCode, String state,
            String operationCode, String operation1, String operation2,
            String operation3, String natureCode, String nature, boolean is211,
            boolean is985) {
        this.code = code;
        this.name = name;
        this.stateCode = stateCode;
        this.state = state;
        this.operationCode = operationCode;
        this.operation1 = operation1;
        this.operation2 = operation2;
        this.operation3 = operation3;
        this.natureCode = natureCode;
        this.nature = nature;
        this.is985 = is985;
        this.is211 = is211;
    }

    // --------------------

    public University(long id, String name) {
        super.setId(id);
        this.name=name;
    }
    
    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public String getOperation1() {
        return operation1;
    }

    public void setOperation1(String operation1) {
        this.operation1 = operation1;
    }

    public String getOperation2() {
        return operation2;
    }

    public void setOperation2(String operation2) {
        this.operation2 = operation2;
    }

    public String getOperation3() {
        return operation3;
    }

    public void setOperation3(String operation3) {
        this.operation3 = operation3;
    }

    public String getNatureCode() {
        return natureCode;
    }

    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public boolean isIs985() {
        return is985;
    }

    public void setIs985(boolean is985) {
        this.is985 = is985;
    }

    public boolean isIs211() {
        return is211;
    }

    public void setIs211(boolean is211) {
        this.is211 = is211;
    }

}
