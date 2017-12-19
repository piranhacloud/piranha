/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
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
