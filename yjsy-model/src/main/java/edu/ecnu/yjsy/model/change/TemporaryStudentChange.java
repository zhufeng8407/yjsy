package edu.ecnu.yjsy.model.change;

import static edu.ecnu.yjsy.model.constant.Column.STUDENT;
import static edu.ecnu.yjsy.model.constant.Column.UPDATEJSON;
import static edu.ecnu.yjsy.model.constant.Column.CHANGE_REQUEST;
import static edu.ecnu.yjsy.model.constant.Column.DELFLG;
import static edu.ecnu.yjsy.model.constant.Table.TEMPORARY_STUDENT_CHANGE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.ecnu.yjsy.model.EntityId;

/**
 * 学籍异动更新学籍临时表
 *
 * @author zhufeng
 *         <p>
 *         1. id - 主键
 *         <p>
 *         2. 学员id - 学籍表主键
 *         <p>
 *         3. 更新json - 更新字段以json格式存储成String
 *         <p>
 *         4. 删除flag - 默认为0，逻辑删除为1(只有当申请提交了以后，移动流程变更了才会置为物理删除）
 *         <p>
 *         5. 异动申请 - 异动申请表逐渐
 *         <p>
 */

@Entity
@Table(name = TEMPORARY_STUDENT_CHANGE)
public class TemporaryStudentChange extends EntityId {

    /**
     * 
     */
    private static final long serialVersionUID = 3459648460556997704L;

    @Column(nullable = false, name = STUDENT)
    private String studentId;

    @Column(name = UPDATEJSON)
    private String updateJson;

    @Column(nullable = false, name = DELFLG)
    private String delFlg;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false, name = CHANGE_REQUEST)
    private StatusChangeRequest request;

    public TemporaryStudentChange() {}

    public TemporaryStudentChange(String studentId, String updateJson,
            String delFlg, StatusChangeRequest request) {
        super();
        this.studentId = studentId;
        this.updateJson = updateJson;
        this.delFlg = delFlg;
        this.request = request;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getUpdateJson() {
        return updateJson;
    }

    public void setUpdateJson(String updateJson) {
        this.updateJson = updateJson;
    }

    public String getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(String delFlg) {
        this.delFlg = delFlg;
    }

    public StatusChangeRequest getRequest() {
        return request;
    }

    public void setRequest(StatusChangeRequest request) {
        this.request = request;
    }

}
