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
 * The HttpConstraint API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpConstraint {

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
     * Get the EmptyRoleSemantic.
     *
     * @return the EmptyRoleSemantic.
     */
    EmptyRoleSemantic value() default EmptyRoleSemantic.PERMIT;
}
