package edu.ecnu.yjsy.model.auth;

import java.util.Set;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface RoleRepository extends JpaRepository<Role, Long> {

    @EntityGraph(value = "role.all", type = EntityGraphType.FETCH)
    Role findByName(String name);

    @EntityGraph(value = "role.all", type = EntityGraphType.FETCH)
    @Query("from Role where name = ?1")
    Role findPagesByName(String name);

    @EntityGraph(value = "role.all", type = EntityGraphType.FETCH)
    @Query("select role from Role as role join role.restAPIs as mps where mps.id=?1")
    public Set<Role> findByrestAPIId(Long id);

}
