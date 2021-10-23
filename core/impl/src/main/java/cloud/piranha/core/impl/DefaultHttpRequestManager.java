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
package cloud.piranha.core.impl;

import cloud.piranha.core.api.HttpRequestManager;
import jakarta.servlet.ServletRequestAttributeEvent;
import jakarta.servlet.ServletRequestAttributeListener;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 *
 * @author Arjan Tijms
 *
 */
public class DefaultHttpRequestManager implements HttpRequestManager {

    /**
     * Stores the session listeners.
     */
    private final List<ServletRequestAttributeListener> attributeListeners = new ArrayList<>();

    @Override
    public <T extends EventListener> void addListener(T listener) {
        if (listener instanceof ServletRequestAttributeListener) {
            attributeListeners.add((ServletRequestAttributeListener) listener);
        }
    }

    @Override
    public void attributeAdded(HttpServletRequest request, String name, Object value) {
        attributeListeners.stream().forEach(listener -> listener.attributeAdded(new ServletRequestAttributeEvent(request.getServletContext(), request, name, value)));
    }

    @Override
    public void attributeRemoved(HttpServletRequest request, String name, Object value) {
        attributeListeners.stream().forEach(listener -> listener.attributeRemoved(new ServletRequestAttributeEvent(request.getServletContext(), request, name, value)));
    }

    @Override
    public void attributeReplaced(HttpServletRequest request, String name, Object value) {
        attributeListeners.stream().forEach(listener -> listener.attributeReplaced(new ServletRequestAttributeEvent(request.getServletContext(), request, name, value)));
    }

}
