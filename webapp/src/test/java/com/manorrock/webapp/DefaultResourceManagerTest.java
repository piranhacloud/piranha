/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.io.File;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * The JUnit tests for the DefaultResourceManager class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultResourceManagerTest {

    /**
     * Test getResource method to not find a missing resource.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetResource() throws Exception {
        DefaultResourceManager manager = new DefaultResourceManager();
        manager.addResource(new DefaultDirectoryResource(new File("")));
        assertNull(manager.getResource("/doesnotexist"));
    }

    /**
     * Test getResource method to find the /src directory resource.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetResource2() throws Exception {
        DefaultResourceManager manager = new DefaultResourceManager();
        manager.addResource(new DefaultDirectoryResource(new File(".")));
        assertNotNull(manager.getResource("src"));
    }

    /**
     * Test getResourceAsStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetResource3() throws Exception {
        DefaultResourceManager manager = new DefaultResourceManager();
        manager.addResource(new DefaultDirectoryResource(new File(".")));
        manager.addResource(new DefaultDirectoryResource(new File("")));
        assertNotNull(manager.getResource("/src/main/java/com/manorrock/webapp/DefaultResourceManager.java"));
    }

    /**
     * Test getResourceAsStream method to not find a missing resource.
     */
    @Test
    public void testGetResourceAsStream() {
        DefaultResourceManager manager = new DefaultResourceManager();
        manager.addResource(new DefaultDirectoryResource(new File("")));
        assertNull(manager.getResourceAsStream("/doesnotexist"));
    }

    /**
     * Test getResourceAsStream method to not find a missing resource.
     */
    @Test
    public void testGetResourceAsStream2() {
        DefaultResourceManager manager = new DefaultResourceManager();
        manager.addResource(new DefaultDirectoryResource(new File(".")));
        manager.addResource(new DefaultDirectoryResource(new File("")));
        assertNotNull(manager.getResourceAsStream("/src/main/java/com/manorrock/webapp/DefaultResourceManager.java"));
    }
}
