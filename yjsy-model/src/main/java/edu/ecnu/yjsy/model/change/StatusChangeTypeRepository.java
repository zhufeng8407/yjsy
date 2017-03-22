package edu.ecnu.yjsy.model.change;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface StatusChangeTypeRepository
        extends JpaRepository<StatusChangeType, Long> {

    List<StatusChangeType> findByOrderByMajorAscMinorAsc();

    StatusChangeType findById(Long id);
    
    StatusChangeType findByMinor(String minor);
    
    @Query(value = "select s from StatusChangeType s where s.majorCode =?1 and s.minorCode = ?2")
    StatusChangeType findByMajorCodeAndMinorCode(short majorCode, short minorCode);

    // ------------------

    // FIXME
    // 建议返回代码和名称
    @Query(value = "select distinct major from StatusChangeType order by major")
    List<String> findMajor();
    
    @Query(value = "select distinct new StatusChangeType(s.major, s.majorCode) from StatusChangeType s")
    List<StatusChangeType> findMajorCodeAndName();

    // FIXME
    // 建议返回代码和名称
    @Query(value = "select majorCode from StatusChangeType where major=?1")
    short findMajorCodeByMajor(String major);

    // FIXME
    // @see StatusChangeType
    // 使用自增长模式就不需要了
    @Query(value = "select max(majorCode) from StatusChangeType")
    short findMaxMajorCode();

    // FIXME
    // 建议返回代码和名称
    @Query(value = "select minor from StatusChangeType order by minor")
    List<String> findMinor();

    // FIXME
    // 建议返回代码和名称
    @Query(value = "select minor from StatusChangeType where major=?1 order by minor")
    List<String> findMinorByMajor(String major);
    
    @Query(value = "select new StatusChangeType(s.minorCode, s.minor) from StatusChangeType s where s.majorCode=?1 order by s.minorCode")
    List<StatusChangeType> findMinorByMajorCode(short majorCode);

    // FIXME
    // 建议返回代码和名称
    @Query(value = "select minorCode from StatusChangeType where minor=?1")
    short findMinorCodeByMinor(String minor);

    // FIXME
    // @see StatusChangeType
    // 使用自增长模式就不需要了
    @Query(value = "select max(minorCode) from StatusChangeType where major=?1")
    short findMaxMinorCodeByMajor(String major);

    @Query(value = "select t.myConstraint from StatusChangeType t where t.minor=?1")
    String findMyConstraint(String minor);

    @Query(value = "select t.memo from StatusChangeType t where t.minor=?1")
    String findMemo(String minor);

    @Query(value = "select t.isVisible from StatusChangeType t where t.minor=?1")
    String findIsVisible(String minor);

    @Query(value = "select t.isUploadFile from StatusChangeType t where t.minor=?1")
    String findIsUploadFile(String minor);

}
