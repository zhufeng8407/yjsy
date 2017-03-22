package edu.ecnu.yjsy.model.pregraduation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface PregraduateRequstRepository
        extends JpaRepository<PregraduateRequst, Long> {

    @Query(value = "from PregraduateRequst where fieldDisplayName=?1 order by createDate desc")
    List<PregraduateRequst> findByFieldDisplayName(String fieldDisplayName);

    @Query("select requst from PregraduateRequst as requst join requst.student as stu "
            + "where stu.sno=?1 and requst.fieldDisplayName=?2 and requst.auditDate = null "
            + "order by requst.auditDate desc")
    List<PregraduateRequst> findBySnoAndField(String sno, String field);

    @Query("select requst from PregraduateRequst as requst join requst.student as stu "
            + "where stu.sno=?1 and requst.fieldDisplayName=?2 and requst.createDate != null "
            + "order by requst.id desc")
    List<PregraduateRequst> findBySnoAndFieldAndCreatedate(String sno,
            String field);

}
