package edu.ecnu.yjsy.model.auth;

import static edu.ecnu.yjsy.model.constant.Column.ACCOUNT_PASSWD;
import static edu.ecnu.yjsy.model.constant.Column.ACCOUNT_STAFF;
import static edu.ecnu.yjsy.model.constant.Column.ACCOUNT_STUDENT;
import static edu.ecnu.yjsy.model.constant.Column.ACCOUNT_USERNAME;
import static edu.ecnu.yjsy.model.constant.Table.ACCOUNT;
import static edu.ecnu.yjsy.model.constant.Table.REL_ACCOUNT_ROLE;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import edu.ecnu.yjsy.model.EntityId;
import edu.ecnu.yjsy.model.message.Message;
import edu.ecnu.yjsy.model.staff.Staff;
import edu.ecnu.yjsy.model.student.Student;

/**
 * 表示系统中的账号,每个账号可以关联一个老师或者一个学生
 *
 * @author guhang
 * @author xiafan
 */
@Entity
@Table(name = ACCOUNT, uniqueConstraints = @UniqueConstraint(
        columnNames = { ACCOUNT_USERNAME }))
@NamedEntityGraphs({
        @NamedEntityGraph(name = "account.staff",
                attributeNodes = { @NamedAttributeNode("staff") }),
        @NamedEntityGraph(name = "account.student",
                attributeNodes = { @NamedAttributeNode("student") }),
        @NamedEntityGraph(name = "account.all",
                attributeNodes = { @NamedAttributeNode("student"),
                        @NamedAttributeNode("staff"),
                        @NamedAttributeNode(value = "accountPrivileges",
                                subgraph = "role"), },
                subgraphs = {
                        @NamedSubgraph(name = "pages",
                                attributeNodes = @NamedAttributeNode("pages")),
                        @NamedSubgraph(name = "role",
                                attributeNodes = @NamedAttributeNode(
                                        value = "role",
                                        subgraph = "pages")) }) })
public class Account extends EntityId {

    private static final long serialVersionUID = 1848943640346915715L;

    @OneToOne
    @JoinColumn(name = ACCOUNT_STAFF)
    private Staff staff;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = ACCOUNT_STUDENT)
    private Student student;

    @Column(name = ACCOUNT_USERNAME)
    private String username;

    @Column(name = ACCOUNT_PASSWD)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinTable(name = REL_ACCOUNT_ROLE,
            joinColumns = { @JoinColumn(name = "accounts",
                    referencedColumnName = "id") },
            inverseJoinColumns = {
                    @JoinColumn(name = "roles", referencedColumnName = "id") },
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = { "accounts", "roles" }) })
    @OrderBy(value = "id DESC")
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private Set<AccountPrivilege> accountPrivileges = new HashSet<>();

    // FIXME: 顾航需要确认一下这里是用来检索发给该账号的消息还是由该账号创建的消息
    @ManyToMany(mappedBy = "receivers")
    private Set<Message> messages = new HashSet<>();

    // --------------------
    // METHODS
    // --------------------

    public Account() {}

    public Account(Long id, Staff staff) {
        super.setId(id);
        this.staff = staff;
    }

    public Account(Long id, Student student) {
        super.setId(id);
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public Set<AccountPrivilege> getAccountPrivileges() {
        return accountPrivileges;
    }

    public void setAccountPrivileges(Set<AccountPrivilege> accountPrivileges) {
        this.accountPrivileges = accountPrivileges;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

}
