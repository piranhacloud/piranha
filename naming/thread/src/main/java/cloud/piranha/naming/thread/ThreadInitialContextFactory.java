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
package cloud.piranha.naming.thread;

import java.util.HashMap;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 * The Thread InitialContextFactory.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ThreadInitialContextFactory implements InitialContextFactory {

    /**
     * Stores the initial contexts by thread id.
     */
    private static final HashMap<Long, Context> INITIAL_CONTEXTS = new HashMap<>(1);

    /**
     * Get the initial context.
     * 
     * @return the initial context.
     * @param environment the environment.
     * @throws NamingException when a naming error occurs.
     */
    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        if (INITIAL_CONTEXTS.containsKey(Thread.currentThread().getId())) {
            return INITIAL_CONTEXTS.get(Thread.currentThread().getId());
        }
        throw new NamingException("Initial context not available for thread: " + Thread.currentThread());
    }

    /**
     * Remove the initial context.
     */
    public static void removeInitialContext() {
        INITIAL_CONTEXTS.remove(Thread.currentThread().getId());
    }

    /**
     * Set the initial context.
     *
     * @param initialContext the initial context.
     */
    public static void setInitialContext(Context initialContext) {
        INITIAL_CONTEXTS.put(Thread.currentThread().getId(), initialContext);
    }
}
