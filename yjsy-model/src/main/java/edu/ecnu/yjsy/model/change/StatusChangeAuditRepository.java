package edu.ecnu.yjsy.model.change;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface StatusChangeAuditRepository
        extends JpaRepository<StatusChangeAudit, Long> {

    // FIXME
    // ByRequest_id or ByRequest?
    // 需要检查到底存储的是那个 id
    @Query(value = " select * from t_yd_sp where fk_yd_sq = ?1 ",
            nativeQuery = true)
    List<StatusChangeAudit> findByRequest_id(long id);

    // -----------------

    // FIXME
    // 如果 status 的编码发生了变化怎么办？
    // 20170112 modified by zhufeng
    @Modifying
    @Transactional
    @Query(value = "update StatusChangeAudit set status=?2, auditAt=?3 where id=?1")
    void accept(long id, AuditStatus status, Timestamp auditDate);

    @Modifying
    // 20170112 modified by zhufeng
    @Transactional
    @Query(value = "update StatusChangeAudit set status=?2, auditAt=?3 where id=?1")
    void reject(long id, AuditStatus status, Timestamp auditDate);

    @Modifying
    @Transactional
    @Query(value = "update StatusChangeAudit set status=?2 where id=?1")
    void auditing(long id, AuditStatus status);

    @Modifying
    @Transactional
    @Query(value = "update StatusChangeAudit set status=?2 where id=?1")
    void waiting(long id, AuditStatus status);

}
