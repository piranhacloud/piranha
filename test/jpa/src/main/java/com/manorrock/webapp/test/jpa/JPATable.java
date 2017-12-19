/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp.test.jpa;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A JPA table.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Entity
public class JPATable implements Serializable {

    /**
     * Stores the id (primary key).
     */
    @Id
    private Long id;

    /**
     * Get the id.
     *
     * @return the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id.
     *
     * @param id the id.
     */
    public void setId(Long id) {
        this.id = id;
    }
}
