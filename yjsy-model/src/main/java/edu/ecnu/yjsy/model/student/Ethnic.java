package edu.ecnu.yjsy.model.student;

import static edu.ecnu.yjsy.model.constant.Column.META_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_NAME;
import static edu.ecnu.yjsy.model.constant.Table.ETHNIC;

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
@Table(name = ETHNIC,
        uniqueConstraints = { @UniqueConstraint(columnNames = { META_CODE }),
                @UniqueConstraint(columnNames = { META_NAME }) })
public class Ethnic extends MetaCode {

    /**
     * 
     */
    private static final long serialVersionUID = 6362382996649741822L;

    public Ethnic() {

    }

    public Ethnic(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
