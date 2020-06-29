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
package cloud.piranha.transaction.nonxa;

import org.junit.jupiter.api.Test;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAResource;

/**
 * The JUnit tests for the DefaultTransactionManager class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultTransactionManagerTest {

    /**
     * Test suspend method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testSuspend() throws Exception {
        DefaultTransactionManager txManager = new DefaultTransactionManager();
        txManager.begin();
        Transaction tx = txManager.suspend();
        txManager.resume(tx);
    }

    /**
     * Test suspend method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalStateException.class)
    public void testSuspend2() throws Exception {
        DefaultTransactionManager txManager = new DefaultTransactionManager();
        txManager.begin();
        Transaction tx = txManager.getTransaction();
        txManager.resume(tx);
    }
    
    /**
     * Test suspend method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = InvalidTransactionException.class)
    public void testSuspend3() throws Exception {
        DefaultTransactionManager txManager = new DefaultTransactionManager();
        txManager.resume(new Transaction() {
            @Override
            public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
            }

            @Override
            public boolean delistResource(XAResource xaRes, int flag) throws IllegalStateException, SystemException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean enlistResource(XAResource xaRes) throws RollbackException, IllegalStateException, SystemException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int getStatus() throws SystemException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void registerSynchronization(Synchronization sync) throws RollbackException, IllegalStateException, SystemException {
            }

            @Override
            public void rollback() throws IllegalStateException, SystemException {
            }

            @Override
            public void setRollbackOnly() throws IllegalStateException, SystemException {
            }
        });
    }
}
