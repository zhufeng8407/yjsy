package edu.ecnu.yjsy.model.auth;

import static edu.ecnu.yjsy.model.constant.Column.ID;
import static edu.ecnu.yjsy.model.constant.Table.PAGE_PRIVILEGE;
import static edu.ecnu.yjsy.model.constant.Table.REL_PAGE_METHOD_PRIVILEGE;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 对应于前端的一个菜单项
 *
 * @author guhang
 * @author xiafan
 */
@Entity
@Table(name = PAGE_PRIVILEGE, uniqueConstraints = @UniqueConstraint(
        columnNames = { "pageName", "url" }))
@NamedEntityGraphs({ @NamedEntityGraph(name = "page.restAPIs",
        attributeNodes = { @NamedAttributeNode("restAPIs") }) })
public class Page implements Serializable {

    private static final long serialVersionUID = -2427110103293900960L;

    @Id
    long id;

    // 页面名称
    private String pageName;

    // 页面地址
    private String url;

    // 页面使用的icon类
    private String iconClass;

    // 对页面的详细描述
    private String description = "";

    // 页面的备注,用于区分同名的页面
    private String annotation = "";

    // 页面所属的父页面
    private long parentPageID = -1;

    @ManyToMany(mappedBy = "pages", fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(name = REL_PAGE_METHOD_PRIVILEGE,
            joinColumns = {
                    @JoinColumn(name = "pages", referencedColumnName = ID) },
            inverseJoinColumns = {
                    @JoinColumn(name = "restAPIs", referencedColumnName = ID) })
    private Set<RestAPI> restAPIs = new HashSet<>();

    public Page() {}

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentPageID() {
        return parentPageID;
    }

    public void setParentPageID(long parentPageID) {
        this.parentPageID = parentPageID;
    }

    public String getIconClass() {
        return iconClass;
    }

    public Set<RestAPI> getRestAPIs() {
        return restAPIs;
    }

    public String getPageName() {
        return pageName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getUrl() {
        return url;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public void setRestAPIs(Set<RestAPI> restAPIs) {
        this.restAPIs = restAPIs;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    @Override
    public int hashCode() {
        return (Integer.parseInt(getId().toString()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Page) { return getId() == ((Page) obj).getId(); }
        return false;
    }

}
