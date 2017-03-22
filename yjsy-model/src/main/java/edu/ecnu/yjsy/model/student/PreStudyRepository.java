package edu.ecnu.yjsy.model.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface PreStudyRepository extends JpaRepository<PreStudy, Long> {

    PreStudy findByName(String name);

    PreStudy findByCode(String code);
}
