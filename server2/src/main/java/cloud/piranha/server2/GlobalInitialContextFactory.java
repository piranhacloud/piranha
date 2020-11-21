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
package cloud.piranha.server2;

import static cloud.piranha.server2.GlobalPolicy.getContextId;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import cloud.piranha.naming.impl.DefaultInitialContext;

/**
 * The default InitialContextFactory.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @author Arjan Tijms
 */
public class GlobalInitialContextFactory implements InitialContextFactory {

    /**
     * Stores the initial contexts
     */
    private static final Map<String, DefaultInitialContext> INITIAL_CONTEXTS = new ConcurrentHashMap<>();

    /**
     * A dynamic initial context.
     * 
     * @author Manfred Riem (mriem@manorrock.com)
     */
    public static class DynamicInitialContext extends DefaultInitialContext {

        /**
         * Stores the context id.
         */
        private final String contextId;

        /**
         * Constructor.
         * 
         * @param contextId the context id.
         */
        public DynamicInitialContext(String contextId) {
            this.contextId = contextId;
        }

        @Override
        public void close() throws NamingException {};

        @Override
        public String toString() {
            return contextId + " " + super.toString();
        }
    }

    /**
     * Get the initial context.
     *
     * @param environment the environment.
     * @return the initial context.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        return INITIAL_CONTEXTS.computeIfAbsent(getContextId(), DynamicInitialContext::new);
    }

}
