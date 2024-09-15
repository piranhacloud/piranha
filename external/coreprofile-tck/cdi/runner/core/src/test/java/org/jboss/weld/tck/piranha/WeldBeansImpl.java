/*
 * Copyright (c) 2024 Manorrock.com. All Rights Reserved.
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

package org.jboss.weld.tck.piranha;

import java.io.IOException;
import java.util.Arrays;

import org.jboss.cdi.tck.spi.Beans;

/**
 * CDI TCK tests use this class as an adapter between the test application and server container.
 *
 * Then its implementation can simplify the behavior, ie. explicit passivation, while
 * in a real application the decision to passivate/activate some object is on the container
 * and cannot be requested by the application.
 * <p>
 * Until GlassFish provides standalone utility to do that, we have to fake
 * the passivation/activation.
 *
 * @author David Matejcek
 */
public class WeldBeansImpl implements Beans {

    private Object fakeSerialized;

    @Override
    public boolean isProxy(Object instance) {
        return instance.getClass().getName().indexOf("_$$_Weld") > 0;
    }

    @Override
    public byte[] passivate(Object instance) throws IOException {
        fakeSerialized = instance;
        return instance.toString().getBytes();
    }


    @Override
    public Object activate(byte[] bytes) throws IOException, ClassNotFoundException {
        if (Arrays.equals(fakeSerialized.toString().getBytes(), bytes)) {
            Object result = fakeSerialized;
            fakeSerialized = null;
            return result;
        }

        return null;
    }
}
