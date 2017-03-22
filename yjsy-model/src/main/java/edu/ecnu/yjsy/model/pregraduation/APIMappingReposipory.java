package edu.ecnu.yjsy.model.pregraduation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface APIMappingReposipory extends JpaRepository<APIMapping, Long> {

    @Query(value = "select field from APIMapping order by id")
    List<String> findFields();

    APIMapping findByField(String field);

}
