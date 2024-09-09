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

import jakarta.el.ArrayELResolver;
import jakarta.el.BeanELResolver;
import jakarta.el.CompositeELResolver;
import jakarta.el.ELContext;
import jakarta.el.ELContextEvent;
import jakarta.el.ELContextListener;
import jakarta.el.ELResolver;
import jakarta.el.ExpressionFactory;
import jakarta.el.FunctionMapper;
import jakarta.el.ListELResolver;
import jakarta.el.MapELResolver;
import jakarta.el.ResourceBundleELResolver;
import jakarta.el.VariableMapper;
import jakarta.enterprise.inject.spi.BeanManager;

import org.jboss.cdi.tck.spi.EL;
import org.jboss.weld.bean.builtin.BeanManagerProxy;
import org.jboss.weld.manager.BeanManagerImpl;
import org.jboss.weld.module.web.el.WeldELContextListener;
import org.jboss.weld.module.web.el.WeldExpressionFactory;

public class WeldELImpl implements EL {

    private static final ExpressionFactory EXPRESSION_FACTORY = new WeldExpressionFactory(ExpressionFactory.newInstance());

    private static final ELContextListener[] EL_CONTEXT_LISTENERS = { new WeldELContextListener() };

    @Override
    @SuppressWarnings("unchecked")
    public <T> T evaluateValueExpression(BeanManager beanManager, String expression, Class<T> expectedType) {
        ELContext elContext = createELContext(beanManager);
        return (T) EXPRESSION_FACTORY.createValueExpression(elContext, expression, expectedType).getValue(elContext);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T evaluateMethodExpression(BeanManager beanManager, String expression, Class<T> expectedType,
            Class<?>[] expectedParamTypes, Object[] expectedParams) {
        ELContext elContext = createELContext(beanManager);
        return (T) EXPRESSION_FACTORY.createMethodExpression(elContext, expression, expectedType, expectedParamTypes).invoke(
                elContext, expectedParams);
    }

    @Override
    public ELContext createELContext(BeanManager beanManager) {
        if (beanManager instanceof BeanManagerProxy) {
            BeanManagerProxy proxy = (BeanManagerProxy) beanManager;
            beanManager = proxy.delegate();
        }
        if (beanManager instanceof BeanManagerImpl) {
            return createELContext((BeanManagerImpl) beanManager);
        }
        throw new IllegalStateException("Wrong manager");
    }

    private ELContext createELContext(BeanManagerImpl beanManagerImpl) {

        final ELResolver resolver = createELResolver(beanManagerImpl);

        ELContext context = new ELContext() {

            @Override
            public ELResolver getELResolver() {
                return resolver;
            }

            @Override
            public FunctionMapper getFunctionMapper() {
                return null;
            }

            @Override
            public VariableMapper getVariableMapper() {
                return null;
            }

        };
        callELContextListeners(context);
        return context;
    }

    private ELResolver createELResolver(BeanManagerImpl beanManagerImpl) {
        CompositeELResolver resolver = new CompositeELResolver();
        resolver.add(beanManagerImpl.getELResolver());
        resolver.add(new MapELResolver());
        resolver.add(new ListELResolver());
        resolver.add(new ArrayELResolver());
        resolver.add(new ResourceBundleELResolver());
        resolver.add(new BeanELResolver());
        return resolver;
    }

    private void callELContextListeners(ELContext context) {
        ELContextEvent event = new ELContextEvent(context);
        for (ELContextListener listener : EL_CONTEXT_LISTENERS) {
            listener.contextCreated(event);
        }
    }
}
