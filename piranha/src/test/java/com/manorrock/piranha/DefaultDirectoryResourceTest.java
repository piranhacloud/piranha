/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.io.File;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * The JUnit tests for DirectoryResource class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultDirectoryResourceTest {

    /**
     * Test getResource method.
     */
    @Test
    public void testGetResource() {
        DefaultDirectoryResource resource = new DefaultDirectoryResource();
        assertNull(resource.getResource("/resource"));
    }

    /**
     * Test getResourceAsStream method.
     */
    @Test
    public void testGetResourceAsStream() {
        DefaultDirectoryResource resource = new DefaultDirectoryResource();
        assertNull(resource.getResourceAsStream("/resource"));
    }

    /**
     * Test getResourceAsStream method.
     */
    @Test
    public void testGetResourceAsStream2() {
        DefaultDirectoryResource resource = new DefaultDirectoryResource(new File("."));
        assertNotNull(resource.getResourceAsStream("pom.xml"));
    }

    /**
     * Test getRootDirectory method, of class DirectoryResource.
     */
    @Test
    public void testGetRootDirectory() {
        DefaultDirectoryResource resource = new DefaultDirectoryResource();
        resource.setRootDirectory(new File("src/main/java"));
        assertNotNull(resource.getRootDirectory());
    }
}
