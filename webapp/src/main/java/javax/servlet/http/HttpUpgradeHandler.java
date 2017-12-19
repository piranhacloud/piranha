/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

/**
 * The HttpUgradeHandler API.
 */
public interface HttpUpgradeHandler {

    /**
     * Initialize the upgrade.
     *
     * @param webConnection the web connection.
     */
    public void init(WebConnection webConnection);

    /**
     * Destroy the upgrade.
     */
    public void destroy();
}
