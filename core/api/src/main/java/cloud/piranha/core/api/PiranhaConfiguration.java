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
package cloud.piranha.core.api;

import java.io.File;

/**
 * Piranha configuration.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface PiranhaConfiguration {

    /**
     * Get the value as a boolean.
     * 
     * @param key the key.
     * @param defaultValue the default value if the key is not found.
     * @return the value.
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * Get the value as a class.
     * 
     * @param key the key.
     * @return the value.
     */
    Class<?> getClass(String key);
    
    /**
     * Get the value as a File.
     * 
     * @param key the key.
     * @return the value.
     */
    File getFile(String key);
    
    /**
     * Get the value as an int.
     * 
     * @param key the key.
     * @return the value.
     */
    Integer getInteger(String key);
    
    /**
     * Get the value as a long.
     * 
     * @param key the key.
     * @return the value.
     */
    Long getLong(String key);
    
    /**
     * Get the value as a string.
     * 
     * @param key the key.
     * @return the value (or null if not set).
     */
    String getString(String key);
    
    /**
     * Set the boolean value.
     * 
     * @param key the key.
     * @param value the boolean value.
     */
    void setBoolean(String key, Boolean value);

    /**
     * Set the class value.
     * 
     * @param key the key.
     * @param value the class value. 
     */
    void setClass(String key, Class<?> value);
    
    /**
     * Set the File value.
     * 
     * @param key the key.
     * @param value the value.
     */
    void setFile(String key, File value);
    
    /**
     * Set the integer value.
     * 
     * @param key the key.
     * @param value the value. 
     */
    void setInteger(String key, Integer value);
    
    /**
     * Set the long value.
     * 
     * @param key the key.
     * @param value the value. 
     */
    void setLong(String key, Long value);
    
    /**
     * Set the string value.
     * 
     * @param key the key.
     * @param value the value.
     */
    void setString(String key, String value);
}
