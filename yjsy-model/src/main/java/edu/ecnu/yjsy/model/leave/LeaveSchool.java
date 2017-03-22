package edu.ecnu.yjsy.model.leave;

import static edu.ecnu.yjsy.model.constant.Table.LEAVE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import edu.ecnu.yjsy.model.EntityId;
import edu.ecnu.yjsy.model.student.Student;

/**
 * @author LeeYanBin
 */

@Entity
@Table(name = LEAVE)
public class LeaveSchool extends EntityId{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1154423004972062983L;
    
    //原系秘书审批
    private Boolean secretaryResult;
    
    @Column(length = 20)
    private String secretaryReason;
    
    //职能部门审批
    //财务处
    private Boolean financialResult;
    
    @Column(length = 20)
    private String financialReason;
    
    //图书馆
    private Boolean libraryResult;
    
    @Column(length = 20)
    private String libraryReason;
    
    //后勤
    private Boolean logisticsResult;
    
    @Column(length = 20)
    private String logisticsReason;
    
    //研究生院审批
    private Boolean graduateResult;
    
    @Column(length = 20)
    private String graduateReason;
    
    //学生外键
    @ManyToOne
    private Student student;

    // --------------------
    // METHODS
    // --------------------
    
    public LeaveSchool() {}

    public LeaveSchool(Boolean secretaryResult, String secretaryReason,
            Boolean financialResult, String financialReason,
            Boolean libraryResult, String libraryReason,
            Boolean logisticsResult, String logisticsReason,
            Boolean graduateResult, String graduateReason, Student student) {
        super();
        this.secretaryResult = secretaryResult;
        this.secretaryReason = secretaryReason;
        this.financialResult = financialResult;
        this.financialReason = financialReason;
        this.libraryResult = libraryResult;
        this.libraryReason = libraryReason;
        this.logisticsResult = logisticsResult;
        this.logisticsReason = logisticsReason;
        this.graduateResult = graduateResult;
        this.graduateReason = graduateReason;
        this.student = student;
    }

    public Boolean getSecretaryResult() {
        return secretaryResult;
    }

    public void setSecretaryResult(Boolean secretaryResult) {
        this.secretaryResult = secretaryResult;
    }

    public String getSecretaryReason() {
        return secretaryReason;
    }

    public void setSecretaryReason(String secretaryReason) {
        this.secretaryReason = secretaryReason;
    }

    public Boolean getFinancialResult() {
        return financialResult;
    }

    public void setFinancialResult(Boolean financialResult) {
        this.financialResult = financialResult;
    }

    public String getFinancialReason() {
        return financialReason;
    }

    public void setFinancialReason(String financialReason) {
        this.financialReason = financialReason;
    }

    public Boolean getLibraryResult() {
        return libraryResult;
    }

    public void setLibraryResult(Boolean libraryResult) {
        this.libraryResult = libraryResult;
    }

    public String getLibraryReason() {
        return libraryReason;
    }

    public void setLibraryReason(String libraryReason) {
        this.libraryReason = libraryReason;
    }

    public Boolean getLogisticsResult() {
        return logisticsResult;
    }

    public void setLogisticsResult(Boolean logisticsResult) {
        this.logisticsResult = logisticsResult;
    }

    public String getLogisticsReason() {
        return logisticsReason;
    }

    public void setLogisticsReason(String logisticsReason) {
        this.logisticsReason = logisticsReason;
    }

    public Boolean getGraduateResult() {
        return graduateResult;
    }

    public void setGraduateResult(Boolean graduateResult) {
        this.graduateResult = graduateResult;
    }

    public String getGraduateReason() {
        return graduateReason;
    }

    public void setGraduateReason(String graduateReason) {
        this.graduateReason = graduateReason;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    
    
}
