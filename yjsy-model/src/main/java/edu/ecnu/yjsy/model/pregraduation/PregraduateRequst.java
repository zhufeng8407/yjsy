package edu.ecnu.yjsy.model.pregraduation;

import static edu.ecnu.yjsy.model.constant.Column.ACCOUNT;
import static edu.ecnu.yjsy.model.constant.Column.AUDITDATE;
import static edu.ecnu.yjsy.model.constant.Column.CREATEDATE;
import static edu.ecnu.yjsy.model.constant.Column.CURDISPLAYVALUE;
import static edu.ecnu.yjsy.model.constant.Column.FIELDDISPLAYNAME;
import static edu.ecnu.yjsy.model.constant.Column.FIELDNAME;
import static edu.ecnu.yjsy.model.constant.Column.ISACCEPT;
import static edu.ecnu.yjsy.model.constant.Column.NEWDISPLAYVALUE;
import static edu.ecnu.yjsy.model.constant.Column.NEWVALUE;
import static edu.ecnu.yjsy.model.constant.Column.REASON;
import static edu.ecnu.yjsy.model.constant.Column.STUDENT;
import static edu.ecnu.yjsy.model.constant.Table.PREGRADUATION_REQUEST;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import edu.ecnu.yjsy.model.EntityId;
import edu.ecnu.yjsy.model.auth.Account;
import edu.ecnu.yjsy.model.student.Student;

/**
 * FIXME: @潘妍虹: fieldName应该换为APIMapping
 *
 * @author PYH 预毕业申请表
 */
@Entity
@Table(name = PREGRADUATION_REQUEST)
public class PregraduateRequst extends EntityId {

    /**
     * 
     */
    private static final long serialVersionUID = 8113308224742576448L;

    @ManyToOne
    @JoinColumn(name = STUDENT)
    private Student student;

    // 类名
    @Column(name = FIELDNAME, length = 50)
    private String fieldName;

    // 类的显示名称，中文能看到的名字
    @Column(name = FIELDDISPLAYNAME, length = 256)
    private String fieldDisplayName;

    // 当前该字段显示值
    @Column(name = CURDISPLAYVALUE, length = 256)
    private String curDisplayValue;

    // 修改后该字段新值，外键就为id，非外键就是直接的值
    @Column(name = NEWVALUE, length = 256)
    private String newValue;

    // 修改后新字段的显示值
    @Column(name = NEWDISPLAYVALUE, length = 256)
    private String newDisplayValue;

    // 申请提交日期
    @Column(name = CREATEDATE, nullable = false)
    private Date createDate;

    // 申请审批日期
    @Column(name = AUDITDATE)
    private Date auditDate;

    // 审批人账号
    @ManyToOne
    @JoinColumn(name = ACCOUNT)
    private Account account;

    // 审批结果，同意审批更改数据，不同意驳回
    @Column(name = ISACCEPT)
    private boolean isAccept;

    // 不同意审批的理由
    @Column(name = REASON, length = 1024)
    private String reason;

    public PregraduateRequst() {

    }

    public PregraduateRequst(Student student, String fieldName,
            String fieldDisplayName, String curDisplayValue, String newValue,
            String newDisplayValue, Date createDate) {
        super();
        this.student = student;
        this.fieldName = fieldName;
        this.fieldDisplayName = fieldDisplayName;
        this.curDisplayValue = curDisplayValue;
        this.newValue = newValue;
        this.newDisplayValue = newDisplayValue;
        this.createDate = createDate;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDisplayName() {
        return fieldDisplayName;
    }

    public void setFieldDisplayName(String fieldDisplayName) {
        this.fieldDisplayName = fieldDisplayName;
    }

    public String getCurDisplayValue() {
        return curDisplayValue;
    }

    public void setCurDisplayValue(String curDisplayValue) {
        this.curDisplayValue = curDisplayValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getNewDisplayValue() {
        return newDisplayValue;
    }

    public void setNewDisplayValue(String newDisplayValue) {
        this.newDisplayValue = newDisplayValue;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean isAccept() {
        return isAccept;
    }

    public void setAccept(boolean isAccept) {
        this.isAccept = isAccept;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
