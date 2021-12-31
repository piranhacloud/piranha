/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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

/**
 * A servlet multipart-config inside of web.xml/web-fragment.xml.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @author Arjan Tijms
 */
public class WebXmlServletMultipartConfig {

    /**
     * Stores the location to store the files.
     */
    private String location;

    /**
     * Stores the maximum size for files. If value is set to -1 it means
     * unlimited.
     */
    private long maxFileSize = -1;

    /**
     * Stores the maximum size for requests. If value is set to -1 it means
     * unlimited.
     */
    private long maxRequestSize = -1;

    /**
     * Stores the threshold for bytes kept in memory before written to disk.
     */
    private int fileSizeThreshold = 0;

    /**
     * Get the location.
     *
     * @return the location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Get the max file size.
     *
     * @return the max file size.
     */
    public long getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * Get the max request size.
     *
     * @return the max request size.
     */
    public long getMaxRequestSize() {
        return maxRequestSize;
    }

    /**
     * Get the file size threshold.
     *
     * @return the file size threshold.
     */
    public int getFileSizeThreshold() {
        return fileSizeThreshold;
    }

    /**
     * Set the location.
     *
     * @param location the location.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Set the max file size.
     *
     * @param maxFileSize the max file size.
     */
    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    /**
     * Set the max request size.
     *
     * @param maxRequestSize the max request size.
     */
    public void setMaxRequestSize(long maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    /**
     * Set the file size threshold.
     *
     * @param fileSizeThreshold the file size threshold.
     */
    public void setFileSizeThreshold(int fileSizeThreshold) {
        this.fileSizeThreshold = fileSizeThreshold;
    }
}
