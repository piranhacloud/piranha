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
package jakarta.servlet;

/**
 * The ServletRequestAttributeEvent API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletRequestAttributeEvent extends ServletRequestEvent {

    /**
     * Stores the serial version UID.
     */
    private static final long serialVersionUID = -1466635426192317793L;

    /**
     * Stores the name.
     */
    private final String name;

    /**
     * Stores the value.
     */
    private transient Object value;

    /**
     * Constructor.
     *
     * @param servletContext the servlet context.
     * @param request the request.
     * @param name the name.
     * @param value the value.
     */
    public ServletRequestAttributeEvent(ServletContext servletContext, ServletRequest request, String name, Object value) {
        super(servletContext, request);
        this.name = name;
        this.value = value;
    }

    /**
     * {@return the name}
     */
    public String getName() {
        return name;
    }

    /**
     * {@return the value}
     */
    public Object getValue() {
        return this.value;
    }
}
