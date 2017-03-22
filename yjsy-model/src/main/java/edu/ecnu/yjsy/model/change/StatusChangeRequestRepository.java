package edu.ecnu.yjsy.model.change;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import edu.ecnu.yjsy.model.student.Student;

import java.util.List;

/**
 * FIXME 如果 status 的编码变化了，那么就会出现数据不一致的问题
 */

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface StatusChangeRequestRepository
        extends JpaRepository<StatusChangeRequest, Long> {

    // FIXME
    // 要查一下具体的语法，好像不记得是不是这么用了
    List<StatusChangeRequest> findByStudent_sno(String sno);

    @Query("from StatusChangeRequest where status=?1 and student=?2")
    List<StatusChangeRequest> findByAuditstatusAndStudent(AuditStatus status,
            Student student);

    // --------------

    @Modifying
    @Transactional
    @Query(value = "update StatusChangeRequest set status=0 where id=?1")
    void accept(long id);

    @Modifying
    @Transactional
    @Query(value = "update StatusChangeRequest set status=1 where id=?1")
    void reject(long id);

    @Modifying
    @Transactional
    @Query(value = "update StatusChangeRequest set status=2 where id=?1")
    void auditing(long id);

    @Modifying
    @Transactional
    @Query(value = "update StatusChangeRequest set status=3 where id=?1")
    void waiting(long id);

}
