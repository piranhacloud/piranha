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

import java.util.ArrayList;
import java.util.List;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.xa.XAResource;

/**
 * The default Transaction.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultTransaction implements Transaction {

    /**
     * Stores the status.
     */
    private int status;

    /**
     * Stores the synchronizations.
     */
    private final List<Synchronization> synchronizations;

    /**
     * Stores the timeout.
     */
    private int timeout;

    /**
     * Stores the transaction manager.
     */
    private TransactionManager transactionManager;

    /**
     * Stores the XA resources.
     */
    private ArrayList<XAResource> xaResources = new ArrayList<>();

    /**
     * Constructor.
     */
    public DefaultTransaction() {
        this.synchronizations = new ArrayList<>();
        this.status = Status.STATUS_ACTIVE;
    }

    /**
     * Commit the transaction.
     *
     * @throws RollbackException when a rollback error occurs.
     * @throws HeuristicMixedException when the heuristics were mixed.
     * @throws HeuristicRollbackException when a rollback error occurs.
     * @throws SecurityException when a security error occurs.
     * @throws IllegalStateException when the transaction is not active.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public synchronized void commit() throws RollbackException, HeuristicMixedException,
            HeuristicRollbackException, SecurityException,
            IllegalStateException, SystemException {
        handleBeforeCompletion();
        try {
            switch (status) {
                case Status.STATUS_COMMITTED:
                    break;
                case Status.STATUS_MARKED_ROLLBACK: {
                    rollback();
                    throw new HeuristicRollbackException();
                }
                case Status.STATUS_ROLLEDBACK: {
                    throw new RollbackException();
                }
                default: {
                    status = Status.STATUS_COMMITTED;
                }
            }
        } finally {
            handleAfterCompletion();
        }
    }

    /**
     * Delist the resource.
     *
     * @param xaResource the XA resource.
     * @param flags the flags.
     * @return true when delisted, false otherwise.
     * @throws IllegalStateException when the transaction is not active.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public boolean delistResource(XAResource xaResource, int flags)
            throws IllegalStateException, SystemException {
        xaResources.remove(xaResource);
        return true;
    }

    /**
     * Enlist the resource.
     *
     * @param xaResource the XA resource.
     * @return true if enlisted, false otherwise.
     * @throws RollbackException when transaction has already been marked for
     * rollback.
     * @throws IllegalStateException when the transaction is not active.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public boolean enlistResource(XAResource xaResource)
            throws RollbackException, IllegalStateException, SystemException {
        if (status == Status.STATUS_MARKED_ROLLBACK) {
            throw new RollbackException("Transaction has already been marked for rollback");
        }
        xaResources.add(xaResource);
        return true;
    }

    /**
     * Get the status.
     *
     * @return the status.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public int getStatus() throws SystemException {
        return status;
    }

    /**
     * A private method that handles calling the afterCompletion method on each
     * synchronization registered.
     */
    private void handleAfterCompletion() {
        if (synchronizations.size() > 0) {
            synchronizations.forEach(synchronization -> {
                try {
                    synchronization.afterCompletion(status);
                } catch (RuntimeException re) {
                    re.printStackTrace();
                }
            });
        }
    }

    /**
     * A private method that handles calling the beforeCompletion method on each
     * synchronization registered.
     */
    private void handleBeforeCompletion() {
        if (synchronizations.size() > 0) {
            synchronizations.forEach(synchronization -> {
                try {
                    synchronization.beforeCompletion();
                } catch (RuntimeException re) {
                    re.printStackTrace();
                }
            });
        }
    }

    /**
     * Register a synchronization.
     *
     * @param synchronization the synchronization.
     * @throws RollbackException when a rollback error occurs.
     * @throws IllegalStateException when the transaction is not active
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public void registerSynchronization(Synchronization synchronization)
            throws RollbackException, IllegalStateException, SystemException {
        synchronizations.add(synchronization);
    }

    /**
     * Rollback the transaction.
     *
     * @throws IllegalStateException when the transaction is not active.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public void rollback() throws IllegalStateException, SystemException {
        this.status = Status.STATUS_ROLLEDBACK;
    }

    /**
     * Set rollback only.
     *
     * @throws IllegalStateException when the transaction is not active.
     * @throws SystemException when a serious error occurs.
     */
    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        this.status = Status.STATUS_MARKED_ROLLBACK;
    }

    /**
     * Set the timeout.
     *
     * @param timeout the timeout.
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Set the transaction manager.
     *
     * @param transactionManager the transaction manager.
     */
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
