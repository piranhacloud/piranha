/*
 * Copyright (c) 2002-2023 Manorrock.com. All Rights Reserved.
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

package cloud.piranha.extension.eclipselink.wrappers;

import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;

/**
* Provides a convenient implementation of the EntityManager interface that can be subclassed by developers wishing
* to adapt the entity manager.
*
* <p>
* This class implements the Wrapper or Decorator pattern. Methods default to calling through to the wrapped request
* object.
*
*/
public class EntityManagerWrapper implements EntityManager {

    /**
     * The entity manager that we wrap and delegate to
     */
    private EntityManager wrappedEntityManager;

    /**
     */
    public EntityManagerWrapper() {
    }

    /**
     *
     * @param entityManager The entity manager to be wrapped
     */
    public EntityManagerWrapper(EntityManager entityManager) {
        this.wrappedEntityManager = entityManager;
    }

    /**
     * The underlying wrapped EntityManager
     * @return underlying wrapped EntityManager 
     */
    public EntityManager getWrapped() {
        return wrappedEntityManager;
    }

    @Override
    public void persist(Object entity) {
        getWrapped().persist(entity);
    }

    @Override
    public <T> T merge(T entity) {
        return getWrapped().merge(entity);
    }

    @Override
    public void remove(Object entity) {
        getWrapped().remove(entity);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey) {
        return getWrapped().find(entityClass, primaryKey);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
        return getWrapped().find(entityClass, primaryKey, properties);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
        return getWrapped().find(entityClass, primaryKey, lockMode);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
        return getWrapped().find(entityClass, primaryKey, lockMode, properties);
    }

    @Override
    public void flush() {
        getWrapped().flush();
    }

    @Override
    public Query createQuery(CriteriaUpdate updateQuery) {
        return getWrapped().createQuery(updateQuery);
    }

    @Override
    public Query createQuery(CriteriaDelete deleteQuery) {
        return getWrapped().createQuery(deleteQuery);
    }

    @Override
    public Query createQuery(String ejbqlString) {
        return getWrapped().createQuery(ejbqlString);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String ejbqlString, Class<T> resultClass) {
        return getWrapped().createQuery(ejbqlString, resultClass);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return getWrapped().createQuery(criteriaQuery);
    }

    @Override
    public Query createNamedQuery(String name) {
        return getWrapped().createNamedQuery(name);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
        return getWrapped().createNamedQuery(name, resultClass);
    }

    @Override
    public Query createNativeQuery(String sqlString) {
        return getWrapped().createNativeQuery(sqlString);
    }

    @Override
    public Query createNativeQuery(String sqlString, Class resultClass) {
        return getWrapped().createNativeQuery(sqlString, resultClass);
    }

    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        return getWrapped().createNativeQuery(sqlString, resultSetMapping);
    }

    @Override
    public void refresh(Object entity) {
        getWrapped().refresh(entity);
    }

    @Override
    public void refresh(Object entity, Map<String, Object> properties) {
        getWrapped().refresh(entity, properties);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode) {
        getWrapped().refresh(entity, lockMode);
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        getWrapped().refresh(entity, lockMode, properties);
    }

    @Override
    public LockModeType getLockMode(Object o) {
        return getWrapped().getLockMode(o);
    }

    @Override
    public void detach(Object o) {
        getWrapped().detach(o);
    }

    @Override
    public void close() {
        getWrapped().close();
    }

    @Override
    public boolean isOpen() {
        return getWrapped().isOpen();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return getWrapped().getEntityManagerFactory();
    }

    @Override
    public void joinTransaction() {
        getWrapped().joinTransaction();
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
        return getWrapped().createNamedStoredProcedureQuery(name);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
        return getWrapped().createStoredProcedureQuery(procedureName);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
        return getWrapped().createStoredProcedureQuery(procedureName, resultClasses);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
        return getWrapped().createStoredProcedureQuery(procedureName, resultSetMappings);
    }

    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        return getWrapped().getReference(entityClass, primaryKey);
    }

    @Override
    public void setProperty(String propertyName, Object value) {
        getWrapped().setProperty(propertyName, value);
    }

    @Override
    public Map<String, Object> getProperties() {
        return getWrapped().getProperties();
    }

    @Override
    public EntityTransaction getTransaction() {
        return getWrapped().getTransaction();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return getWrapped().getCriteriaBuilder();
    }

    @Override
    public boolean contains(Object entity) {
        return getWrapped().contains(entity);
    }

    @Override
    public Metamodel getMetamodel() {
        return getWrapped().getMetamodel();
    }

    @Override
    public void lock(Object entity, LockModeType lockMode) {
        getWrapped().lock(entity, lockMode);
    }

    @Override
    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        getWrapped().lock(entity, lockMode, properties);
    }

    @Override
    public void clear() {
        getWrapped().clear();
    }

    @Override
    public Object getDelegate() {
        return getWrapped().getDelegate();
    }

    @Override
    public FlushModeType getFlushMode() {
        return getWrapped().getFlushMode();
    }

    @Override
    public void setFlushMode(FlushModeType flushMode) {
        getWrapped().setFlushMode(flushMode);
    }

    @Override
    public <T> T unwrap(Class<T> tClass) {
        return getWrapped().unwrap(tClass);
    }

    @Override
    public boolean isJoinedToTransaction() {
        return getWrapped().isJoinedToTransaction();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
        return getWrapped().createEntityGraph(rootType);
    }

    @Override
    public EntityGraph<?> createEntityGraph(String graphName) {
        return getWrapped().createEntityGraph(graphName);
    }

    @Override
    public EntityGraph<?> getEntityGraph(String graphName) {
        return getWrapped().getEntityGraph(graphName);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
        return getWrapped().getEntityGraphs(entityClass);
    }

}
