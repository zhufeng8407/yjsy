package edu.ecnu.yjsy.model.student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xulinhao
 */

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {

    Discipline findByMinorCode(String minorCode);

    // -------------------------------------

    @Query(value = "select distinct category, categoryCode from Discipline")
    List<Discipline> findCategories();

    @Query(value = "select distinct major, majorCode from Discipline where categoryCode=?1")
    List<Discipline> findMajors(String categoryCode);

    @Query(value = "select id, minor, minorCode from Discipline where majorCode=?1")
    List<Discipline> findMinors(String majorCode);

}
