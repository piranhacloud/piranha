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

import static java.lang.System.Logger.Level.DEBUG;

import java.lang.System.Logger;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Reference;

import cloud.piranha.core.api.WebApplication;
import ee.omnifish.transact.cdi.beans.JndiToCdiReference;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

/**
 * The Transact initializer.
 *
 * @author Arjan Tijms
 */
public class TransactInitializer implements ServletContainerInitializer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(TransactInitializer.class.getName());

    /**
     * Initialize Transact.
     *
     * @param classes the classes.
     * @param servletContext the Servlet context.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        WebApplication application = (WebApplication) servletContext;

        LOGGER.log(DEBUG, "Initializing Transact");

        if (!isCDIEnabled()) {
            return;
        }

        Reference reference = new JndiToCdiReference();

        try {
            new InitialContext().bind("java:comp/UserTransaction", reference);
            new InitialContext().bind("java:appserver/TransactionManager", reference);
            new InitialContext().bind("java:comp/TransactionSynchronizationRegistry", reference);
        } catch (NamingException e) {
            throw new ServletException(e);
        }


        FilterRegistration.Dynamic dynamic = application.addFilter(TransactFilter.class.getSimpleName(), TransactFilter.class);
        dynamic.setAsyncSupported(true);
        application.addFilterMapping(TransactFilter.class.getSimpleName(), false, "/*");

        System.setProperty("ee.omnifish.transact.jts.dblogging.use.nontx.connection.for.add", "true");

        LOGGER.log(DEBUG, "Initialized Transact");
    }

    private boolean isCDIEnabled() {
        try {
            CDI.current();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }



}
