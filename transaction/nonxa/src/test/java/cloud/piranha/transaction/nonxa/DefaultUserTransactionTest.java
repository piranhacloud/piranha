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

import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.Status;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the DefaultUserTransaction class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultUserTransactionTest {

    /**
     * Test begin method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = NotSupportedException.class)
    public void testBegin() throws Exception {
        DefaultUserTransaction transaction = new DefaultUserTransaction(
                new DefaultTransactionManager());
        transaction.begin();
        transaction.begin();
    }

    /**
     * Test commit method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testCommit() throws Exception {
        DefaultUserTransaction transaction = new DefaultUserTransaction(
                new DefaultTransactionManager());
        transaction.begin();
        transaction.commit();
        assertEquals(Status.STATUS_NO_TRANSACTION, transaction.getStatus());
    }

    /**
     * Test commit method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalStateException.class)
    public void testCommit2() throws Exception {
        DefaultUserTransaction transaction = new DefaultUserTransaction(
                new DefaultTransactionManager());
        transaction.begin();
        transaction.commit();
        transaction.commit();
    }
    
    /**
     * Test getStatus method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetStatus() throws Exception {
        DefaultUserTransaction transaction = new DefaultUserTransaction(
                new DefaultTransactionManager());
        assertEquals(Status.STATUS_NO_TRANSACTION, transaction.getStatus());
    }

    /**
     * Test getStatus method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetStatus2() throws Exception {
        DefaultUserTransaction transaction = new DefaultUserTransaction(
                new DefaultTransactionManager());
        transaction.begin();
        assertEquals(Status.STATUS_ACTIVE, transaction.getStatus());
    }

    /**
     * Test rollback method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testRollback() throws Exception {
        DefaultUserTransaction transaction = new DefaultUserTransaction(
                new DefaultTransactionManager());
        transaction.begin();
        transaction.rollback();
    }

    /**
     * Test setRollbackOnly method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = HeuristicRollbackException.class)
    public void testSetRollbackOnly() throws Exception {
        DefaultUserTransaction transaction = new DefaultUserTransaction(
                new DefaultTransactionManager());
        transaction.begin();
        transaction.setRollbackOnly();
        transaction.commit();
    }
    
    /**
     * Test setTransactionTimeout method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testSetTransactionTimeout() throws Exception {
        DefaultUserTransaction transaction = new DefaultUserTransaction(
                new DefaultTransactionManager());
        transaction.begin();
        transaction.setTransactionTimeout(1000);
    }
}
