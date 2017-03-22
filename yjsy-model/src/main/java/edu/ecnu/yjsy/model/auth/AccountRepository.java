package edu.ecnu.yjsy.model.auth;

import java.util.List;

import edu.ecnu.yjsy.model.staff.Staff;
import edu.ecnu.yjsy.model.student.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface AccountRepository extends JpaRepository<Account, Long> {

    @EntityGraph(value = "account.all", type = EntityGraphType.FETCH)
    @Query("from Account a where username=?1")
    public Account findOne(String username);

    @EntityGraph(value = "account.staff", type = EntityGraphType.FETCH)
    @Query("from Account where staff != null")
    public List<Account> findAllwithIdStaff();

    @EntityGraph(value = "account.student", type = EntityGraphType.FETCH)
    @Query("from Account where student != null")
    public List<Account> findAllwithIdStudent();

    @EntityGraph(attributePaths = {"staff", "student", "roles"}, type = EntityGraphType.FETCH)
    @Query("from Account where username=?1")
    public Account findOneWithStaffAndStudentByusername(String username);

    @EntityGraph(attributePaths = {"roles"})
    public Account findByStaff(Staff staff);

    public Account findByStudent(Student student);

}
