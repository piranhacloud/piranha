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

import cloud.piranha.resource.api.Resource;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * A resource backed by a class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ClassResource implements Resource {

    /**
     * Stores the class name.
     */
    private final String className;

    /**
     * Stores the location.
     */
    private String location;

    /**
     * Constructor.
     *
     * @param className the class name.
     */
    public ClassResource(String className) {
        this.className = className;
        try {
            this.location = "/" + Class.forName(className).getName().replace(".", "/") + ".class";
        } catch (ClassNotFoundException ex) {
            this.location = null;
        }
    }

    @Override
    public URL getResource(String location) {
        URL result = null;
        if (this.location.equals(location)) {
            return getClass().getResource(location);
        }
        return result;
    }

    @Override
    public InputStream getResourceAsStream(String location) {
        InputStream result = null;
        if (this.location.equals(location)) {
            result = getClass().getResourceAsStream(location);
        }
        return result;
    }

    @Override
    public Stream<String> getAllLocations() {
        ArrayList<String> result = new ArrayList<>();
        if (location != null) {
            result.add(location);
        }
        return result.stream();
    }
}
