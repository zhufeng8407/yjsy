package edu.ecnu.yjsy.model.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * FIXME: USE CODE TO QUERY
 * 
 * @author xulinhao
 *
 */

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface RegionRepository extends JpaRepository<Region, Long> {
    
    Region findByCountyCode(String countyCode);

    // ----------------------------

    @Query(value = "select distinct state, stateCode from Region")
    List<Region> findStates();

    @Query(value = "select distinct city, cityCode from Region where stateCode=?1")
    List<Region> findCities(String stateCode);

   
    @Query(value = "select id,county,countyCode from Region where cityCode=?1")
    List<Region> findCounties(String cityCode);

}
