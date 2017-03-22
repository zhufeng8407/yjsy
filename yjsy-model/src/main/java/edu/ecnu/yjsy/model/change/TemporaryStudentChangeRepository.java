package edu.ecnu.yjsy.model.change;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TemporaryStudentChangeRepository
        extends JpaRepository<TemporaryStudentChange, Long> {

    @Query(value = "select * from t_ls_xj_yd where fk_xj=?1 and del_flg=?2",
            nativeQuery = true)
    List<TemporaryStudentChange> getStudentChangeList(Long studentID,
            String delFlg);

    @Query(value = "select * from t_ls_xj_yd where fk_yd_sq=?1 and del_flg=?2",
            nativeQuery = true)
    List<TemporaryStudentChange> getStudentChangeByStatusChangeRequest(
            Long statusChangeRequestId, String delFlg);

    @Modifying
    @Transactional
    @Query(value = "update TemporaryStudentChange set delFlg=?2 where fk_yd_sq=?1")
    void delTemporaryStudentChange(Long statusChangeRequestId, String delFlg);
}
