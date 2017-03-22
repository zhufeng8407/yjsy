package edu.ecnu.yjsy.model.student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import edu.ecnu.yjsy.model.view.student.StudentAuthSummary;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findBySno(String sno);

    // 写query的方法，当collection包含不止1个元素时，会报错
    // @EntityGraph(value = "student.staff.unit", type =
    // EntityGraph.EntityGraphType.LOAD)
    // @Query(value = "select id, supervisor, unit from Student as student where
    // student.id=?1")
    StudentAuthSummary findStudentBySno(String sno);

    List<String> findSnoByName(String name);

    // 写query的方法，当collection包含不止1个元素时，会报错
    // @EntityGraph(value = "student.staff.unit", type =
    // EntityGraph.EntityGraphType.LOAD)
    // @Query(value = "select student.id, student.supervisor, student.unit from
    // Student as student left join fetch student.supervisor left join fetch
    // student.unit where student.id=?1")
    StudentAuthSummary findStudentById(Long id);

    List<Student> findByLeave(boolean leave);

    List<Student> findByIsNew(boolean isNew);

    // TODO: this query needs to be cached as it is frequently
    // used by the security module
    String findSnoById(long id);

    long findIdBySno(String sno);
    // ----------------

    @Query(value = "select distinct grade from Student")
    List<Short> findGrades();

    @Modifying
    @Query("UPDATE Student student SET student.isNew = :isNew WHERE student.id = :studentId")
    int updateIsNew(long studentId, boolean isNew);

    @Query(value = "select * from t_xj left join t_account on t_xj.id = t_account.fk_xs where t_account.id =?1",
            nativeQuery = true)
    Student findByAccountId(long accountId);

}
