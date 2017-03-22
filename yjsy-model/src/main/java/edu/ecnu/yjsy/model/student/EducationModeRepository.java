package edu.ecnu.yjsy.model.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

// FIXME: QUERY BY DEGREE TYPE: 1XX, 2XX AND 3XX

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface EducationModeRepository
        extends JpaRepository<EducationMode, Long> {

    EducationMode findByName(String name);

    EducationMode findByCode(String code);

}
