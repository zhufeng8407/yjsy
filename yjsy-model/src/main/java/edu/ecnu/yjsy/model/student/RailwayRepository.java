package edu.ecnu.yjsy.model.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface RailwayRepository extends JpaRepository<Railway, Long> {

    Railway findByStateAndName(String state, String name);
    
    Railway findByName(String name);
    

    List<Railway> findByState(String state);

    List<Railway> findByCode(String code); // find by 火车站名首字母

    // --------------------------------

    @Query(value = "select distinct state from Railway")
    List<Railway> findStates();

    @Query(value = "select id,name from Railway where state=?1")
    List<Railway> findStations(String state);

}
