package edu.ecnu.yjsy.model.student;

import static edu.ecnu.yjsy.model.constant.Column.META_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_NAME;
import static edu.ecnu.yjsy.model.constant.Table.EDUCATION_MODE;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import edu.ecnu.yjsy.model.MetaCode;

/**
 * 已获得学历的学习形式: 本科(3XX)，硕士(1XX),博士(2XX).
 * 
 * @author songshubin
 * @author xiafan
 * @author xulinhao
 */

@Entity
@Table(name = EDUCATION_MODE,
        uniqueConstraints = { @UniqueConstraint(columnNames = { META_CODE }),
                @UniqueConstraint(columnNames = { META_CODE, META_NAME }) })
public class EducationMode extends MetaCode {

    /**
     * 
     */
    private static final long serialVersionUID = 6240293447025144064L;

    public EducationMode() {

    }

    public EducationMode(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
