package edu.ecnu.yjsy.model.auth;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface RestAPIRepository extends JpaRepository<RestAPI, Long> {

    RestAPI findByApi(String api);

    @EntityGraph(value = "restAPI.roles", type = EntityGraph.EntityGraphType.FETCH)
    RestAPI findByApiAndHttpMethod(String api, String httpMethod);

    @Query(value = "select mp from RestAPI as mp join mp.roles as roles where roles.id=?1")
    public Set<RestAPI> findByRoleId(Long id);

}
