package edu.ecnu.yjsy.model.staff;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface StaffRepository extends JpaRepository<Staff, Long> {

    public Staff findBySno(String sno);

    public Staff findById(Long id);

    @Query("select sno from Staff where id=?1")
    String findSnoById(long auditorStaff);

    @Query("select id from Staff where sno=?1")
    long findIdBySno(String sno);

    @Query("select staff from Account as account join account.staff as staff where account.id=?1")
    public Staff findByAccountId(Long accountId);

    @Query("select new Staff(id,name) from Staff where name like ?1")
    List<Staff> findIdAndName(String supervisor);

}
