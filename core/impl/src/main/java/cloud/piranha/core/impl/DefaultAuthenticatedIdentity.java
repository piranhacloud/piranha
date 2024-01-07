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

import static java.util.Collections.unmodifiableSet;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;

import cloud.piranha.core.api.AuthenticatedIdentity;

/**
 * Default implementation of AuthenticatedIdentity.
 *
 * <p>
 * This implementation is an immutable structure, with a facility to store it in
 * TLS. It's the responsibility of the context, e.g. the HTTP request handler,
 * to remove the identity from TLS at the end of the context (e.g. end of the
 * HTTP request), or at any other appropriate time (e.g. when logging out
 * mid-request).
 *
 *
 * @author Arjan Tijms
 *
 */
public class DefaultAuthenticatedIdentity implements AuthenticatedIdentity {

    /**
     * Stores the current identity.
     */
    private static InheritableThreadLocal<AuthenticatedIdentity> currentIdentity = new InheritableThreadLocal<>();

    /**
     * Stores the current subject.
     */
    private static InheritableThreadLocal<Subject> currentSubject = new InheritableThreadLocal<>();

    /**
     * Stores the caller principal.
     */
    private Principal callerPrincipal;

    /**
     * Stores the groups.
     */
    private Set<String> groups = new HashSet<>();

    /**
     * Constructor.
     *
     * @param callerPrincipal the caller principal.
     * @param groups the groups.
     */
    public DefaultAuthenticatedIdentity(Principal callerPrincipal, Set<String> groups) {
        this.callerPrincipal = callerPrincipal;
        this.groups = unmodifiableSet(groups);
    }

    /**
     * Set the current identity.
     *
     * @param callerPrincipal the caller principal.
     * @param groups the groups.
     */
    public static void setCurrentIdentity(Principal callerPrincipal, Set<String> groups) {
        setCurrentIdentity(new DefaultAuthenticatedIdentity(callerPrincipal, groups));
    }

    /**
     * Set the current identity.
     *
     * @param identity the identity.
     */
    public static void setCurrentIdentity(AuthenticatedIdentity identity) {
        Subject subject = new Subject();
        subject.getPrincipals().add(identity);
        if (identity.getCallerPrincipal() != null) {
            subject.getPrincipals().add(identity.getCallerPrincipal());
        }

        currentIdentity.set(identity);
        currentSubject.set(subject);
    }

    /**
     * {@return the current subject}
     */
    public static Subject getCurrentSubject() {
        return currentSubject.get();
    }

    /**
     * {@return the current identity}
     */
    public static AuthenticatedIdentity getCurrentIdentity() {
        return currentIdentity.get();
    }

    /**
     * Clear identity and subject.
     */
    public static void clear() {
        currentIdentity.remove();
        currentSubject.remove();
    }

    @Override
    public Principal getCallerPrincipal() {
        return callerPrincipal;
    }

    @Override
    public Set<String> getGroups() {
        return groups;
    }

}
