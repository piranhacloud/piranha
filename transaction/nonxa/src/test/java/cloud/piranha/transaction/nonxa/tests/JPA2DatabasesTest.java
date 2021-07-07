/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice, 
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its 
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package cloud.piranha.transaction.nonxa.tests;

import javax.naming.InitialContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transaction;
import org.h2.jdbcx.JdbcDataSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import cloud.piranha.transaction.nonxa.DefaultTransactionManager;

/**
 * s
 * A JPA database test.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class JPA2DatabasesTest {

    /**
     * Test database.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testJpaDatabase() throws Exception {
        System.getProperties().put("java.naming.factory.initial", "cloud.piranha.naming.impl.DefaultInitialContextFactory");
        InitialContext initialContext = new InitialContext();
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:./target/JPADatabaseTest2");
        initialContext.bind("java:/JPADatabaseTest2", dataSource);
        JdbcDataSource dataSource2 = new JdbcDataSource();
        dataSource2.setURL("jdbc:h2:./target/JPADatabaseTest3");
        initialContext.bind("java:/JPADatabaseTest3", dataSource2);
        DefaultTransactionManager transactionManager = new DefaultTransactionManager();
        initialContext.rebind("java:/TransactionManager", transactionManager);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPADatabaseTest2");
        EntityManagerFactory emf2 = Persistence.createEntityManagerFactory("JPADatabaseTest3");
        transactionManager.begin();
        EntityManager em = emf.createEntityManager();
        JPATestModel jpaTest = new JPATestModel();
        jpaTest.setId(1);
        em.persist(jpaTest);
        em.flush();
        Transaction transaction = transactionManager.getTransaction();
        assertEquals(0, transaction.getStatus());
        EntityManager em2 = emf2.createEntityManager();
        JPATestModel jpaTest2 = new JPATestModel();
        jpaTest2.setId(2);
        em2.persist(jpaTest2);
        em2.flush();
        Transaction transaction2 = transactionManager.getTransaction();
        assertEquals(0, transaction2.getStatus());
        transactionManager.commit();
        
        assertEquals(3, transaction.getStatus());
        em = emf.createEntityManager();
        JPATestModel jpaTestb = em.find(JPATestModel.class, 1);
        assertNotNull(jpaTestb);
        assertEquals(jpaTest.getId(), jpaTestb.getId());
        assertEquals(3, transaction2.getStatus());
        em2 = emf2.createEntityManager();
        JPATestModel jpaTest2b = em2.find(JPATestModel.class, 2);
        assertNotNull(jpaTest2b);
        assertEquals(jpaTest2b.getId(), jpaTest2.getId());
        
        em.close();
        emf.close();
        em2.close();
        emf2.close();
    }
}
