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
package com.manorrock.piranha.test.jersey;

import com.manorrock.piranha.DefaultWebApplicationResponse;
import com.manorrock.piranha.test.utils.TestServletOutputStream;
import java.util.List;
import javax.servlet.http.Cookie;

/**
 * A test HTTP servlet response.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestHttpServletResponse extends DefaultWebApplicationResponse {

    /**
     * Constructor.
     */
    public TestHttpServletResponse() {
        super();
        this.outputStream = new TestServletOutputStream();
    }

    /**
     * Get the cookies.
     *
     * @return the cookies.
     */
    public List<Cookie> getCookies() {
        return cookies;
    }

    /**
     * Get the buffer size.
     *
     * @return the buffer size.
     */
    @Override
    public int getBufferSize() {
        return 0;
    }

    /**
     * Get the bytes in the buffer.
     *
     * @return the bytes in the buffer.
     */
    public byte[] getResponseBody() {
        if (this.gotWriter) {
            this.writer.flush();
        }
        TestServletOutputStream output = (TestServletOutputStream) this.outputStream;
        return output.getBytes();
    }

    /**
     * Reset the buffer.
     */
    @Override
    public void resetBuffer() {
        verifyNotCommitted("resetBuffer");
        TestServletOutputStream output = (TestServletOutputStream) this.outputStream;
        output.reset();
    }

    /**
     * Set the buffer size.
     *
     * @param size the buffer size.
     */
    @Override
    public void setBufferSize(int size) {
    }
}
