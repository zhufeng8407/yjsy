package edu.ecnu.yjsy.model.student;

import static edu.ecnu.yjsy.model.constant.Column.META_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_NAME;
import static edu.ecnu.yjsy.model.constant.Table.GENDER;

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
@Table(name = GENDER,
        uniqueConstraints = { @UniqueConstraint(columnNames = { META_CODE }),
                @UniqueConstraint(columnNames = { META_NAME }) })
public class Gender extends MetaCode {

    /**
     * 
     */
    private static final long serialVersionUID = 5730937256515047636L;

    public Gender() {

    }

    public Gender(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
