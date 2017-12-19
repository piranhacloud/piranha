/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import javax.servlet.annotation.MultipartConfig;

/**
 * The MultipartConfigElement API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class MultipartConfigElement {

    /**
     * Stores the file size threshold.
     */
    private final int fileSizeThreshold;

    /**
     * Stores the location.
     */
    private final String location;

    /**
     * Stores the max file size.
     */
    private final long maxFileSize;

    /**
     * Stores the max request size.
     */
    private final long maxRequestSize;

    /**
     * Constructor.
     *
     * @param location the location.
     */
    public MultipartConfigElement(String location) {
        this.fileSizeThreshold = 0;
        this.location = location;
        this.maxFileSize = -1L;
        this.maxRequestSize = -1L;
    }

    /**
     * Constructor.
     *
     * @param location the location.
     * @param maxFileSize the maximum file size.
     * @param maxRequestSize the maximum request size.
     * @param fileSizeThreshold the file size threshold.
     */
    public MultipartConfigElement(String location, long maxFileSize, long maxRequestSize, int fileSizeThreshold) {
        this.fileSizeThreshold = fileSizeThreshold;
        this.location = location;
        this.maxFileSize = maxFileSize;
        this.maxRequestSize = maxRequestSize;
    }

    /**
     * Constructor.
     *
     * @param annotation the annotation value
     */
    public MultipartConfigElement(MultipartConfig annotation) {
        this.fileSizeThreshold = annotation.fileSizeThreshold();
        this.location = annotation.location();
        this.maxFileSize = annotation.maxFileSize();
        this.maxRequestSize = annotation.maxRequestSize();
    }

    /**
     * Get the file size threshold.
     *
     * @return the file size threshold.
     */
    public int getFileSizeThreshold() {
        return this.fileSizeThreshold;
    }

    /**
     * Get the storage location.
     *
     * @return the storage location.
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Get the max file size.
     *
     * @return the max file size.
     */
    public long getMaxFileSize() {
        return this.maxFileSize;
    }

    /**
     * Get the max request size.
     *
     * @return the max request size.
     */
    public long getMaxRequestSize() {
        return this.maxRequestSize;
    }
}
