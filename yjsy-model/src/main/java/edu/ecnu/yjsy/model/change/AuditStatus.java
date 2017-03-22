package edu.ecnu.yjsy.model.change;

/**
 * 对于<code>研究生</code>，一个异动申请存在三种状态：
 * 
 * 1. 该申请的最终状态是：<code>同意</code>或<code>否决</code>
 * 
 * 2. 该申请在审批过程中任意时刻的状态是：<code>审批中</code>
 * 
 * 对于<code>导师，院系秘书或学籍管理员</code>，一个异动申请存在四种状态：同意，否决，审批中，待审批。
 * 
 * 1. 该申请的最终状态是：<code>同意</code>或<code>否决</code>
 * 
 * 2. 对于审批流程中的某个审批人而言，如果审批流程已到达<code>该审批人</code>且还没有审批，则该审批人看到的申请状态是：<code>待审批</code>
 * 
 * 3. 对于审批流程中的某个审批人而言，如果审批流程还没有到达<code>该审批人</code>这一步，则该审批人看到的申请状态是：<code>审批中</code>
 * 
 * @author wanglei
 * @author xiafan
 * @author xulinhao
 */

public enum AuditStatus {

    同意, 否决, 审批中, 待审批,

}
