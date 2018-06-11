/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
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
package com.manorrock.piranha;

/**
 * The WebApplicationBuilder API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Deprecated
public interface WebApplicationBuilder {

    /**
     * Build the web application.
     *
     * @return the web application.
     */
    WebApplication build();

    /**
     * Set the class loader.
     *
     * @param classLoader the class loader.
     * @return the WebApplicationBuilder.
     */
    WebApplicationBuilder classLoader(ClassLoader classLoader);

    /**
     * Set the context path.
     *
     * @param contextPath the context path.
     * @return the WebApplicationBuilder.
     */
    WebApplicationBuilder contextPath(String contextPath);

    /**
     * Add the servlet.
     *
     * @param servletName the servlet name.
     * @param className the class name.
     * @return
     */
    WebApplicationBuilder servlet(String servletName, String className);

    /**
     * Add the servlet mapping.
     *
     * @param servletName the servlet name.
     * @param urlPatterns the URL patterns.
     * @return the WebApplicationBuilder.
     */
    WebApplicationBuilder servletMapping(String servletName, String... urlPatterns);
}
