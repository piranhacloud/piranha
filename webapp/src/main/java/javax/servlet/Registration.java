/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.util.Map;
import java.util.Set;

/**
 * The Registration API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface Registration {

    /**
     * Get the class name.
     *
     * @return the class name.
     */
    public String getClassName();

    /**
     * Get the init parameter.
     *
     * @param name the name.
     * @return the value, or null if not found.
     */
    public String getInitParameter(String name);

    /**
     * Get the init parameters.
     *
     * @return the init parameters.
     */
    public Map<String, String> getInitParameters();

    /**
     * Get the name.
     *
     * @return the name.
     */
    public String getName();

    /**
     * Set the init parameter.
     *
     * @param name the name.
     * @param value the value.
     * @return true if it was set, false otherwise.
     */
    public boolean setInitParameter(String name, String value);

    /**
     * Set the init parameters.
     *
     * @param initParameters the init parameters.
     * @return the init parameters that could NOT be set.
     */
    public Set<String> setInitParameters(Map<String, String> initParameters);

    /**
     * The Registration.Dynamic API.
     *
     * @author Manfred Riem (mriem@manorrock.com)
     */
    interface Dynamic extends Registration {

        /**
         * Set async supported.
         *
         * @param asyncSupported the async supported flag.
         */
        public void setAsyncSupported(boolean asyncSupported);
    }
}
