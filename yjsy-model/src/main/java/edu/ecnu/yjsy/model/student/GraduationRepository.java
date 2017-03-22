package edu.ecnu.yjsy.model.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface GraduationRepository extends JpaRepository<Graduation, Long> {

    Graduation findByName(String name);

    Graduation findByCode(String code);
}
