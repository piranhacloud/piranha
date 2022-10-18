/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.extension.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.sql.XAConnection;
import javax.sql.XADataSource;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transaction;
import jakarta.transaction.TransactionManager;

/**
 * Data source wrapper that adds connections retrieved from it to any ongoing transaction.
 *
 * @author Arjan Tijms
 *
 */
public class TxJoiningDataSource extends DataSourceWrapper {

    /**
     * Stores the transactionManager
     */
    private transient TransactionManager transactionManager;

    /**
     * Creates a wrapper with the given data source
     *
     * @param dataSource the data source being wrapped
     */
    public TxJoiningDataSource(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Connection getConnection() throws SQLException {
        try {
            Transaction transaction = getTransactionManager().getTransaction();
            if (transaction != null) {

                DataSource dataSource = getWrapped();
                if (dataSource instanceof XADataSource xaDataSource) {
                    XAConnection xaConnection = xaDataSource.getXAConnection();
                    transaction.enlistResource(xaConnection.getXAResource());

                    return xaConnection.getConnection();
                }
            }

            return super.getConnection();

        } catch (SystemException | IllegalStateException | RollbackException e) {
            throw new SQLException(e);
        }
    }


    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        try {
            Transaction transaction = getTransactionManager().getTransaction();
            if (transaction != null) {

                DataSource dataSource = getWrapped();
                if (dataSource instanceof XADataSource xaDataSource) {
                    XAConnection xaConnection = xaDataSource.getXAConnection(username, password);
                    transaction.enlistResource(xaConnection.getXAResource());

                    return xaConnection.getConnection();
                }
            }

            return super.getConnection(username, password);

        } catch (SystemException | IllegalStateException | RollbackException e) {
            throw new SQLException(e);
        }
    }

    /**
     * Attempt to get a non-transactional connection from this data source.
     *
     * @return a connection that is not explicitly added to any ongoing transaction.
     * @throws SQLException when something goes wrong
     */
    public Connection getNonTxConnection() throws SQLException {
        return super.getConnection();
    }

    /**
     * Attempt to get a non-transactional connection from this data source.
     *
     * @param username name to login to the database
     * @param password password to use to login to the database
     * @return a connection that is not explicitly added to any ongoing transaction.
     * @throws SQLException when something goes wrong
     */
    public Connection getNonTxConnection(String username, String password) throws SQLException {
        return super.getConnection(username, password);
    }

    private TransactionManager getTransactionManager() {
        if (transactionManager == null) {
            transactionManager = CDI.current().select(TransactionManager.class).get();
        }

        return transactionManager;
    }

}
