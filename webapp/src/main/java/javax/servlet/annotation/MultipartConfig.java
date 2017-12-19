/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The MultipartConfig API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultipartConfig {

    /**
     * Get the file size threshold.
     *
     * @return the file size threshold.
     */
    int fileSizeThreshold() default 0;

    /**
     * Get the storage location.
     *
     * @return the storage location.
     */
    String location() default "";

    /**
     * Get the maximum file size.
     *
     * @return the maximum file size, or -1 for unlimited.
     */
    long maxFileSize() default -1L;

    /**
     * Get the maximum POST body request size.
     *
     * @return the maximum POST body request size.
     */
    long maxRequestSize() default -1L;
}
