/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package com.manorrock.piranha.cdi.weld;

import static cloud.piranha.api.CurrentRequestHolder.CURRENT_REQUEST_ATTRIBUTE;

import javax.servlet.http.HttpServletRequest;

import cloud.piranha.DefaultCurrentRequestHolder;
import cloud.piranha.api.CurrentRequestHolder;

/**
 * An HttpServletRequest wrapper that always delegates every operation to what has been set as
 * the <em>current</em> request.
 * 
 * <p>
 * This allows Weld to hold on to a single HttpServletRequest instance, which can then be set to
 * point to another HttpServletRequest instance as the current one that the request uses changes.
 * 
 * <p>
 * This instance changes for example after an authentication module or filter has provided a new
 * HttpServletRequest, or when a dispatch or include is performed.
 * 
 * @author Arjan Tijms
 *
 */
public class WeldHttpServletRequest extends RealtimeHttpServletRequestWrapper {
    
    private final CurrentRequestHolder currentRequestHolder;

    public WeldHttpServletRequest(HttpServletRequest request) {
        currentRequestHolder = new DefaultCurrentRequestHolder(request);
        request.setAttribute(CURRENT_REQUEST_ATTRIBUTE, currentRequestHolder);
    }
    
    @Override
    protected HttpServletRequest getWrapped() {
        return currentRequestHolder.getRequest();
    }
    
}
