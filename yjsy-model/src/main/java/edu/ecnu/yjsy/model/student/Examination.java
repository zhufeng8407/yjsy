package edu.ecnu.yjsy.model.student;

import static edu.ecnu.yjsy.model.constant.Column.META_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_NAME;
import static edu.ecnu.yjsy.model.constant.Table.EXAMINATION;

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
@Table(name = EXAMINATION,
        uniqueConstraints = { @UniqueConstraint(columnNames = { META_CODE }),
                @UniqueConstraint(columnNames = { META_CODE, META_NAME }) })
public class Examination extends MetaCode {

    /**
     * 
     */
    private static final long serialVersionUID = 7370858892522889474L;

    public Examination() {

    }

    public Examination(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
