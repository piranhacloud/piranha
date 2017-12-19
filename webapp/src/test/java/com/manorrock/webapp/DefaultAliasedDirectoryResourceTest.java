/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.io.File;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * The JUnit test for AliasedDirectoryResource.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultAliasedDirectoryResourceTest {

    /**
     * Test getResource.
     */
    @Test
    public void testGetResource() {
        DefaultAliasedDirectoryResource resource = new DefaultAliasedDirectoryResource(null, "/alias");
        assertNull(resource.getResource("resource"));
    }

    /**
     * Test getResource.
     */
    @Test
    public void testGetResource2() {
        DefaultAliasedDirectoryResource resource = new DefaultAliasedDirectoryResource();
        resource.setRootDirectory(new File("src/main/java"));
        resource.setAlias("/alias");
        assertNotNull(resource.getResource("/alias/com/manorrock/webapp/DefaultDirectoryResource.java"));
    }

    /**
     * Test getResource method.
     */
    @Test
    public void testGetResource3() {
        DefaultAliasedDirectoryResource resource = new DefaultAliasedDirectoryResource(new File("src/test/java/org"), "/org");
        assertNull(resource.getResource("/com/manorrock/webapp/AliasedDirectoryResourceTest2.java"));
    }

    /**
     * Test getResourceAsStream method.
     */
    @Test
    public void testGetResourceAsStream() {
        DefaultAliasedDirectoryResource resource = new DefaultAliasedDirectoryResource(null, "/alias");
        assertNull(resource.getResourceAsStream("/resource"));
    }

    /**
     * Test getResourceAsStream method.
     */
    @Test
    public void testGetResourceAsStream2() {
        DefaultAliasedDirectoryResource resource = new DefaultAliasedDirectoryResource(new File("src/main/java"), "/alias");
        assertNotNull(resource.getResourceAsStream("/alias/com/manorrock/webapp/DefaultDirectoryResource.java"));
    }

    /**
     * Test getResourceAsStream method.
     */
    @Test
    public void testGetResourceAsStream3() {
        DefaultAliasedDirectoryResource resource = new DefaultAliasedDirectoryResource(new File("src/main/java"), "/alias");
        assertNull(resource.getResourceAsStream("/alias/com/manorrock/webapp/DirectoryResource.class"));
    }

    /**
     * Test getAlias method.
     */
    @Test
    public void testGetAlias() {
        DefaultAliasedDirectoryResource resource = new DefaultAliasedDirectoryResource(null, "/alias");
        assertEquals("/alias", resource.getAlias());
    }

    /**
     * Test getRootDirectory method.
     */
    @Test
    public void testGetRootDirectory() {
        DefaultAliasedDirectoryResource resource = new DefaultAliasedDirectoryResource();
        resource.setRootDirectory(new File("src/main/java"));
        resource.setAlias("/alias");
        assertNotNull(resource.getRootDirectory());
    }
}
