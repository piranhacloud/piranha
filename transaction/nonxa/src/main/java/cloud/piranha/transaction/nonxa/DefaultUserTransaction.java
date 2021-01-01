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
package cloud.piranha.transaction.nonxa;

import java.io.Serializable;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;

/**
 * The default UserTransaction.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultUserTransaction implements Serializable, UserTransaction {

    /**
     * Stores the serial version UID.
     */
    private static final long serialVersionUID = 1484494589933854686L;

    /**
     * Stores the transaction manager.
     */
    private final TransactionManager transactionManager;

    /**
     * Constructor.
     *
     * @param transactionManager the transaction manager.
     */
    public DefaultUserTransaction(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * Begin a transaction.
     *
     * @throws NotSupportedException when a nested transaction is attempted.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public void begin() throws NotSupportedException, SystemException {
        transactionManager.begin();
    }

    /**
     * Commit the transaction.
     *
     * @throws RollbackException when a roll back error occurs.
     * @throws HeuristicMixedException when heuristics are being mixed.
     * @throws HeuristicRollbackException when a rollback error occurs.
     * @throws SecurityException when a security error occurs.
     * @throws IllegalStateException when the transaction is not active.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public void commit() throws RollbackException, HeuristicMixedException,
            HeuristicRollbackException, SecurityException,
            IllegalStateException, SystemException {
        if (transactionManager.getTransaction() != null) {
            transactionManager.commit();
        } else {
            throw new IllegalStateException("Thread not associated with a transaction");
        }
    }

    /**
     * Get the status.
     *
     * @return the status.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public int getStatus() throws SystemException {
        return transactionManager.getStatus();
    }

    /**
     * Rollback the transaction.
     *
     * @throws IllegalStateException when the transaction is not active.
     * @throws SecurityException when a security error occurs.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public void rollback() throws IllegalStateException, SecurityException,
            SystemException {
        transactionManager.rollback();
    }

    /**
     * Set rollback only.
     *
     * @throws IllegalStateException when the transaction is not active.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        transactionManager.setRollbackOnly();
    }

    /**
     * Set the transaction timeout.
     *
     * @param timeout the timeout.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public void setTransactionTimeout(int timeout) throws SystemException {
        transactionManager.setTransactionTimeout(timeout);
    }
}
