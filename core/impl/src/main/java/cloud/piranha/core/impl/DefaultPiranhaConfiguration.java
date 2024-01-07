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
package cloud.piranha.core.impl;

import cloud.piranha.core.api.PiranhaConfiguration;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * The default Piranha configuration implementation.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultPiranhaConfiguration implements PiranhaConfiguration {

    /**
     * Stores the configuration.
     */
    private final Map<String, Object> configuration = new HashMap<>();

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        if (configuration.containsKey(key)) {
            return (boolean) configuration.get(key);
        }
        return defaultValue;
    }

    @Override
    public Class<?> getClass(String key) {
        return (Class<?>) configuration.get(key);
    }

    @Override
    public File getFile(String key) {
        return (File) configuration.get(key);
    }

    @Override
    public Integer getInteger(String key) {
        return (Integer) configuration.get(key);
    }

    @Override
    public Long getLong(String key) {
        return (Long) configuration.get(key);
    }

    @Override
    public String getString(String key) {
        return (String) configuration.get(key);
    }

    @Override
    public void setBoolean(String key, Boolean value) {
        configuration.put(key, value);
    }

    @Override
    public void setClass(String key, Class<?> value) {
        configuration.put(key, value);
    }

    @Override
    public void setFile(String key, File value) {
        configuration.put(key, value);
    }

    @Override
    public void setInteger(String key, Integer value) {
        configuration.put(key, value);
    }

    @Override
    public void setLong(String key, Long value) {
        configuration.put(key, value);
    }

    @Override
    public void setString(String key, String value) {
        configuration.put(key, value);
    }
}
