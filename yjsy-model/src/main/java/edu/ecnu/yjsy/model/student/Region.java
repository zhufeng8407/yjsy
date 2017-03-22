package edu.ecnu.yjsy.model.student;

import static edu.ecnu.yjsy.model.constant.Column.META_CITY;
import static edu.ecnu.yjsy.model.constant.Column.META_CITY_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_COUNTY;
import static edu.ecnu.yjsy.model.constant.Column.META_COUNTY_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_NATION;
import static edu.ecnu.yjsy.model.constant.Column.META_STATE;
import static edu.ecnu.yjsy.model.constant.Column.META_STATE_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_ZIPCODE;
import static edu.ecnu.yjsy.model.constant.Table.REGION;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import edu.ecnu.yjsy.model.EntityId;

/**
 * @author songshubin
 * @author xiafan
 * @author xulinhao
 */

@Entity
@Table(name = REGION,
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = { META_STATE, META_CITY, META_COUNTY }),
                @UniqueConstraint(columnNames = { META_STATE_CODE,
                        META_CITY_CODE, META_COUNTY_CODE }) })
public class Region extends EntityId {

    /**
     * 
     */
    private static final long serialVersionUID = 386733980082025593L;

    @Column(name = META_NATION, nullable = false, length = 40)
    private String country;

    @Column(name = META_STATE, nullable = false, length = 40)
    private String state;

    @Column(name = META_STATE_CODE, nullable = false, length = 6)
    private String stateCode;

    @Column(name = META_CITY, nullable = false, length = 40)
    private String city;

    @Column(name = META_CITY_CODE, nullable = false, length = 6)
    private String cityCode;

    @Column(name = META_COUNTY, nullable = false, length = 40)
    private String county;

    @Column(name = META_COUNTY_CODE, nullable = false, length = 6)
    private String countyCode;

    @Column(name = META_ZIPCODE, length = 6)
    private String zipcode;

    // --------------------

    public Region() {

    }

    public Region(String country, String state, String stateCode, String city,
            String cityCode, String county, String countyCode, String zipcode) {
        this.country = country;
        this.state = state;
        this.stateCode = stateCode;
        this.city = city;
        this.cityCode = cityCode;
        this.county = county;
        this.countyCode = countyCode;
        this.zipcode = zipcode;
    }

    // --------------------

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return state + city + county;
    }
    

}
