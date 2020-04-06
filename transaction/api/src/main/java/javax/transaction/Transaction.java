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
package javax.transaction;

import javax.transaction.xa.XAResource;

/**
 * The Transaction API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface Transaction {

    /**
     * Commit the transaction.
     *
     * @throws RollbackException when the transaction has been rolled back.
     * @throws HeuristicMixedException when part of the transaction has been
     * rolled back.
     * @throws HeuristicRollbackException when all of the transaction has been
     * rolled back.
     * @throws SecurityException when it was not allowed to commit.
     * @throws IllegalStateException when the transaction is inactive.
     * @throws SystemException when a serious error occurs.
     */
    void commit() throws RollbackException, HeuristicMixedException,
            HeuristicRollbackException, SecurityException,
            IllegalStateException, SystemException;

    /**
     * Delist the XA resource.
     *
     * @param xaResource the XA resource.
     * @param flag the TMxxx flag
     * @return true if de-listed successfully, false otherwise.
     * @throws IllegalStateException when the transaction is inactive.
     * @throws SystemException when a serious error occurs.
     */
    boolean delistResource(XAResource xaResource, int flag)
            throws IllegalStateException, SystemException;

    /**
     * Enlist the XA resource.
     *
     * @param xaResource the XA resource.
     * @return true if en-listed successfully, false otherwise.
     * @throws RollbackException when the transaction has been marked for
     * rollback.
     * @throws IllegalStateException when the transaction is inactive.
     * @throws SystemException when a serious error occurs.
     */
    boolean enlistResource(XAResource xaResource) throws RollbackException,
            IllegalStateException, SystemException;

    /**
     * Get the status.
     *
     * @return the status of the transaction.
     * @throws SystemException when a serious error occurs.
     */
    int getStatus() throws SystemException;

    /**
     * Register the synchronization object.
     *
     * @param synchronization the synchronization object.
     * @throws RollbackException when the transaction has been marked for
     * rollback.
     * @throws IllegalStateException when the transaction is inactive.
     * @throws SystemException when a serious error occurs.
     */
    void registerSynchronization(Synchronization synchronization)
            throws RollbackException, IllegalStateException, SystemException;

    /**
     * Rollback the transaction.
     *
     * @throws IllegalStateException when the transaction is inactive.
     * @throws SystemException when a serious error occurs.
     */
    void rollback() throws IllegalStateException, SystemException;

    /**
     * Set for rollback only.
     *
     * @throws IllegalStateException when the transaction is inactive.
     * @throws SystemException when a serious error occurs.
     */
    void setRollbackOnly() throws IllegalStateException, SystemException;
}
