package edu.ecnu.yjsy.model.change;

import static edu.ecnu.yjsy.model.constant.Column.*;
import static edu.ecnu.yjsy.model.constant.Table.STATUS_CHANGE_TYPE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import edu.ecnu.yjsy.model.EntityId;

/**
 * 学籍异动类型
 *
 * @author wanglei
 * @author LeeYanBin
 * @author xulinhao
 * @see <a href=
 * "http://58.198.176.57/xulinhao/yjsy-wiki/blob/master/documentation/B-学籍/B4-异动设计.xlsx">学籍异动类型定义</a>
 * <p>
 * FIXME
 * 2016-12-13的更新如下：
 * <p>
 * 1. 延期 - 预毕业日期
 * <p>
 * 2. 休学 - 预毕业日期 在籍状态（补贴等将会需要随之调整），是否需要在学籍表中增加在籍状态（毕业状态是一个，需要统一化）
 * <p>
 * 3. 复学 - 在籍状态
 * <p>
 * 4. 提前毕业 - 预毕业时间
 * <p>
 * 5. 放弃入学 - 离校状态（参考毕业类型）
 * <p>
 * 6. 推迟入学 - 离校状态 入学日期 入学年级 预毕业日期 学号不变 下一学年按照新生处理
 * <p>
 * 7. 转导师 - 导师工号
 * <p>
 * 8. 转专业 - 专业代码
 * <p>
 * 9. 转专业（跨院系）- 专业代码 院系代码 导师工号 需要走签报系统，学籍系统用于记录，由学籍管理员操作，需要记录 签报号 和 URL
 * <p>
 * 10. 结业 - 预毕业审核 在籍状态 毕业类型到结业 离校时间 毕业时间
 * <p>
 * 11. 肄业 - 预毕业审核 在籍状态 毕业类型到结业 离校时间 毕业时间
 * <p>
 * 12. 硕博连读 - 毕业时间？
 * <p>
 * 13. 硕博转硕士 - 层次到硕士 预毕业时间
 * <p>
 * 14. 直博转硕士 - 层次到硕士 预毕业时间
 * <p>
 * 15. 退学 - 参考肄业 需要走签报系统，学籍系统用于记录，由学籍管理员操作，需要记录 签报号 和 URL
 * <p>
 * 16. 出国 - 在籍状态到出国 时间 学习方式 不延长学制
 * <p>
 * 增加备注字段：用于提示学生的申请条件
 */

@Entity
@Table(name = STATUS_CHANGE_TYPE, uniqueConstraints = {
        @UniqueConstraint(columnNames = { MAJOR_CODE, MINOR_CODE }) })
public class StatusChangeType extends EntityId {

    /**
     * 
     */
    private static final long serialVersionUID = 4154327593882474830L;

    // 大类
    @Column(nullable = false, length = 40, name = MAJOR)
    private String major;

    // 大类编号
    @Column(nullable = false, name = MAJOR_CODE)
    private short majorCode;

    // 小类
    @Column(nullable = false, length = 40, name = MINOR)
    private String minor;

    // 小类编号
    @Column(nullable = false, name = MINOR_CODE)
    private short minorCode;

    // 限制
    @Column(length = 200, name = RESTRICT)
    private String restrict;

    // 备注
    @Column(length = 200, name = MEMO)
    private String memo;

    // 学生是否可见
    @Column(length = 200, name = VISIBLE)
    private boolean visible;

    // 是否需要上传文件
    @Column(name = ATTACHMENT)
    private boolean attachment;

    // FIXME
    // 命名不好
    // 异动后相关信息修改
    @Column(length = 200, name = MODIFICATION)
    private String modification;

    // 学籍状态
    @Column(length = 20, name = STATUS)
    private String status;

    // FIXME
    // 命名不好
    // 是否离校
    @Column(name = QUIT)
    private boolean quit;

    // FIXME
    // 命名不好
    // 是否归入终结生
    @Column(name = TERMINATED)
    private boolean terminated;

    // 邮件的回复内容（学生）
    @Column(length = 1000, name = REPLY_STUDENT)
    private String replyToStudent;

    // 邮件的回复内容（导师）
    @Column(length = 1000, name = REPLY_SUPERVISOR)
    private String replyToSupervisor;

    @Column(name = STATUS_CHANGE_VISIBLE)
    private boolean isVisible;

    @Column(name = STATUS_CHANGE_ISUPLOAD_FILE)
    private boolean isUploadFile;

    @Column(length = 1000, name = STATUS_CHANGE_CONSTRAINT)
    private String myConstraint;
    // --------------------
    // METHODS
    // --------------------

    public StatusChangeType() {
    }

    public StatusChangeType(String major, short majorCode, String minor,
            short minorCode) {
        this.major = major;
        this.majorCode = majorCode;
        this.minor = minor;
        this.minorCode = minorCode;
    }
    
    
    public StatusChangeType(String major, short majorCode) {
		super();
		this.major = major;
		this.majorCode = majorCode;
	}

 
	public StatusChangeType(short minorCode, String minor) {
		super();
		this.minorCode = minorCode;
		this.minor = minor;
	}

	public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public short getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(short majorCode) {
        this.majorCode = majorCode;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public short getMinorCode() {
        return minorCode;
    }

    public void setMinorCode(short minorCode) {
        this.minorCode = minorCode;
    }

    public String getRestrict() {
        return restrict;
    }

    public void setRestrict(String restrict) {
        this.restrict = restrict;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isAttachment() {
        return attachment;
    }

    public void setAttachment(boolean attachment) {
        this.attachment = attachment;
    }

    public String getModification() {
        return modification;
    }

    public void setModification(String modification) {
        this.modification = modification;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isQuit() {
        return quit;
    }

    public void setQuit(boolean quit) {
        this.quit = quit;
    }

    public boolean isTerminated() {
        return terminated;
    }

    public void setTerminated(boolean terminated) {
        this.terminated = terminated;
    }

    public String getReplyToStudent() {
        return replyToStudent;
    }

    public void setReplyToStudent(String replyToStudent) {
        this.replyToStudent = replyToStudent;
    }

    public String getReplyToSupervisor() {
        return replyToSupervisor;
    }

    public void setReplyToSupervisor(String replyToSupervisor) {
        this.replyToSupervisor = replyToSupervisor;
    }

    public boolean isUploadFile() {
        return isUploadFile;
    }

    public void setUploadFile(boolean uploadFile) {
        isUploadFile = uploadFile;
    }

    public String getMyConstraint() {
        return myConstraint;
    }

    public void setMyConstraint(String myConstraint) {
        this.myConstraint = myConstraint;
    }
}