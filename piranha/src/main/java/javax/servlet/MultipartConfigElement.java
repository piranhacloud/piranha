/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
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
