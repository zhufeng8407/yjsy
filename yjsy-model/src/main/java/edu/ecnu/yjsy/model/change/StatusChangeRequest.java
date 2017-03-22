package edu.ecnu.yjsy.model.change;

import static edu.ecnu.yjsy.model.constant.Column.CHANGE_TYPE;
import static edu.ecnu.yjsy.model.constant.Column.COMMENT;
import static edu.ecnu.yjsy.model.constant.Column.CREATED_AT;
import static edu.ecnu.yjsy.model.constant.Column.REASON;
import static edu.ecnu.yjsy.model.constant.Column.STAFF;
import static edu.ecnu.yjsy.model.constant.Column.STATUS;
import static edu.ecnu.yjsy.model.constant.Column.STUDENT;
import static edu.ecnu.yjsy.model.constant.Column.APPLICANT_USERNAME;
import static edu.ecnu.yjsy.model.constant.Column.APPLICANT_NAME;
import static edu.ecnu.yjsy.model.constant.Table.STATUS_CHANGE_REQUEST;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.ecnu.yjsy.model.EntityId;
import edu.ecnu.yjsy.model.staff.Staff;
import edu.ecnu.yjsy.model.student.Student;

/**
 * 记录每一个具体的异动申请。
 * 
 * FIXME 需要增加唯一性检查;换言之，当一个异动申请<code>正在</code>提交时，系统需要检查是否同一个学生已经提交了同一类型的异动申请？
 * 
 * @author wanglei
 * @author xiafan
 * @author xulinhao
 */

@Entity
@Table(name = STATUS_CHANGE_REQUEST)
public class StatusChangeRequest extends EntityId {

    /**
     * 
     */
    private static final long serialVersionUID = 1977438911415983168L;

    // 申请理由
    @Column(nullable = false, length = 500, name = REASON)
    private String reason;

    // 申请日期
    @Column(nullable = false, name = CREATED_AT)
    private Timestamp createdAt;

    // FIXME
    // 具体存储什么信息
    // 备注
    @Column(length = 40, name = COMMENT)
    private String comment;

    // FIXME
    // 记录最终审批结果?
    // 审批结果
    // 参见<code>AuditStatus</code>的说明
    @Column(nullable = false, columnDefinition = "smallint", name = STATUS)
    @Enumerated(EnumType.ORDINAL)
    private AuditStatus status;

    // 不可为空是因为学籍异动必须和一个学生关联
    // 申请人 - 学生
    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false, name = STUDENT)
    private Student student;

    // 有些异动申请，如推迟入学复学，是由学院提交的
    // 申请人 - 教职工
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = STAFF)
    private Staff staff;

    // 异动类型
    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false, name = CHANGE_TYPE)
    private StatusChangeType type;

    // 代申请人用户名
    @Column(length = 40, name = APPLICANT_USERNAME)
    private String applicantUserName;

    // 代申请人姓名
    @Column(length = 40, name = APPLICANT_NAME)
    private String applicantName;

    // --------------------
    // METHODS
    // --------------------

    public StatusChangeRequest() {}

    public StatusChangeRequest(String reason, Timestamp createdAt,
            String comment, AuditStatus status, Student student, Staff staff,
            StatusChangeType type, String applicantUserName,
            String applicantName) {
        this.reason = reason;
        this.createdAt = createdAt;
        this.comment = comment;
        this.status = status;
        this.student = student;
        this.staff = staff;
        this.type = type;
        this.applicantUserName = applicantUserName;
        this.applicantName = applicantName;
    }
    
    public StatusChangeRequest(String reason, Timestamp createdAt,
            String comment, AuditStatus status, Student student,
            StatusChangeType type, String applicantUserName,
            String applicantName) {
        this.reason = reason;
        this.createdAt = createdAt;
        this.comment = comment;
        this.status = status;
        this.student = student;
        this.type = type;
        this.applicantUserName = applicantUserName;
        this.applicantName = applicantName;
    }

    public StatusChangeRequest(long id, String reason, Timestamp createdAt,
            String comment, AuditStatus status, Student student, Staff staff,
            StatusChangeType type, String applicantUserName,
            String applicantName) {
        this(reason, createdAt, comment, status, student, staff, type,
                applicantUserName, applicantName);
        this.setId(id);
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public AuditStatus getStatus() {
        return status;
    }

    public void setStatus(AuditStatus status) {
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public StatusChangeType getType() {
        return type;
    }

    public void setType(StatusChangeType type) {
        this.type = type;
    }

    public String getApplicantUserName() {
        return applicantUserName;
    }

    public void setApplicantUserName(String applicantUserName) {
        this.applicantUserName = applicantUserName;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

}