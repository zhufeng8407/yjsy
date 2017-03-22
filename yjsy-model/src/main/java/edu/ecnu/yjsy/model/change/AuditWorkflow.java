package edu.ecnu.yjsy.model.change;

import static edu.ecnu.yjsy.model.constant.Column.CHANGE_TYPE;
import static edu.ecnu.yjsy.model.constant.Column.ROLE;
import static edu.ecnu.yjsy.model.constant.Column.SEQUENCE;
import static edu.ecnu.yjsy.model.constant.Table.AUDIT_WORKFLOW;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.ecnu.yjsy.model.EntityId;
import edu.ecnu.yjsy.model.auth.Role;

/**
 * 定义每个异动类型的审批流程，其中流程中的每个步骤由<code>sequence</code>和<code>type</code>唯一表示。
 * 
 * 主要逻辑如下：
 * 
 * 1. 每个异动类型对应<code>唯一</code>的审批流程
 * 
 * 2. 每个审批流程包括<code>若干步</code>具体的审批
 * 
 * 3. 每一步审批都对应一个角色，用于实际的审批操作
 * 
 * <pre>
 * 一个具体案例如下：
 * 
 * 1. 对于异动大类<code>转导师</code>中异动小类<code>院系内部转导师</code>，具体流程为<code>学生申请—>院系审核—>培养处审核—>学籍处审核</code>。
 * 
 * 2. 对于流程中的每个步骤，例如，<code>学生申请</code>，系统都会为其创建一个<code>sequence</code>，并为其赋予一个角色<code>role</code>，用于实际的申请审核。
 * </pre>
 * 
 * FIXME 1.
 * 考虑到业务变化，一种学籍异动类型的审批流程可能会发生变化，而现在的模型设计没有考虑到这种变化，需要扩展;换言之，如果要更改一种已在使用的学籍异动审批流程，那么已有的数据会发生不一致的问题，如何解决？
 * 
 * 2. 需要增加发起人
 * 
 * @author wanglei
 * @author xiafan
 * @author xulinhao
 */

@Entity
@Table(name = AUDIT_WORKFLOW, uniqueConstraints = @UniqueConstraint(
        columnNames = { SEQUENCE, CHANGE_TYPE }))
public class AuditWorkflow extends EntityId {

    /**
     * 
     */
    private static final long serialVersionUID = -4685792589949550851L;

    // 审批流程序号
    @Column(nullable = false, name = SEQUENCE)
    private short sequence;

    // 异动类型
    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false, name = CHANGE_TYPE)
    private StatusChangeType type;

    // 角色
    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false, name = ROLE)
    private Role role;

    // --------------------
    // METHODS
    // --------------------

    public short getSequence() {
        return sequence;
    }

    public void setSequence(short sequence) {
        this.sequence = sequence;
    }

    public StatusChangeType getType() {
        return type;
    }

    public void setType(StatusChangeType type) {
        this.type = type;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
