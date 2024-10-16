/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.extension.jersey;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import java.lang.System.Logger;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationExtension;

import static java.lang.System.Logger.Level.TRACE;

/**
 * The extension that delivers Eclipse Jersey to Piranha.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class JerseyExtension implements WebApplicationExtension, Extension {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(JerseyExtension.class.getName());

    /**
     * Constructor.
     */
    public JerseyExtension() {
    }
    
    /**
     * Configure the extension.
     *
     * @param webApplication the web application.
     */
    @Override
    public void configure(WebApplication webApplication) {
        LOGGER.log(TRACE, "Configuring Jersey extension");
    }

    /**
     * Register a source and target bean to force the adding of a class analyzer.
     * 
     * @param beforeBeanDiscovery the BeforeBeanDiscovery.
     * @param beanManager the BeanManager.
     */
    public void register(@Observes BeforeBeanDiscovery beforeBeanDiscovery, 
            BeanManager beanManager) {

        LOGGER.log(TRACE, "Registering beans to force adding of class analyzer");

        /*
         * Force a class analyzer to be added that makes sure a REST resource is
         * not attempted to be injected by both CDI and HK2.
         *
         * See https://github.com/eclipse-ee4j/jersey/issues/5745 on why this is
         * needed.
         */
        addAnnotatedTypes(beforeBeanDiscovery, beanManager, 
                JerseyTargetBean.class, JerseySourceBean.class);
    }

    /**
     * Add annotated types.
     * 
     * @param beforeBeanDiscovery the BeforeBeanDiscovery. 
     * @param beanManager the BeanManager.
     * @param types the types to add.
     */
    public static void addAnnotatedTypes(BeforeBeanDiscovery beforeBeanDiscovery, 
            BeanManager beanManager, Class<?>... types) {

        LOGGER.log(TRACE, "Adding annotated types");

        for (Class<?> type : types) {
            beforeBeanDiscovery.addAnnotatedType(
                    beanManager.createAnnotatedType(type), 
                    "JerseyExtension " + type.getName());
        }
    }
}
