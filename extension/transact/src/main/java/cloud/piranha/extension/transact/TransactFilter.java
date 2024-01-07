/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.extension.transact;

import static ee.omnifish.transact.api.ComponentInvocation.ComponentInvocationType.SERVLET_INVOCATION;

import java.io.IOException;

import ee.omnifish.transact.api.ComponentInvocation;
import ee.omnifish.transact.api.Globals;
import ee.omnifish.transact.api.InvocationManager;
import ee.omnifish.transact.api.impl.ComponentInvocationImpl;
import ee.omnifish.transact.api.spi.ServiceLocator;
import jakarta.inject.Inject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;

public class TransactFilter extends HttpFilter {

    private static final long serialVersionUID = 1L;

    /**
     * Injects ServiceLocator
     */
    @Inject
    private ServiceLocator serviceLocator;

    /**
     * Injects InvocationManager
     */
    @Inject
    private InvocationManager invocationManager;

    /**
     * Injects TransactionManager
     */
    @Inject
    private TransactionManager transactionManager;

    @Override
    public void init() throws ServletException {
        Globals.setDefaultServiceLocator(serviceLocator);
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        ComponentInvocation componentInvocation = new ComponentInvocationImpl(this, SERVLET_INVOCATION);
        try {
            invocationManager.preInvoke(componentInvocation);

            super.doFilter(req, res, chain);
        } finally {
            invocationManager.postInvoke(componentInvocation);

            if (transactionManager != null) {
                try {
                    if (transactionManager.getTransaction() != null) {
                        transactionManager.rollback();
                    }
                } catch (IllegalStateException | SecurityException | SystemException e) {
                    throw new IllegalStateException(e);
                }

            }
        }

    }

}
