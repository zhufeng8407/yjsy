package edu.ecnu.yjsy.model.student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface DifficultyRepository extends JpaRepository<Difficulty, Long> {

    // ------------

    @Modifying
    @Transactional
    @Query("delete from Difficulty where id =?1")
    public void deleteById(long id);
    
    Difficulty findByStudentAndYear(Student student, String year);

    // 查找学年为了前台显示
    @Query(value = "select distinct year from Difficulty")
    List<String> getYear();   
}
