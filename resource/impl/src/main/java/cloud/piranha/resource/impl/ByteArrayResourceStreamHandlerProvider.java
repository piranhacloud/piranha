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
package cloud.piranha.resource.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.spi.URLStreamHandlerProvider;
import java.util.function.Function;

/**
 * Handler for the <code>bytes://</code> protocol.
 *
 * @author Arjan Tijms
 */
public class ByteArrayResourceStreamHandlerProvider extends URLStreamHandlerProvider {

    /**
     * A Function that provides the input stream based on the string form of a
     * <code>bytes://</code> URL.
     */
    private static InheritableThreadLocal<Function<String, InputStream>> localGetResourceAsStreamFunction = new InheritableThreadLocal<>();

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if (!"bytes".equals(protocol)) {
            return null;
        }

        return new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u) throws IOException {
                return new URLConnection(u) {
                    @Override
                    public InputStream getInputStream() throws IOException {
                        return localGetResourceAsStreamFunction.get().apply(u.toString());
                    }

                    @Override
                    public void connect() throws IOException {
                        // Do nothing
                    }
                };
            }
        };
    }
    
    /**
     * Get the Function set to handle <code>bytes://</code> protocol handling.
     * 
     * @return the Function, or null.
     */
    public static Function<String, InputStream> getGetResourceAsStreamFunction() {
        return localGetResourceAsStreamFunction.get();
    }

    /**
     * Sets a Function that provides the input stream based on the string form
     * of a <code>bytes://</code> URL.
     *
     * @param getResourceAsStreamFunction the function to transform a string URL
     * to an input stream
     */
    public static void setGetResourceAsStreamFunction(
            Function<String, InputStream> getResourceAsStreamFunction) {
        localGetResourceAsStreamFunction.set(getResourceAsStreamFunction);
    }
}
