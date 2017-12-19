/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

/**
 * The HttpMethodConstraintElement API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpMethodConstraintElement extends HttpConstraintElement {

    /**
     * Stores the HTTP method name.
     */
    private final String methodName;

    /**
     * Constructor.
     *
     * @param methodName the HTTP method name.
     */
    public HttpMethodConstraintElement(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Constructor.
     *
     * @param methodName the HTTP method name.
     * @param constraint the constraint.
     */
    public HttpMethodConstraintElement(String methodName, HttpConstraintElement constraint) {
        super(constraint.getEmptyRoleSemantic(), constraint.getTransportGuarantee(), constraint.getRolesAllowed());
        this.methodName = methodName;
    }

    /**
     * Get the HTTP method name.
     *
     * @return the HTTP method name
     */
    public String getMethodName() {
        return methodName;
    }
}
