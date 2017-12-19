/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.descriptor;

import java.util.Collection;

/**
 * The JspConfigDescriptor API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface JspConfigDescriptor {

    /**
     * Get the JSP property groups.
     *
     * @return the JSP property groups.
     */
    public Collection<JspPropertyGroupDescriptor> getJspPropertyGroups();

    /**
     * Get the taglibs.
     *
     * @return the taglibs.
     */
    public Collection<TaglibDescriptor> getTaglibs();

}
