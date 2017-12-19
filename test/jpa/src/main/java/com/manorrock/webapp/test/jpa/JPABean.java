/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp.test.jpa;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * A simple bean.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Named(value = "jpaBean")
@RequestScoped
public class JPABean {
    
    /**
     * Stores the entity manager factory.
     */
    private static EntityManagerFactory emf;
    
    /**
     * Get the entity manager.
     */
    public EntityManager getEntityManager() {
        synchronized(this) {
            if (emf == null) {
                emf = Persistence.createEntityManagerFactory("demo");
            }
        }
        return emf.createEntityManager();
    }
    
    /**
     * Get the count message.
     * 
     * @return the count message.
     */
    public String getMessage() {
        TypedQuery<JPATable> query = getEntityManager().createQuery("SELECT o FROM JPATable AS o", JPATable.class);
        List<JPATable> list = query.getResultList();
        return "Count: " + list.size();
    }
}
