/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package cloud.piranha.extension.eclipselink;

import static jakarta.persistence.PersistenceContextType.TRANSACTION;
import static jakarta.persistence.SynchronizationType.SYNCHRONIZED;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.SynchronizationType;
import jakarta.persistence.TransactionRequiredException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transaction;
import jakarta.transaction.TransactionManager;

/**
 * Entity Manager wrapper suitable for injection that creates the actual entity manager lazily on demand.
 *
 */
public class PiranhaEntityManager extends EntityManagerWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Stores the unit name
     */
    private String unitName = "";

    /**
     * (note extended not yet supported)
     */
    private PersistenceContextType contextType = TRANSACTION;

    /**
     * Stores the properties
     */
    private Map<Object, Object> properties = new HashMap<>();

    /**
     * (note UNSYNCHRONIZED not yet supported)
     */
    private SynchronizationType synchronizationType = SYNCHRONIZED;

    /**
     * Stores the entityManagerFactory
     */
    private transient EntityManagerFactory entityManagerFactory;

    /**
     * Stores the transactionManager
     */
    private transient TransactionManager transactionManager;

    /**
     *
     * @param unitName
     * @param contextType
     * @param synchronizationType
     * @param properties
     */
    public PiranhaEntityManager(String unitName, PersistenceContextType contextType, SynchronizationType synchronizationType, Map<Object, Object> properties) {
        this.unitName = unitName;
        this.contextType = contextType;
        this.synchronizationType = synchronizationType;
        this.properties = properties;
    }

    @Override
    public EntityManager getWrapped() {
        EntityManager wrappedEntityManager = null;

        if (contextType == PersistenceContextType.TRANSACTION) {

            Transaction tx = getCurrentTransaction();

            if (tx != null) {
                // Create an entity manager. Note the entity manager needs to be closed at some point still.
                wrappedEntityManager = getEntityManagerFactory().createEntityManager(synchronizationType, properties);
            }

            // Note the non-transactional entity manager is not supported yet, but should be.
        }

        return wrappedEntityManager;
    }


    // ### Checked methods

    @Override
    public void persist(Object entity) {
        tryCheckTransactionActive();

        super.persist(entity);
    }

    @Override
    public <T> T merge(T entity) {
        tryCheckTransactionActive();

        return getWrapped().merge(entity);
    }

    @Override
    public void remove(Object entity) {
        tryCheckTransactionActive();

        getWrapped().remove(entity);
    }

    @Override
    public void flush() {
        checkTransactionActive();

        getWrapped().flush();
    }

    @Override
    public void refresh(Object entity) {
        tryCheckTransactionActive();

        getWrapped().refresh(entity);
    }

    @Override
    public void refresh(Object entity, Map<String, Object> properties) {
        tryCheckTransactionActive();

        getWrapped().refresh(entity, properties);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode) {
        tryCheckTransactionActive();

        getWrapped().refresh(entity, lockMode);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        tryCheckTransactionActive();

        getWrapped().refresh(entity, lockMode, properties);
    }

    @Override
    public LockModeType getLockMode(Object o) {
        checkTransactionActive();

        return getWrapped().getLockMode(o);
    }

    @Override
    public void detach(Object o) {
        tryCheckTransactionActive();

        getWrapped().detach(o);
    }

    @Override
    public void joinTransaction() {
        checkTransactionActive();

        getWrapped().joinTransaction();
    }



    // ### Clear context methods


    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey) {
        EntityManager wrappedEntityManager = getWrapped();
        T returnValue = wrappedEntityManager.find(entityClass, primaryKey);
        clearDetachedEntityManager(wrappedEntityManager);
        return returnValue;
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
        EntityManager wrappedEntityManager = getWrapped();
        T returnValue = wrappedEntityManager.find(entityClass, primaryKey, properties);
        clearDetachedEntityManager(wrappedEntityManager);

        return returnValue;
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
        EntityManager wrappedEntityManager = getWrapped();
        T returnValue = wrappedEntityManager.find(entityClass, primaryKey, lockMode);
        clearDetachedEntityManager(wrappedEntityManager);

        return returnValue;
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
        EntityManager wrappedEntityManager = getWrapped();
        T returnValue = wrappedEntityManager.find(entityClass, primaryKey, lockMode, properties);
        clearDetachedEntityManager(wrappedEntityManager);

        return returnValue;
    }


    // ### Result wrapping methods

    @Override
    public Query createQuery(String ejbqlString) {
        EntityManager wrappedEntityManager = getWrapped();
        Query returnValue = wrappedEntityManager.createQuery(ejbqlString);

        return wrapQueryIfDetached(returnValue, wrappedEntityManager);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String ejbqlString, Class<T> resultClass) {
        EntityManager wrappedEntityManager = getWrapped();
        TypedQuery<T> returnValue = wrappedEntityManager.createQuery(ejbqlString, resultClass);

        return wrapQueryIfDetached(returnValue, wrappedEntityManager);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        EntityManager wrappedEntityManager = getWrapped();
        TypedQuery<T> returnValue = wrappedEntityManager.createQuery(criteriaQuery);

        return wrapQueryIfDetached(returnValue, wrappedEntityManager);
    }

    @Override
    public Query createNamedQuery(String name) {
        EntityManager wrappedEntityManager = getWrapped();
        Query returnValue = wrappedEntityManager.createNamedQuery(name);

        return wrapQueryIfDetached(returnValue, wrappedEntityManager);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
        EntityManager wrappedEntityManager = getWrapped();
        TypedQuery<T> returnValue = wrappedEntityManager.createNamedQuery(name, resultClass);

        return wrapQueryIfDetached(returnValue, wrappedEntityManager);
    }

    @Override
    public Query createNativeQuery(String sqlString) {
        EntityManager wrappedEntityManager = getWrapped();
        Query returnValue = wrappedEntityManager.createNativeQuery(sqlString);

        return wrapQueryIfDetached(returnValue, wrappedEntityManager);
    }

    @Override
    public Query createNativeQuery(String sqlString, Class resultClass) {
        EntityManager wrappedEntityManager = getWrapped();
        Query returnValue = wrappedEntityManager.createNativeQuery(sqlString, resultClass);

        return wrapQueryIfDetached(returnValue, wrappedEntityManager);
    }

    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        EntityManager wrappedEntityManager = getWrapped();
        Query returnValue = wrappedEntityManager.createNativeQuery(sqlString, resultSetMapping);

        return wrapQueryIfDetached(returnValue, wrappedEntityManager);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
        EntityManager wrappedEntityManager = getWrapped();
        StoredProcedureQuery returnValue = wrappedEntityManager.createNamedStoredProcedureQuery(name);

        return wrapQueryIfDetached(returnValue, wrappedEntityManager);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
        EntityManager wrappedEntityManager = getWrapped();
        StoredProcedureQuery returnValue = wrappedEntityManager.createStoredProcedureQuery(procedureName);

        return wrapQueryIfDetached(returnValue, wrappedEntityManager);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
        EntityManager wrappedEntityManager = getWrapped();
        StoredProcedureQuery returnValue = wrappedEntityManager.createStoredProcedureQuery(procedureName, resultClasses);

        return wrapQueryIfDetached(returnValue, wrappedEntityManager);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
        EntityManager wrappedEntityManager = getWrapped();
        StoredProcedureQuery returnValue = wrappedEntityManager.createStoredProcedureQuery(procedureName, resultSetMappings);

        return wrapQueryIfDetached(returnValue, wrappedEntityManager);
    }


    // ### Other methods

    @Override
    public void close() {
        throw new IllegalStateException();
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            entityManagerFactory = CDI.current()
                    .select(EntityManagerFactoryCreator.class)
                    .get()
                    .get(unitName);
        }

        return entityManagerFactory;
    }

    @Override
    public Object getDelegate() {
        return getWrapped();
    }


    // ### Private methods

    private void tryCheckTransactionActive() {
        if (contextType != TRANSACTION) {
            return;
        }

        checkTransactionActive();
    }

    private void checkTransactionActive() {
        if (getCurrentTransaction() == null) {
            throw new TransactionRequiredException();
        }
    }

    private Transaction getCurrentTransaction() {
        try {
            return getTransactionManager().getTransaction();
        } catch (Exception e) {
            throw new IllegalStateException("Error getting current transaction", e);
        }
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }

    private TransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = CDI.current().select(TransactionManager.class).get();
        }

        return transactionManager;
    }

    private boolean isDetached() {
        return getCurrentTransaction() == null && contextType != PersistenceContextType.EXTENDED;
    }

    private StoredProcedureQuery wrapQueryIfDetached(StoredProcedureQuery query, EntityManager delegate) {
        return isDetached() ? query : query;
    }

    private <T> TypedQuery<T> wrapQueryIfDetached(TypedQuery<T> query, EntityManager delegate) {
        return isDetached() ? query : query;
    }

    private Query wrapQueryIfDetached(Query query, EntityManager delegate) {
        return isDetached() ? query : query;
    }

    private void clearDetachedEntityManager(EntityManager entityManager) {
        if (isDetached()) {
            entityManager.clear();
        }
    }

}
