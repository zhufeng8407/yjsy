package edu.ecnu.yjsy.model.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface UnitRepository extends JpaRepository<Unit, Long> {

    List<Unit> findByDivision(String division);

    List<Unit> findBySchool(String school);

    Unit findByDepartmentCode(String departmentCode);

    @Query(value = "select distinct school from Unit where schoolCode=?1")
    String getSchoolName(String schoolCode);

    @Query("select distinct department from Unit where departmentCode=?1")
    String getDepartmentName(String departmentCode);

    @Query(value = "select distinct school, schoolCode from Unit where (schoolType='教学实体' or schoolType='教学虚体') and school != ''")
    List<Unit> findSchools();

    @Query(value = "select department, departmentCode, id from Unit where schoolCode=?1 and department != ''")
    List<Unit> findDepartments(String schoolCode);

}
