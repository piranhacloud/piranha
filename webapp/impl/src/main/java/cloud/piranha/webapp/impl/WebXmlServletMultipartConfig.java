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
package cloud.piranha.webapp.impl;

public class WebXmlServletMultipartConfig {
    
    /**
     * Location to store files
     */
    private String location;

    /**
     * Maximum size for files. -1 means unlimited.
     * 
     */
    private long maxFileSize = -1;

    /**
     * Maximum size for requests -1 means unlimited.
     * 
     */
    private long maxRequestSize = -1;

    /**
     * Threshold for bytes kept in memory before written to disk
     *
     */
    private int fileSizeThreshold = 0;
    
    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the maxFileSize
     */
    public long getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * @param maxFileSize the maxFileSize to set
     */
    public void setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    /**
     * @return the maxRequestSize
     */
    public long getMaxRequestSize() {
        return maxRequestSize;
    }

    /**
     * @param maxRequestSize the maxRequestSize to set
     */
    public void setMaxRequestSize(long maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    /**
     * @return the fileSizeThreshold
     */
    public int getFileSizeThreshold() {
        return fileSizeThreshold;
    }

    /**
     * @param fileSizeThreshold the fileSizeThreshold to set
     */
    public void setFileSizeThreshold(int fileSizeThreshold) {
        this.fileSizeThreshold = fileSizeThreshold;
    }

   
}
