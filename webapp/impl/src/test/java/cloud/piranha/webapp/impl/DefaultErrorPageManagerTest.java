package cloud.piranha.webapp.impl;

import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DefaultErrorPageManagerTest {
    @Test
    void testPagesByCode() {
        DefaultErrorPageManager errorPageManager = new DefaultErrorPageManager();
        errorPageManager.getErrorPagesByCode().put(501, "/501");
        errorPageManager.getErrorPagesByCode().put(404, "/404");
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setStatus(501);
        assertEquals("/501", errorPageManager.getErrorPage(null, response));
        response.setStatus(404);
        assertEquals("/404", errorPageManager.getErrorPage(null, response));
    }

    @Test
    void testPagesByCode2() {
        DefaultErrorPageManager errorPageManager = new DefaultErrorPageManager();
        errorPageManager.getErrorPagesByCode().put(404, "/404");
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setStatus(501);
        assertNull(errorPageManager.getErrorPage(null, response));
    }

    @Test
    void testPagesByCode3() {
        DefaultErrorPageManager errorPageManager = new DefaultErrorPageManager();
        errorPageManager.getErrorPagesByCode().put(404, "/404");
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setStatus(500);
        assertNull(errorPageManager.getErrorPage(new NullPointerException(), response));
    }

    @Test
    void testPagesByException() {
        DefaultErrorPageManager errorPageManager = new DefaultErrorPageManager();
        errorPageManager.getErrorPagesByException().put(IllegalArgumentException.class.getName(), "/IAE");
        assertEquals("/IAE", errorPageManager.getErrorPage(new IllegalArgumentException(), null));
    }

    @Test
    void testPagesByException2() {
        DefaultErrorPageManager errorPageManager = new DefaultErrorPageManager();
        errorPageManager.getErrorPagesByException().put(IndexOutOfBoundsException.class.getName(), "/IOUB");
        assertEquals("/IOUB", errorPageManager.getErrorPage(new ArrayIndexOutOfBoundsException(), null));
    }

    @Test
    void testPagesByException3() {
        DefaultErrorPageManager errorPageManager = new DefaultErrorPageManager();
        errorPageManager.getErrorPagesByException().put(IllegalArgumentException.class.getName(), "/IAE");
        assertEquals("/IAE", errorPageManager.getErrorPage(new ServletException(new IllegalArgumentException()), null));
    }

    @Test
    void testPagesByException4() {
        DefaultErrorPageManager errorPageManager = new DefaultErrorPageManager();
        errorPageManager.getErrorPagesByException().put(IllegalArgumentException.class.getName(), "/IAE");
        assertNull(errorPageManager.getErrorPage(new IndexOutOfBoundsException(), null));
    }
}
