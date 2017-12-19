/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp.test.jersey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * A Hello resource.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("/hello")
public class HelloResource {
    
    /**
     * GET request
     * 
     * @return "Hello" 
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello";
    }
}
