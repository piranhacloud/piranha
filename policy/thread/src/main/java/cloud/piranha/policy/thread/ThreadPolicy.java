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
package cloud.piranha.policy.thread;

import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.security.Provider;
import java.util.HashMap;

/**
 * A thread aware Policy.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ThreadPolicy extends Policy {
    
    /**
     * Stores the policy by thread id.
     */
    private static final HashMap<Long, Policy> POLICIES = new HashMap<>(1);

    @Override
    public Parameters getParameters() {
        return POLICIES.get(Thread.currentThread().getId()).getParameters();
    }

    @Override
    public PermissionCollection getPermissions(CodeSource codesource) {
        return POLICIES.get(Thread.currentThread().getId()).getPermissions(codesource);
    }

    @Override
    public PermissionCollection getPermissions(ProtectionDomain domain) {
        return POLICIES.get(Thread.currentThread().getId()).getPermissions(domain);
    }

    @Override
    public Provider getProvider() {
        return POLICIES.get(Thread.currentThread().getId()).getProvider();
    }

    @Override
    public String getType() {
        return POLICIES.get(Thread.currentThread().getId()).getType();
    }

    @Override
    public boolean implies(ProtectionDomain domain, Permission permission) {
        return POLICIES.get(Thread.currentThread().getId()).implies(domain, permission);
    }

    @Override
    public void refresh() {
        POLICIES.get(Thread.currentThread().getId()).refresh();
    }

    /**
     * Remove the policy.
     */
    public static void removePolicy() {
        POLICIES.remove(Thread.currentThread().getId());
    }

    /**
     * Set the policy.
     *
     * @param policy the policy.
     */
    public static void setPolicy(Policy policy) {
        POLICIES.put(Thread.currentThread().getId(), policy);
    }    
}
