/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.http.webapp;

import cloud.piranha.naming.api.NamingManager;
import cloud.piranha.policy.api.PolicyManager;

/**
 * The context for a HTTP WebApplication.
 *
 * <p>
 * This class is used as a ThreadLocal so we can associate information to a web
 * application service method invocation that is needed on a thread local basis.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpWebApplicationContext {

    /**
     * Stores the classloader.
     */
    private ClassLoader classLoader;

    /**
     * Stores the context.
     */
    private NamingManager namingManager;

    /**
     * Stores the policy manager.
     */
    private PolicyManager policyManager;

    /**
     * Get the classloader.
     *
     * @return the classloader.
     */
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    /**
     * Get the naming manager.
     *
     * @return the naming manager.
     */
    public NamingManager getNamingManager() {
        return namingManager;
    }

    /**
     * Get the policy manager.
     *
     * @return the policy manager.
     */
    public PolicyManager getPolicyManager() {
        return policyManager;
    }

    /**
     * Set the classloader.
     *
     * @param classLoader the classloader.
     */
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Set the naming manager.
     *
     * @param namingManager the naming manager.
     */
    public void setNamingManager(NamingManager namingManager) {
        this.namingManager = namingManager;
    }

    /**
     * Set the policy manager.
     *
     * @param policyManager the policy manager.
     */
    public void setPolicyManager(PolicyManager policyManager) {
        this.policyManager = policyManager;
    }
}
