package edu.ecnu.yjsy.model.student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface RegistrationRespository
        extends JpaRepository<Registration, Long> {

    Registration findByStudentAndYearAndTerm(Student student, String year, int term);

    List<Registration> findByYearAndTerm(String schoolYear, int newterm);

    // -------------

}
