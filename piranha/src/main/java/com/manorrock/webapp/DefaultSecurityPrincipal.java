/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.security.Principal;

/**
 * The default SecurityPrincipal.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultSecurityPrincipal implements Principal {

    /**
     * Stores the name.
     */
    private final String name;

    /**
     * Constructor.
     *
     * @param name the name.
     */
    public DefaultSecurityPrincipal(String name) {
        this.name = name;
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    @Override
    public String getName() {
        return name;
    }
}
