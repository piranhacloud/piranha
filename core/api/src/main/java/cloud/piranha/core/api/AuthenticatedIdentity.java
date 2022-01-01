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
package cloud.piranha.core.api;

import java.security.Principal;
import java.util.Set;

/**
 * This interface is implemented by classes that represent the current authenticated identity.
 *
 * <p>
 * What current means here is context dependent. In a Jakarta Servlet application this refers
 * to the caller (user) details during a single HTTP request.
 *
 * @author Arjan Tijms
 *
 */
public interface AuthenticatedIdentity extends Principal {

    @Override
    default String getName() {
        if (getCallerPrincipal() == null) {
            return null;
        }

        return getCallerPrincipal().getName();
    }

    /**
     * Returns the caller principal, which represents the primary name of the calling entity (aka the "caller")
     * to a server.
     *
     * @return the caller principal, or null if authentication has not (yet) completed successfully.
     */
    Principal getCallerPrincipal();

    /**
     * The groups the caller is in.
     *
     * <p>
     * If group to role mapping is not active (the default) groups are equal to roles.
     *
     * @return the set of groups the caller is in, never null.
     */
    Set<String> getGroups();

}
