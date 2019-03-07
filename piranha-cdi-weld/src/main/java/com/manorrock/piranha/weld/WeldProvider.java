/*
 *  Copyright (c) 2002-2019, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha.weld;

import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.CDIProvider;

/**
 * The Weld CDI provider.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WeldProvider implements CDIProvider {

    /**
     * Stores the instances.
     */
    private static final ConcurrentHashMap<ClassLoader, CDI<Object>> INSTANCES = new ConcurrentHashMap<>();

    /**
     * Get the CDI.
     *
     * @return the CDI.
     */
    @Override
    public CDI<Object> getCDI() {
        return INSTANCES.get(Thread.currentThread().getContextClassLoader());
    }

    /**
     * Set the CDI.
     *
     * @param cdi the CDI.
     */
    public static void setCDI(CDI<Object> cdi) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        INSTANCES.put(classLoader, cdi);
    }
}
