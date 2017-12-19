package com.manorrock.webapp.test.wicket;

import org.apache.wicket.protocol.http.WebApplication;

/**
 * Application object.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WicketApplication extends WebApplication {

    /**
     * Get the HomePage class.
     * 
     * @return the HomePage class.
     */
    @Override
    public Class<HomePage> getHomePage() {
        return HomePage.class;
    }

    /**
     * Initialize application.
     */
    @Override
    public void init() {
        super.init();
    }
}
