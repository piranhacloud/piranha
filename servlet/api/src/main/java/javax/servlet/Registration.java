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
package javax.servlet;

import java.util.Map;
import java.util.Set;

/**
 * The Registration API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface Registration {

    /**
     * Get the class name.
     *
     * @return the class name.
     */
    public String getClassName();

    /**
     * Get the init parameter.
     *
     * @param name the name.
     * @return the value, or null if not found.
     */
    public String getInitParameter(String name);

    /**
     * Get the init parameters.
     *
     * @return the init parameters.
     */
    public Map<String, String> getInitParameters();

    /**
     * Get the name.
     *
     * @return the name.
     */
    public String getName();

    /**
     * Set the init parameter.
     *
     * @param name the name.
     * @param value the value.
     * @return true if it was set, false otherwise.
     */
    public boolean setInitParameter(String name, String value);

    /**
     * Set the init parameters.
     *
     * @param initParameters the init parameters.
     * @return the init parameters that could NOT be set.
     */
    public Set<String> setInitParameters(Map<String, String> initParameters);

    /**
     * The Registration.Dynamic API.
     *
     * @author Manfred Riem (mriem@manorrock.com)
     */
    interface Dynamic extends Registration {

        /**
         * Set async supported.
         *
         * @param asyncSupported the async supported flag.
         */
        public void setAsyncSupported(boolean asyncSupported);
    }
}
