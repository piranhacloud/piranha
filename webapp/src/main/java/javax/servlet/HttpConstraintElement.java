/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import javax.servlet.annotation.ServletSecurity.EmptyRoleSemantic;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;

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
        return rolesAllowed;
    }
}
