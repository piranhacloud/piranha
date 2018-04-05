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

import java.lang.annotation.Annotation;
import javax.servlet.annotation.MultipartConfig;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * The JUnit tests for the MultipartConfigElement class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class MultipartConfigElementTest {

    /**
     * Test getFileSizeThreshold method.
     */
    @Test
    public void testGetFileSizeThreshold() {
        MultipartConfigElement element = new MultipartConfigElement("location", 0, 0, 1000);
        assertEquals(1000, element.getFileSizeThreshold());
    }

    /**
     * Test getLocation method.
     */
    @Test
    public void testGetLocation() {
        MultipartConfigElement element = new MultipartConfigElement("location", 0, 0, 1000);
        assertEquals("location", element.getLocation());
    }

    /**
     * Test getMaxFileSize method.
     */
    @Test
    public void testGetMaxFileSize() {
        MultipartConfigElement element = new MultipartConfigElement("location", 1000, 0, 0);
        assertEquals(1000, element.getMaxFileSize());
    }

    /**
     * Test getMaxRequestSize method.
     */
    @Test
    public void testGetMaxRequestSize() {
        MultipartConfigElement element = new MultipartConfigElement("location", 0, 1000, 0);
        assertEquals(1000, element.getMaxRequestSize());
    }

    /**
     * Test getMaxRequestSize method.
     */
    @Test
    public void testGetMaxRequestSize2() {
        MultipartConfigElement element = new MultipartConfigElement(new MultipartConfig() {
            @Override
            public int fileSizeThreshold() {
                return 1000;
            }

            @Override
            public String location() {
                return "";
            }

            @Override
            public long maxFileSize() {
                return 1000;
            }

            @Override
            public long maxRequestSize() {
                return 1000;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        assertEquals(1000, element.getMaxRequestSize());
    }
}
