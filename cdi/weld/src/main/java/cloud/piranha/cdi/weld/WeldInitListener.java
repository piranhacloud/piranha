/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.cdi.weld;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;

import org.jboss.weld.environment.servlet.Listener;
import org.jboss.weld.servlet.api.ServletListener;
import org.jboss.weld.servlet.api.helpers.ForwardingServletListener;

import cloud.piranha.api.CurrentRequestHolder;

/**
 * This Piranha specific Weld initializer forwards all initialization
 * to the original Weld initializer, but modifies the <code>HttpServletRequest</code>
 * that's passed into it.
 * 
 * <p>
 * The purpose of this is making sure Weld is able to access the <em>current</em> 
 * <code>HttpServletRequest</code> as that changes throughout the request processing
 * pipeline.
 * 
 * @see WeldHttpServletRequest
 * @see CurrentRequestHolder
 * 
 * @author Arjan Tijms
 *
 */
public class WeldInitListener extends ForwardingServletListener {
    
    private ServletListener weldTargetListener = new Listener();
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Do nothing
    }
    
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        super.requestInitialized(new ServletRequestEvent(
            sre.getServletContext(), 
            new WeldHttpServletRequest((HttpServletRequest)sre.getServletRequest())));
    }

    @Override
    public ServletListener delegate() {
        return weldTargetListener;
    }

}
