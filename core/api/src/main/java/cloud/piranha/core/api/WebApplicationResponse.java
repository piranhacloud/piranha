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
package cloud.piranha.core.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * The WebApplicationResponse API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface WebApplicationResponse extends HttpServletResponse {

    /**
     * Close the async response.
     */
    void closeAsyncResponse();

    /**
     * Get the content language.
     * 
     * @return the content language.
     */
    String getContentLanguage();

    /**
     * {@return the cookies}
     */
    Collection<Cookie> getCookies();
    
    /**
     * {@return the response closer}
     */
    Runnable getResponseCloser();

    /**
     * Get the status message.
     * 
     * @return the status message.
     */
    String getStatusMessage();
    
    /**
     * Get the web application.
     * 
     * @return the web application.
     */
    WebApplication getWebApplication();

    /**
     * {@return the web application output stream}
     */
    WebApplicationOutputStream getWebApplicationOutputStream();
    
    /**
     * Are we in body only mode.
     *
     * @return true if we are only sending the body, false otherwise.
     */
    boolean isBodyOnly();

    /**
     * Is the buffer resetting.
     * 
     * @return true if it is, false otherwise.
     */
    boolean isBufferResetting();
    
    /**
     * Set the body only mode.
     * 
     * @param bodyOnly the body only mode.
     */
    void setBodyOnly(boolean bodyOnly);

    /**
     * Set the response closer.
     *
     * @param responseCloser the response closer.
     */
    void setResponseCloser(Runnable responseCloser);
        
    /**
     * Set the web application.
     *
     * @param webApplication the web application.
     */
    void setWebApplication(WebApplication webApplication);
    
    /**
     * Set the web application output stream.
     *
     * @param outputStream the web application output stream.
     */
    void setWebApplicationOutputStream(WebApplicationOutputStream outputStream);
}
