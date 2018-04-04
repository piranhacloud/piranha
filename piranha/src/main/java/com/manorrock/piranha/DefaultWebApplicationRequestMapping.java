/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

/**
 * The default WebApplicationRequestMapping.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationRequestMapping implements WebApplicationRequestMapping {

    /**
     * Stores the exact flag.
     */
    private boolean exact;
    
    /**
     * Stores the extension flag.
     */
    private boolean extension;
    
    /**
     * Stores the mapping.
     */
    private String mapping;
    
    /**
     * Constructor.
     * 
     * @param mapping the mapping. 
     */
    public DefaultWebApplicationRequestMapping(String mapping) {
        this.mapping = mapping;
    }

    /**
     * Get the mapping.
     * 
     * @return the mapping.
     */
    @Override
    public String getPath() {
        return mapping;
    }

    /**
     * Is this an exact match.
     * 
     * @return true it it is, false otherwise.
     */
    @Override
    public boolean isExact() {
        return exact;
    }

    /**
     * Is this an extension match.
     * 
     * @return true if it is, false otherwise.
     */
    @Override
    public boolean isExtension() {
        return extension;
    }

    /**
     * Set the exact flag.
     * 
     * @param exact the exact flag. 
     */
    public void setExact(boolean exact) {
        this.exact = exact;
    }
    
    /**
     * Set the extension flag.
     * 
     * @param extension the extension flag.
     */
    public void setExtension(boolean extension) {
        this.extension = extension;
    }

    /**
     * Set the mapping.
     * 
     * @param mapping the mapping. 
     */
    public void setMapping(String mapping) {
        this.mapping = mapping;
    }
}
