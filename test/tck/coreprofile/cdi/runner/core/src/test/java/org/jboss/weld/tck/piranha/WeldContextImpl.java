/*
 * Copyright (c) 2024 Manorrock.com. All Rights Reserved.
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

package org.jboss.weld.tck.piranha;

import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.ServletContext;

import org.jboss.cdi.tck.spi.Contexts;
import org.jboss.weld.Container;
import org.jboss.weld.context.ApplicationContext;
import org.jboss.weld.context.DependentContext;
import org.jboss.weld.context.ManagedContext;
import org.jboss.weld.context.RequestContext;
import org.jboss.weld.context.http.HttpRequestContext;
import org.jboss.weld.util.ForwardingContext;

public class WeldContextImpl implements Contexts<Context> {

    @Override
    public void setActive(Context context) {
        context = ForwardingContext.unwrap(context);
        if (context instanceof ManagedContext managedContext) {
            managedContext.activate();
        } else if (context instanceof ApplicationContext) {
            // No-op, always active
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void setInactive(Context context) {
        context = ForwardingContext.unwrap(context);
        if (context instanceof ManagedContext managedContext) {
            managedContext.deactivate();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public RequestContext getRequestContext() {
        return
            Container.instance(getContextId())
                     .deploymentManager()
                     .instance()
                     .select(HttpRequestContext.class)
                     .get();
    }

    @Override
    public DependentContext getDependentContext() {
        return
            Container.instance(getContextId())
                     .deploymentManager().instance()
                     .select(DependentContext.class)
                     .get();
    }

    @Override
    public void destroyContext(Context context) {
        context = ForwardingContext.unwrap(context);
        if (context instanceof ManagedContext managedContext) {
            managedContext.invalidate();
            managedContext.deactivate();
            managedContext.activate();
        } else if (context instanceof ApplicationContext applicationContext) {
            applicationContext.invalidate();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private String getContextId() {
        ServletContext servletContext = CDI.current().select(ServletContext.class).get();

        return servletContext.getInitParameter("WELD_CONTEXT_ID_KEY");
    }
}
