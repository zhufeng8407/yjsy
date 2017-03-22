package edu.ecnu.yjsy.model.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author guhang
 */

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface ExamRepository extends JpaRepository<Exam, Integer> {

    // --------------

    @Modifying
    @Transactional
    @Query("delete from Exam where id =?1")
    public void deleteById(long id);

    @Query(value = "select test from Exam")
    public List<String> findTests();

    @Query(value = "select * from t_xj_rxcs order by id desc limit ?1,?2",
            nativeQuery = true)
    public List<Exam> findExamsByRange(int begin, int end);

    public List<Exam> findAllByTest(String string);

}
