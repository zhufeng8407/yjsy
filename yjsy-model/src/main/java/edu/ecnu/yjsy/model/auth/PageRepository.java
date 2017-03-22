package edu.ecnu.yjsy.model.auth;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import edu.ecnu.yjsy.model.view.auth.PageSummary;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface PageRepository extends JpaRepository<Page, Long> {

    @Query("select page.id as id, page.parentPageID as parentPageID, page.pageName as pageName, page.description as description, page.url as url , page.annotation as annotation from Page as page")
    public List<PageSummary> queryAll();

    //@EntityGraph(value = "page.all", type = EntityGraphType.FETCH)
    public Page findOneById(long id);

    @EntityGraph(value = "page.restAPIs", type = EntityGraphType.FETCH)
    public Page findWithApisById(long id);

    @Query("from Page where pageName=?1 and url=?2")
    public Page findOne(String pageName, String url);

    @Query("from Page where pageName=?1")
    public Page findOne(String pageName);

    @Query(value = "select page from Page as page join page.restAPIs as mps where mps.id=?1")
    public Set<Page> findByMethodId(long methodId);

    @Query("select page from Page as page join page.roles as roles where roles.id=?1")
    public List<Page> findByRoleId(long roleId);

    @Query("select pageName from Page")
    public List<String> findPageName();

}
