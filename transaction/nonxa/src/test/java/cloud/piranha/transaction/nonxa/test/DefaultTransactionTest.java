/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.transaction.nonxa.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.junit.jupiter.api.Test;

import cloud.piranha.transaction.nonxa.DefaultTransaction;

/**
 * The JUnit tests for the DefaultTransaction class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultTransactionTest {

    /**
     * Test commit method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testCommit() throws Exception {
        DefaultTransaction transaction = new DefaultTransaction();
        transaction.commit();
        transaction.commit();
    }

    /**
     * Test commit method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testCommit2() throws Exception {
        DefaultTransaction transaction = new DefaultTransaction();
        transaction.rollback();
        assertThrows(RollbackException.class, transaction::commit);
    }


    /**
     * Test delistResource method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testDelistResource() throws Exception {
        DefaultTransaction transaction = new DefaultTransaction();
        XAResource xaResource = new XAResource() {
            @Override
            public void commit(Xid xid, boolean bln) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void end(Xid xid, int i) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void forget(Xid xid) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int getTransactionTimeout() throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isSameRM(XAResource xar) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int prepare(Xid xid) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Xid[] recover(int i) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void rollback(Xid xid) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean setTransactionTimeout(int i) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void start(Xid xid, int i) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        transaction.enlistResource(xaResource);
        assertTrue(transaction.delistResource(xaResource, 0));
    }

    /**
     * Test enlistResource method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testEnlistResource() throws Exception {
        DefaultTransaction transaction = new DefaultTransaction();
        transaction.setRollbackOnly();
        assertThrows(RollbackException.class, () -> transaction.enlistResource(null));
    }

    /**
     * Test enlistResource method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testEnlistResource2() throws Exception {
        DefaultTransaction transaction = new DefaultTransaction();
        assertTrue(transaction.enlistResource(new XAResource() {
            @Override
            public void commit(Xid xid, boolean bln) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void end(Xid xid, int i) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void forget(Xid xid) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int getTransactionTimeout() throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isSameRM(XAResource xar) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int prepare(Xid xid) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Xid[] recover(int i) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void rollback(Xid xid) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean setTransactionTimeout(int i) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void start(Xid xid, int i) throws XAException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }));
    }

    /**
     * Test registerSynchronization method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testRegisterSynchronization() throws Exception {
        Synchronization synchronization = new Synchronization() {

            @Override
            public void beforeCompletion() {
                System.setProperty("beforeCompletionCalled", "true");
                throw new RuntimeException("Throwing an exception here");
            }

            @Override
            public void afterCompletion(int status) {
            }
        };
        DefaultTransaction transaction = new DefaultTransaction();
        transaction.registerSynchronization(synchronization);
        transaction.commit();
        assertEquals("true", System.getProperty("beforeCompletionCalled"));
    }

    /**
     * Test registerSynchronization method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testRegisterSynchronization2() throws Exception {
        Synchronization synchronization = new Synchronization() {

            @Override
            public void beforeCompletion() {

            }

            @Override
            public void afterCompletion(int status) {
                System.setProperty("afterCompletionCalled", "true");
                throw new RuntimeException("Throwing an exception here");
            }
        };
        DefaultTransaction transaction = new DefaultTransaction();
        transaction.registerSynchronization(synchronization);
        transaction.commit();
        assertEquals("true", System.getProperty("afterCompletionCalled"));
    }
}
