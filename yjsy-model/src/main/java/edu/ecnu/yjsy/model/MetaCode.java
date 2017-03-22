package edu.ecnu.yjsy.model;

import static edu.ecnu.yjsy.model.constant.Column.META_CODE;
import static edu.ecnu.yjsy.model.constant.Column.META_NAME;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * <code>Student</code> table has many referenced metadata tables, most of which
 * contain name and code fields. <code>MetaCode</code> is used to provide a
 * basic metadata table with name and code fields.
 * 
 * @author xulinhao
 */

@MappedSuperclass
public abstract class MetaCode extends EntityId {

    /**
     * 
     */
    private static final long serialVersionUID = -5392655027484734457L;

    @Column(name = META_NAME, nullable = false, length = 40)
    protected String name;

    @Column(name = META_CODE, nullable = false, length = 10)
    protected String code;

    // --------------------
    // METHODS
    // --------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    @Override
    public String toString() {
        return name;
    }
    

}
