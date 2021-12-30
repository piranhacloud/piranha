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
package cloud.piranha.extension.weld.servlet;

import cloud.piranha.core.api.WebApplication;
import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.WARNING;
import org.jboss.weld.environment.servlet.Container;
import org.jboss.weld.environment.servlet.ContainerContext;
import org.jboss.weld.resources.spi.ResourceLoader;

/**
 * The Weld container.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WeldServletContainer implements Container {

    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(WeldServletContainer.class.getName());

    @Override
    public void destroy(ContainerContext context) {
        LOGGER.log(DEBUG, "Destroying WeldServletContainer");
        // nothing to do here.
    }

    @Override
    public void initialize(ContainerContext context) {
        LOGGER.log(DEBUG, "Initializing WeldServletContainer");
        try {
            WebApplication webApplication = (WebApplication) context.getServletContext();
            WeldObjectInstanceManager manager = new WeldObjectInstanceManager();
            manager.setManager(context.getManager());
            webApplication.getManager().setObjectInstanceManager(manager);
        } catch (Exception e) {
            LOGGER.log(WARNING, "Exception occurred while creating WeldServletContainer");
        }
    }

    @Override
    public boolean touch(ResourceLoader resourceLoader, ContainerContext context) throws Exception {
        LOGGER.log(DEBUG, "Should we use the WeldServletContainer");
        return true;
    }
}
