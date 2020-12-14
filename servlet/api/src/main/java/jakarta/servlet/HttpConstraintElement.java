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
package jakarta.servlet;

import jakarta.servlet.annotation.ServletSecurity.EmptyRoleSemantic;
import jakarta.servlet.annotation.ServletSecurity.TransportGuarantee;

/**
 * The HttpConstraintElement API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpConstraintElement {

    /**
     * Stores the EmptyRoleSemantic.
     */
    private EmptyRoleSemantic emptyRoleSemantic;

    /**
     * Stores the roles allowed.
     */
    private String[] rolesAllowed;

    /**
     * Stores the TransportGuarantee.
     */
    private TransportGuarantee transportGuarantee;

    /**
     * Constructor.
     */
    public HttpConstraintElement() {
        this(EmptyRoleSemantic.PERMIT);
    }

    /**
     * Constructor.
     *
     * @param emptyRoleSemantic the EmptyRoleSemantic.
     */
    public HttpConstraintElement(EmptyRoleSemantic emptyRoleSemantic) {
        this(emptyRoleSemantic, TransportGuarantee.NONE, new String[0]);
    }

    /**
     * Constructor.
     *
     * @param transportGuarantee the TransportGuarantee.
     * @param rolesAllowed the roles allowed.
     */
    public HttpConstraintElement(TransportGuarantee transportGuarantee, String... rolesAllowed) {
        this(EmptyRoleSemantic.PERMIT, transportGuarantee, rolesAllowed);
    }

    /**
     * Constructor.
     *
     * @param emptyRoleSemantic the EmptyRoleSemantic.
     * @param transportGuarantee the TransportGuarantee.
     * @param rolesAllowed the roles allowed.
     */
    public HttpConstraintElement(EmptyRoleSemantic emptyRoleSemantic, TransportGuarantee transportGuarantee, String... rolesAllowed) {
        this.emptyRoleSemantic = emptyRoleSemantic;
        this.transportGuarantee = transportGuarantee;
        this.rolesAllowed = rolesAllowed;
    }

    /**
     * Get the EmptyRoleSemantic.
     *
     * @return the EmptyRoleSemantic.
     */
    public EmptyRoleSemantic getEmptyRoleSemantic() {
        return emptyRoleSemantic;
    }

    /**
     * Get the transport guarantee.
     *
     * @return the transport guarantee.
     */
    public TransportGuarantee getTransportGuarantee() {
        return transportGuarantee;
    }

    /**
     * Get the roles allowed.
     *
     * @return the roles allowed.
     */
    public String[] getRolesAllowed() {
        String[] result = new String[rolesAllowed.length];
        System.arraycopy(rolesAllowed, 0, result, 0, result.length);
        return result;
    }
}
