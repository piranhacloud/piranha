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

import java.util.HashMap;
import java.util.Map;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

/**
 * The default TransactionManager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultTransactionManager implements TransactionManager {

    /**
     * Stores the timeout.
     */
    private int timeout;

    /**
     * Stores the thread-to-transaction map.
     */
    private final Map<Thread, Transaction> threadTransactionMap;

    /**
     * Constructor.
     */
    public DefaultTransactionManager() {
        threadTransactionMap = new HashMap<>();
    }

    /**
     * Begin a transaction.
     *
     * @throws NotSupportedException when a nested transaction is attempted.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public void begin() throws NotSupportedException, SystemException {
        DefaultTransaction transaction = (DefaultTransaction) getTransaction();
        if (transaction == null) {
            transaction = new DefaultTransaction();
            transaction.setTransactionManager(this);
            transaction.setTimeout(timeout);
            Thread currentThread = Thread.currentThread();
            threadTransactionMap.put(currentThread, transaction);
        } else {
            throw new NotSupportedException("Nested transactions are not supported");
        }
    }

    /**
     * Commit a transaction.
     *
     * @throws RollbackException when a rollback error occurs.
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
        Transaction transaction = getTransaction();
        try {
            transaction.commit();
        } finally {
            Thread currentThread = Thread.currentThread();
            threadTransactionMap.remove(currentThread);
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
        int result;
        Transaction transaction = getTransaction();
        if (transaction != null) {
            result = transaction.getStatus();
        } else {
            result = Status.STATUS_NO_TRANSACTION;
        }
        return result;
    }

    /**
     * Get the transaction.
     *
     * @return the transaction, or null if not found.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public Transaction getTransaction() throws SystemException {
        Transaction result = null;
        Thread currentThread = Thread.currentThread();
        if (threadTransactionMap.containsKey(currentThread)) {
            result = threadTransactionMap.get(currentThread);
        }
        return result;
    }

    /**
     * Resume the transaction.
     *
     * @param transaction the transaction.
     * @throws InvalidTransactionException when the transaction could not be
     * found.
     * @throws IllegalStateException when the transaction is not active.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public void resume(Transaction transaction)
            throws InvalidTransactionException, IllegalStateException,
            SystemException {
        Transaction activeTransaction = getTransaction();
        if (activeTransaction != null) {
            throw new IllegalStateException();
        } else if (!transaction.getClass().isAssignableFrom(DefaultTransaction.class)) {
            throw new InvalidTransactionException();
        } else {
            Thread currentThread = Thread.currentThread();
            threadTransactionMap.put(currentThread, transaction);
        }
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
        Transaction transaction = getTransaction();
        try {
            transaction.rollback();
        } finally {
            Thread currentThread = Thread.currentThread();
            threadTransactionMap.remove(currentThread);
        }
    }

    /**
     * Set the rollback only.
     *
     * @throws IllegalStateException when the transaction is not active.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        Transaction transaction = getTransaction();
        transaction.setRollbackOnly();
    }

    /**
     * Set the transaction timeout.
     *
     * @param timeout the timeout.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public void setTransactionTimeout(int timeout) throws SystemException {
        this.timeout = timeout;
    }

    /**
     * Suspend the transaction.
     *
     * @return the transaction.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public Transaction suspend() throws SystemException {
        Thread thread = Thread.currentThread();
        return threadTransactionMap.remove(thread);
    }
}
