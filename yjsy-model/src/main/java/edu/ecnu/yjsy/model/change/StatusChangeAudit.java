package edu.ecnu.yjsy.model.change;

import static edu.ecnu.yjsy.model.constant.Column.ACCOUNT;
import static edu.ecnu.yjsy.model.constant.Column.AUDIT_AT;
import static edu.ecnu.yjsy.model.constant.Column.AUDIT_STATUS;
import static edu.ecnu.yjsy.model.constant.Column.AUDIT_WORKFLOW;
import static edu.ecnu.yjsy.model.constant.Column.CHANGE_REQUEST;
import static edu.ecnu.yjsy.model.constant.Column.REASON;
import static edu.ecnu.yjsy.model.constant.Table.STATUS_CHANGE_AUDIT;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.ecnu.yjsy.model.EntityId;
import edu.ecnu.yjsy.model.auth.Account;

/**
 * 记录异动审批流程中<code>某一审批步骤</code>的具体信息，例如，谁负责审批，审批结果是什么等。
 *
 * <code>workflow</code>和<code>request</code>的组合用于唯一的标识一个具体异动申请中的某一步审批的具体信息。
 * 
 * FIXME 是否需要考虑到学生助管的情况？换言之，只使用一个具体的帐号<code>account</code>是否可以包括这个问题？
 *
 * @author wanglei
 * @author xiafan
 * @author xulinhao
 */

@Entity
@Table(name = STATUS_CHANGE_AUDIT, uniqueConstraints = @UniqueConstraint(
        columnNames = { AUDIT_WORKFLOW, CHANGE_REQUEST }))
public class StatusChangeAudit extends EntityId {

    /**
     * 
     */
    private static final long serialVersionUID = 2206799023608896141L;

    // 流程序号
    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false, name = AUDIT_WORKFLOW)
    private AuditWorkflow workflow;

    // 异动申请
    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false, name = CHANGE_REQUEST)
    private StatusChangeRequest request;

    // 审批结果
    @Column(nullable = false, columnDefinition = "smallint",
            name = AUDIT_STATUS)
    @Enumerated(EnumType.ORDINAL)
    private AuditStatus status;

    // XXX
    // 审批人账号
    // 例如，考虑到学生助管的情况，这里增加一个帐号的字段
    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false, name = ACCOUNT)
    private Account auditor;

    // 审批时间
    @Column(nullable = false, name = AUDIT_AT)
    private Timestamp auditAt;

    // 审批理由
    @Column(name = REASON, length = 500)
    private String reason;

    // --------------------
    // METHODS
    // --------------------

    public StatusChangeAudit() {}

    public StatusChangeAudit(AuditWorkflow workflow,
            StatusChangeRequest request, AuditStatus status, Account auditor,
            Timestamp auditAt, String reason) {
        this.workflow = workflow;
        this.request = request;
        this.status = status;
        this.auditor = auditor;
        this.auditAt = auditAt;
        this.reason = reason;
    }

    public StatusChangeAudit(long id, AuditWorkflow workflow,
            StatusChangeRequest request, AuditStatus status, Account auditor,
            Timestamp auditAt, String reason) {
        this(workflow, request, status, auditor, auditAt, reason);
        this.setId(id);
    }

    public AuditStatus getStatus() {
        return status;
    }

    public void setStatus(AuditStatus status) {
        this.status = status;
    }

    public AuditWorkflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(AuditWorkflow workflow) {
        this.workflow = workflow;
    }

    public StatusChangeRequest getRequest() {
        return request;
    }

    public void setRequest(StatusChangeRequest request) {
        this.request = request;
    }

    public Account getAuditor() {
        return auditor;
    }

    public void setAuditor(Account auditor) {
        this.auditor = auditor;
    }

    public Timestamp getAuditAt() {
        return auditAt;
    }

    public void setAuditAt(Timestamp auditAt) {
        this.auditAt = auditAt;
    }

    public String getReason() {
        // FIXME
        // 不应该在这里处理null的逻辑，应该放在对应的服务中
        if (reason == null) return "";
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
