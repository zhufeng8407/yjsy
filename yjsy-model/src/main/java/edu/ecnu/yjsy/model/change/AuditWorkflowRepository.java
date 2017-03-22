package edu.ecnu.yjsy.model.change;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface AuditWorkflowRepository
        extends JpaRepository<AuditWorkflow, Long> {
	
    List<AuditWorkflow> findByType(StatusChangeType type);
    
    @Modifying
    @Transactional
    @Query(value="delete from t_yd_lc where fk_yd_lx = ?1", nativeQuery = true)
    void deleteFlowsByType(long lxId);

}
