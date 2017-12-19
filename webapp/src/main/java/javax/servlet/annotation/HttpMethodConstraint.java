/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.servlet.annotation.ServletSecurity.EmptyRoleSemantic;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;

/**
 * The HttpMethodConstraint API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpMethodConstraint {

    /**
     * Get the EmptyRoleSemantic.
     *
     * @return the EmptyRoleSemantic.
     */
    EmptyRoleSemantic emptyRoleSemantic() default EmptyRoleSemantic.PERMIT;

    /**
     * Get the roles allowed.
     *
     * @return the roles allowed.
     */
    String[] rolesAllowed() default {};

    /**
     * Get the TransportGuarantee.
     *
     * @return the TransportGuarantee.
     */
    TransportGuarantee transportGuarantee() default TransportGuarantee.NONE;

    /**
     * Get the method name.
     *
     * @return the method name.
     */
    String value();
}
