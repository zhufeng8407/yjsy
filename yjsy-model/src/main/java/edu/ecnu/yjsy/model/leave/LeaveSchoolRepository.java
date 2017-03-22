package edu.ecnu.yjsy.model.leave;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface LeaveSchoolRepository
        extends JpaRepository<LeaveSchool, Long> {

    public LeaveSchool findById(Long id);
}
