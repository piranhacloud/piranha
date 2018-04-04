/*
 *  Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.webapp.weld;

import com.manorrock.webapp.ObjectInstanceManager;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.Unmanaged;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

/**
 * The Weld object instnace manager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WeldObjectInstanceManager implements ObjectInstanceManager {

    /**
     * Create the servlet.
     *
     * @param <T>
     * @param type the servlet class.
     * @return the servlet.
     * @throws ServletException
     */
    @Override
    public <T extends Servlet> T createServlet(Class<T> type) throws ServletException {
        T result = null;
        boolean constructed = false;

        try {
            BeanManager beanManager = CDI.current().getBeanManager();
            Unmanaged<T> unmanaged = new Unmanaged(beanManager, type);
            Unmanaged.UnmanagedInstance<T> unmanagedInstance = unmanaged.newInstance();
            result = unmanagedInstance.produce().inject().postConstruct().get();
            constructed = true;
        } catch (Throwable throwable) {
        }

        if (!constructed) {
            try {
                result = type.newInstance();
            } catch (InstantiationException | IllegalAccessException exception) {
                throw new ServletException(exception);
            }
        }

        return result;
    }
}
