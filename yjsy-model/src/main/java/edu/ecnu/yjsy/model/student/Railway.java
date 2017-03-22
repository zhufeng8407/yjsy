package edu.ecnu.yjsy.model.student;

import static edu.ecnu.yjsy.model.constant.Column.META_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_NAME;
import static edu.ecnu.yjsy.model.constant.Column.META_SPELLING;
import static edu.ecnu.yjsy.model.constant.Column.META_STATE;
import static edu.ecnu.yjsy.model.constant.Table.RAILWAY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import edu.ecnu.yjsy.model.MetaCode;

/**
 * @author songshubin
 * @author xiafan
 * @author xulinhao
 */

@Entity
@Table(name = RAILWAY, uniqueConstraints = {
        @UniqueConstraint(columnNames = { META_CODE, META_NAME, META_STATE }) })
public class Railway extends MetaCode {

    /**
     * 
     */
    private static final long serialVersionUID = -8917375806990927307L;

    // 省份
    @Column(name = META_STATE, nullable = false, length = 40)
    private String state;

    // 火车站名
    // name - mc

    // 火车站名首字母
    // code - dm

    // 拼音
    @Column(name = META_SPELLING, nullable = false, length = 40)
    private String spelling;

    // --------------------

    public Railway() {

    }

    public Railway(String state, String name, String code, String spelling) {
        this.state = state;
        this.name = name;
        this.code = code;
        this.spelling = spelling;
    }

    // --------------------

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSpelling() {
        return spelling;
    }

    public void setSpelling(String spelling) {
        this.spelling = spelling;
    }

    @Override
    public String toString() {
        return state + name;
    }

    
}
