package edu.ecnu.yjsy.model.student;

import static edu.ecnu.yjsy.model.constant.Column.META_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_NAME;
import static edu.ecnu.yjsy.model.constant.Table.DEGREE_LEVEL;

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
@Table(name = DEGREE_LEVEL,
        uniqueConstraints = { @UniqueConstraint(columnNames = { META_CODE }),
                @UniqueConstraint(columnNames = { META_NAME }) })
public class DegreeLevel extends MetaCode {

    /**
     * 
     */
    private static final long serialVersionUID = 6180160547687556126L;

    public DegreeLevel() {

    }

    public DegreeLevel(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
