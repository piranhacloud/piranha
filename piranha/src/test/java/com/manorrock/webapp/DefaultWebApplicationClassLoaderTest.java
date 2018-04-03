/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.io.File;
import static org.junit.Assert.assertNotNull;
import org.junit.Ignore;
import org.junit.Test;

/**
 * The JUnit tests for the DefaultWebApplicationClassLoader.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationClassLoaderTest {

    /**
     * Test loadClass method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testLoadClass() throws Exception {
        DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader();
        assertNotNull(classLoader.loadClass("java.lang.String", true));
    }

    /**
     * Test loadClass method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = ClassNotFoundException.class)
    public void testLoadClass2() throws Exception {
        DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader();
        classLoader.loadClass("this.is.a.bogus.className", true);
    }

    /**
     * Test loadClass method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    @Ignore
    public void testLoadClass3() throws Exception {
        DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader();
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        resourceManager.addResource(new DefaultDirectoryResource(new File("target/classloader/WEB-INF/classes")));
        classLoader.setResourceManager(resourceManager);
        assertNotNull(classLoader.loadClass("com.manorrock.webapp.test.classloader.Test1Servlet", true));
    }

    /**
     * Test loadClass method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    @Ignore
    public void testLoadClass4() throws Exception {
        DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader();
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        resourceManager.addResource(new DefaultDirectoryResource(new File("target/classloader/WEB-INF/classes")));
        classLoader.setResourceManager(resourceManager);
        assertNotNull(classLoader.loadClass("com.manorrock.webapp.test.classloader.Test1Servlet", true));
        assertNotNull(classLoader.loadClass("com.manorrock.webapp.test.classloader.Test1Servlet", true));
    }
}
