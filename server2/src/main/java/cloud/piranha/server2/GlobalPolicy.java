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

import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class GlobalPolicy extends Policy implements Consumer<Policy> {

    /**
     * Stores the thread local context id.
     */
    private static ThreadLocal<String> threadLocalContextId = new ThreadLocal<String>();

    /**
     * Stores the application policies.
     */
    private static final Map<String, Policy> APPLICATION_POLICIES = new ConcurrentHashMap<>();

    /**
     * Get the context id.
     * 
     * @return the context id.
     */
    public static String getContextId() {
        return threadLocalContextId.get();
    }

    /**
     * Set the context id.
     * 
     * @param contextId the context id.
     */
    public static void setContextId(String contextId) {
        threadLocalContextId.set(contextId);
    }

    /**
     * Accept the application policy.
     * 
     * @param applicationPolicy the application policy.
     */
    @Override
    public void accept(Policy applicationPolicy) {
        APPLICATION_POLICIES.put(getContextId(), applicationPolicy);
    }

    /**
     * Does the permission imply the given protection domain.
     * 
     * @param domain the protection domain.
     * @param permission the permission.
     * @return true if it does, false otherwise.
     */
    @Override
    public boolean implies(ProtectionDomain domain, Permission permission) {
        return APPLICATION_POLICIES.get(getContextId()).implies(domain, permission);
    }

    /**
     * Get the permissions for the give code source.
     * 
     * @param codesource the code source.
     * @return the permissions.
     */
    @Override
    public PermissionCollection getPermissions(CodeSource codesource) {
        return APPLICATION_POLICIES.get(getContextId()).getPermissions(codesource);
    }

    /**
     * Get the permissions for the given protection domain.
     * 
     * @param domain the protection domain.
     * @return the permissions.
     */
    @Override
    public PermissionCollection getPermissions(ProtectionDomain domain) {
        return APPLICATION_POLICIES.get(getContextId()).getPermissions(domain);
    }

}
