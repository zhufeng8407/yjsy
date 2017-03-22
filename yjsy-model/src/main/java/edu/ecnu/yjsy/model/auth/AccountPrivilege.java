package edu.ecnu.yjsy.model.auth;

import static edu.ecnu.yjsy.model.constant.Column.ACCOUNT_PRIVILEGE_ACCOUNT;
import static edu.ecnu.yjsy.model.constant.Column.ACCOUNT_PRIVILEGE_DEPART;
import static edu.ecnu.yjsy.model.constant.Column.ACCOUNT_PRIVILEGE_ROLE;
import static edu.ecnu.yjsy.model.constant.Column.ACCOUNT_PRIVILEGE_SCHOOL;
import static edu.ecnu.yjsy.model.constant.Column.ACCOUNT_PRIVILEGE_STUDENT;
import static edu.ecnu.yjsy.model.constant.Column.ACCOUNT_PRIVILEGE_SUPERVISOR;
import static edu.ecnu.yjsy.model.constant.Column.ACCOUNT_PRIVILEGE_UNIV;
import static edu.ecnu.yjsy.model.constant.Table.ACCOUNT_PRIVILEGE;

import javax.persistence.*;

import edu.ecnu.yjsy.model.EntityId;

/**
 * 用于表示一个账号在一个角色下的具体权限,即可以访问的域
 *
 * @author guhang
 * @author xiafan
 */
@Entity
@Table(name = ACCOUNT_PRIVILEGE)
@NamedEntityGraphs({ @NamedEntityGraph(name = "accountPrivilege.all",
        attributeNodes = {
                @NamedAttributeNode(value = "account", subgraph = "account"),
                @NamedAttributeNode(value = "role", subgraph = "pages") },
        subgraphs = {
                @NamedSubgraph(name = "account",
                        attributeNodes = {
                                @NamedAttributeNode("student"),
                                @NamedAttributeNode("staff") }),
                @NamedSubgraph(name = "pages",
                        attributeNodes = @NamedAttributeNode("pages")), }) })
public class AccountPrivilege extends EntityId {

    private static final long serialVersionUID = 1228377313272392930L;

    private final static String ROLE_INFO = "\"roleId\":%d,"
            + "\"roleName\":\"%s\",";

    private final static String UNIVERSITY_DOMAIN = "{" + ROLE_INFO
            + "\"isUniv\":true}";

    private final static String SCHOOL_DOMAIN = "{" + ROLE_INFO
            + " \"school\":\"%s\"}";

    private final static String DEPARTMENT_DOMAIN = "{" + ROLE_INFO
            + " \"school\":\"%s\",\"department\":\"%s\"}";

    // XXX
    // 建议改成 staffID
    private final static String SUPERVISOR_DOMAIN = "{" + ROLE_INFO
            + " \"facultyID\":\"%s\"}";

    private final static String STUDENT_DOMAIN = "{" + ROLE_INFO
            + " \"studentID\":\"%s\"}";

    // 账号
    @ManyToOne(optional = false)
    @JoinColumn(name = ACCOUNT_PRIVILEGE_ACCOUNT, nullable = false)
    private Account account;

    // 角色
    @ManyToOne(optional = false)
    @JoinColumn(name = ACCOUNT_PRIVILEGE_ROLE, nullable = false)
    private Role role;

    // 校级权限
    @Column(name = ACCOUNT_PRIVILEGE_UNIV)
    private boolean isUniversity;

    // 一级单位编码
    @Column(name = ACCOUNT_PRIVILEGE_SCHOOL, length = 10)
    private String schoolCode;

    // 二级单位编码
    @Column(name = ACCOUNT_PRIVILEGE_DEPART, length = 10)
    private String departmentCode;

    // XXX
    // 导师工号
    // 例如，导师张三的学生可能需要李四实际管理
    // 对于李四的帐号权限需要设置张三的工号，使得李四可以管理张三的学生
    @Column(name = ACCOUNT_PRIVILEGE_SUPERVISOR)
    private long supervisor;

    // 学号
    @Column(name = ACCOUNT_PRIVILEGE_STUDENT)
    private long student;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isUniversity() {
        return isUniversity;
    }

    public void setUniversity(boolean university) {
        isUniversity = university;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public long getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(long supervisor) {
        this.supervisor = supervisor;
    }

    public long getStudent() {
        return student;
    }

    public void setStudent(long student) {
        this.student = student;
    }

    @Override
    public int hashCode() {
        return getId().intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AccountPrivilege) { return getId()
                .equals(((AccountPrivilege) obj).getId()); }
        return false;
    }

    @Override
    public String toString() {
        String ret = null;
        switch (getRole().getDomainLevel()) {
        case 校级:
            ret = String.format(UNIVERSITY_DOMAIN, role.getId(),
                    role.getName());
            break;
        case 一级:
            if (getSchoolCode() == null) { throw new RuntimeException(
                    "用户" + getId() + "具备访问院系一级单位的权限，但一级单位编码为空值!"); }
            ret = String.format(SCHOOL_DOMAIN, role.getId(), role.getName(),
                    getSchoolCode());
            break;
        case 二级:
            if (getDepartmentCode() == null) { throw new RuntimeException(
                    "用户" + getId() + "具备访问院系二级单位的权限，但二级单位编码为空值!"); }
            ret = String.format(DEPARTMENT_DOMAIN, role.getId(), role.getName(),
                    getSchoolCode(), getDepartmentCode());
            break;
        case 导师:
            ret = String.format(SUPERVISOR_DOMAIN, role.getId(), role.getName(),
                    account.getStaff().getId());
            break;

        // 默认按照学生权限处理
        default:
            ret = String.format(STUDENT_DOMAIN, role.getId(), role.getName(),
                    account.getStudent().getId());
            break;
        }
        return ret;
    }

}
