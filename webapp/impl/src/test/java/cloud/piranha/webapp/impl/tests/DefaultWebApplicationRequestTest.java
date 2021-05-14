package cloud.piranha.webapp.impl.tests;

import cloud.piranha.webapp.impl.DefaultWebApplicationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultWebApplicationRequestTest {

    @Test
    void getFormParameters () {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setInputStream(new ByteArrayInputStream("param1=value1".getBytes(StandardCharsets.UTF_8)));
        request.setContentType("application/x-www-form-urlencoded");
        Map<String, String[]> parameterMap = request.getParameterMap();
        assertEquals(parameterMap.size(), 1);
        assertArrayEquals(new String[] {"value1"}, parameterMap.get("param1"));
    }

    @Test
    void getFormParametersWithCharset () {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setInputStream(new ByteArrayInputStream("param1=value1".getBytes(StandardCharsets.UTF_8)));
        request.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
        Map<String, String[]> parameterMap = request.getParameterMap();
        assertEquals(parameterMap.size(), 1);
        assertArrayEquals(new String[] {"value1"}, parameterMap.get("param1"));
    }

}
