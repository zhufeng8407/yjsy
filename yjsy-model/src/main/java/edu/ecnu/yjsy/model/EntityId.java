package edu.ecnu.yjsy.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Each entity of any table has a unique long identifier and
 * <code>EntityId</code> is used to provided such an ID.
 * 
 * @author xulinhao
 */

@MappedSuperclass
public abstract class EntityId implements Serializable {

    private static final long serialVersionUID = 188123671496376397L;

    @Id
    @GeneratedValue
    long id;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
