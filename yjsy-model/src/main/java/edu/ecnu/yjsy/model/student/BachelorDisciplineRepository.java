package edu.ecnu.yjsy.model.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xulinhao
 */

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface BachelorDisciplineRepository
        extends JpaRepository<BachelorDiscipline, Long> {

    BachelorDiscipline findByMinorCode(String minorCode);

    // -------------------------------------

    @Query(value = "select distinct category, categoryCode from BachelorDiscipline")
    List<BachelorDiscipline> findCategories();

    @Query(value = "select distinct major, majorCode from BachelorDiscipline where categoryCode=?1")
    List<BachelorDiscipline> findMajors(String categoryCode);

    @Query(value = "select id, minor, minorCode from BachelorDiscipline where majorCode=?1")
    List<BachelorDiscipline> findMinors(String majorCode);

}
