/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.servlet.DispatcherType;

/**
 * The WebFilter API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebFilter {

    /**
     * Get the async supported flag.
     *
     * @return the async supported flag.
     */
    boolean asyncSupported() default false;

    /**
     * Get the description.
     *
     * @return the description.
     */
    String description() default "";

    /**
     * Get the dispatcher types.
     *
     * @return the dispatcher type.s
     */
    DispatcherType[] dispatcherTypes() default {DispatcherType.REQUEST};

    /**
     * Get the display name.
     *
     * @return the display name.
     */
    String displayName() default "";

    /**
     * Get the name.
     *
     * @return the name.
     */
    String filterName() default "";

    /**
     * Get the init parameters.
     *
     * @return the init parameters.
     */
    WebInitParam[] initParams() default {};

    /**
     * Get the large icon.
     *
     * @return the large icon.
     */
    String largeIcon() default "";

    /**
     * Get the servlet names.
     *
     * @return the servlet names.
     */
    String[] servletNames() default {};

    /**
     * Get the small icon.
     *
     * @return the small icon.
     */
    String smallIcon() default "";

    /**
     * Get the URL patterns.
     *
     * @return the URL patterns.
     */
    String[] urlPatterns() default {};

    /**
     * Get the URL patterns.
     *
     * @return the URL patterns.
     */
    String[] value() default {};

}
