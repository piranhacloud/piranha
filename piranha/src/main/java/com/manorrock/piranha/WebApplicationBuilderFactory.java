/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

/**
 * The WebApplicationBuilder factory.
 *
 * @author Manfred
 */
public class WebApplicationBuilderFactory {

    /**
     * Produce the default web application builder.
     *
     * @return the default web application builder.
     */
    public static WebApplicationBuilder produce() {
        return new DefaultWebApplicationBuilder();
    }
}
