package edu.ecnu.yjsy.model.student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface UniversityRepository extends JpaRepository<University, Long> {

    University findByName(String name);

    University findByCode(String code);
    
    @Query("select new University(id,name) from University where name like ?1")
    List<University> findIdAndName(String bachelorUnitName);
}
