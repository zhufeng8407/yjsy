package edu.ecnu.yjsy.model.auth;

import static edu.ecnu.yjsy.model.constant.Table.REL_ROLE_PAGE;
import static edu.ecnu.yjsy.model.constant.Table.ROLE;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import edu.ecnu.yjsy.model.EntityId;

/**
 * Define the role of a group users who have the same privileges.
 *
 * @author xiafan
 * @author wanglei
 * @author guhang
 */

@Entity
@Table(name = ROLE,
        uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedEntityGraphs({@NamedEntityGraph(name = "role.all",
        attributeNodes = {@NamedAttributeNode("pages")})})
public class Role extends EntityId {

    private static final long serialVersionUID = 8875642221499652417L;

    public static final String NEWBIE = "新生";
    public static final String STUDENT = "学生";
    public static final String SUPERVISOR = "导师";

    // 角色名称
    @Column(nullable = false, length = 20)
    private String name;

    @Enumerated
    private DomainLevel domainLevel;

    @ManyToMany(mappedBy = "roles")
    private Set<Account> accounts = new HashSet<>();

    // 和<code>RestAPI</code>关联的修改只能通过<code>RestAPI</code>修改!!!
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<RestAPI> restAPIs = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = REL_ROLE_PAGE,
            joinColumns = {
                    @JoinColumn(name = "roles", referencedColumnName = "id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "pages", referencedColumnName = "id")},
            uniqueConstraints = @UniqueConstraint(
                    columnNames = {"roles", "pages"}))
    private Set<Page> pages = new HashSet<>();

    // 和<code>AccountPrivilege</code>关联的修改只能通过<code>AccountPrivilege</code>修改!!!
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @OrderBy(value = "id DESC")
    private Set<AccountPrivilege> accountPrivileges = new HashSet<>();

    // --------------------
    // METHODS
    // --------------------

    public String getName() {
        return name;
    }

    public Set<AccountPrivilege> getAccountPrivileges() {
        return accountPrivileges;
    }

    public void setAccountPrivileges(Set<AccountPrivilege> accountPrivileges) {
        this.accountPrivileges = accountPrivileges;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Set<Page> getPages() {
        return pages;
    }

    public void setPages(Set<Page> pages) {
        this.pages = pages;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DomainLevel getDomainLevel() {
        return domainLevel;
    }

    public void setDomainLevel(DomainLevel domainLevel) {
        this.domainLevel = domainLevel;
    }

    public Set<RestAPI> getRestAPIs() {
        return restAPIs;
    }

    public void setRestAPIs(Set<RestAPI> restAPIs) {
        this.restAPIs = restAPIs;
    }

    @Override
    public int hashCode() {
        return getId().intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Role) {
            return getId()
                    .equals(((Role) obj).getId());
        }
        return false;
    }

}
