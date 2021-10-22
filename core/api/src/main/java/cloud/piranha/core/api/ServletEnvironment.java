/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.api;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletRegistration.Dynamic;

/**
 * The environment for a Servlet.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface ServletEnvironment extends Dynamic, ServletConfig {

    /**
     * Defines the UNAVAILABLE constant.
     */
    int UNAVAILABLE = -1;


    /**
     * {@return the load on startup}
     */
    int getLoadOnStartup();

    /**
     * {@return the multi-part config}
     */
    MultipartConfigElement getMultipartConfig();

    /**
     * {@return the servlet}
     */
    Servlet getServlet();


    /**
     * {@return the servlet class}
     */
    Class<? extends Servlet> getServletClass();

    /**
     * {@return the status}
     */
    int getStatus();

    /**
     * {@return the web application}
     */
    WebApplication getWebApplication();

    /**
     * Is async supported.
     *
     * @return true if it is, false otherwise.
     */
    boolean isAsyncSupported();


    /**
     * Set the class name.
     *
     * @param className the class name.
     */
    void setClassName(String className);

    /**
     * Set the servlet.
     *
     * @param servlet the servlet.
     */
    void setServlet(Servlet servlet);

    /**
     * Set the status.
     *
     * @param status the status.
     */
    void setStatus(int status);

    /**
     * The exception that caused this servlet to become unavailable
     *
     * @return the exception.
     */
    Throwable getUnavailableException();

    /**
     * Sets the exception that caused this servlet to become unavailable
     *
     * @param unavailableException the unavailable exception.
     */
    void setUnavailableException(Throwable unavailableException);

}