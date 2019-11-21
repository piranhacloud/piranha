/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manorrock.piranha.nano;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * The JUnit tests for the NanoPiranha class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NanoPiranhaTest {
    
    /**
     * Test service method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testService() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        NanoPiranha piranha = new NanoPiranha();
        piranha.setServlet(new HelloWorldServlet());
        piranha.service(inputStream, outputStream);
        assertEquals("Hello World", outputStream.toString());
    }
}
