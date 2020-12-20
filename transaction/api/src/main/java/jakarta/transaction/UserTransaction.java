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
package jakarta.transaction;

/**
 * The UserTransaction API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface UserTransaction {

    /**
     * Begin the transaction.
     *
     * @throws NotSupportedException when nested transaction are not supported.
     * @throws SystemException when a serious error occurs.
     */
    void begin() throws NotSupportedException, SystemException;

    /**
     * Commit the transaction.
     *
     * @throws RollbackException when the transaction has been rolled back.
     * @throws HeuristicMixedException when transaction has been partially
     * rolled back.
     * @throws HeuristicRollbackException when transaction has been rolled back.
     * @throws SecurityException when not allowed to commit the transaction.
     * @throws IllegalStateException when not associated with a transaction.
     * @throws SystemException when a serious error occurs.
     */
    void commit() throws RollbackException, HeuristicMixedException,
            HeuristicRollbackException, SecurityException,
            IllegalStateException, SystemException;

    /**
     * Get the status.
     *
     * @return the status.
     * @throws SystemException when a serious error occurs.
     */
    int getStatus() throws SystemException;

    /**
     * Rollback the transaction.
     *
     * @throws IllegalStateException when not associated with a transaction.
     * @throws SecurityException when not allowed to rollback the transaction.
     * @throws SystemException when a serious error occurs.
     */
    void rollback() throws IllegalStateException, SecurityException,
            SystemException;

    /**
     * Set to rollback only.
     *
     * @throws IllegalStateException when not associated with a transaction.
     * @throws SystemException when a serious error occurs.
     */
    void setRollbackOnly() throws IllegalStateException, SystemException;

    /**
     * Set the transaction timeout.
     *
     * @param seconds the transaction timeout in seconds
     * @throws SystemException when a serious error occurs.
     */
    void setTransactionTimeout(int seconds) throws SystemException;
}
