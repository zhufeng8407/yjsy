package edu.ecnu.yjsy.model.auth;

import static edu.ecnu.yjsy.model.constant.Table.METHOD_PRIVILEGE;
import static edu.ecnu.yjsy.model.constant.Table.REL_METHODPRIVILEGE_ROLE;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import edu.ecnu.yjsy.model.EntityId;

/**
 * @author guhang
 * @author xiafan
 */
@Entity
@Table(name = METHOD_PRIVILEGE, uniqueConstraints = @UniqueConstraint(
        columnNames = {"api", "httpMethod"}))
@NamedEntityGraphs({@NamedEntityGraph(name = "restAPI.all",
        attributeNodes = {
                @NamedAttributeNode(value = "roles", subgraph = "pages"),
                @NamedAttributeNode(value = "pages")},
        subgraphs = @NamedSubgraph(name = "pages",
                attributeNodes = @NamedAttributeNode("pages"))),
        @NamedEntityGraph(name = "restAPI.roles",
                attributeNodes = {@NamedAttributeNode(value = "roles")})})

public class RestAPI extends EntityId {

    private static final long serialVersionUID = 2075801454941172704L;

    // 暴露的rest api的地址,例如/xj/student
    private String api;

    // api的http访问方法,例如, GET,POST
    private String httpMethod;

    // 对api的描述
    private String description;

    // api返回结果的示例
    private String example;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = REL_METHODPRIVILEGE_ROLE,
            joinColumns = {@JoinColumn(name = "restAPIs",
                    referencedColumnName = "id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "roles", referencedColumnName = "id")},
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"restAPIs", "roles"})})
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "restAPIs", fetch = FetchType.LAZY)
    private Set<Page> pages = new HashSet<>();

    public String getApi() {
        return api;
    }

    public Set<Page> getPages() {
        return pages;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public void setPages(Set<Page> pages) {
        this.pages = pages;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
