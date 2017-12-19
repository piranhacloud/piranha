/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.descriptor;

/**
 * The TagLibDescriptor API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface TaglibDescriptor {

    /**
     * Get the taglib location.
     *
     * @return the taglib location.
     */
    public String getTaglibLocation();

    /**
     * Get the taglib URI.
     *
     * @return the taglib URI.
     */
    public String getTaglibURI();
}
