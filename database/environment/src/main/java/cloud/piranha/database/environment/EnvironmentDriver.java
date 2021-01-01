/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package cloud.piranha.database.environment;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * A JDBC driver that looks for environment variables to delegate to a another
 * JDBC driver.
 *
 * <p>
 * E.g. if you set the JDBC url in your application to be jdbc:environment:0 it
 * will look for environment variables of the format:
 * </p>
 * <pre>
 *    PIRANHA_ENVIRONMENT.0.URL=
 *    PIRANHA_ENVIRONMENT.0.PROPERTY.1=name=value
 *    PIRANHA_ENVIRONMENT.0.PROPERTY.2=name2=value2
 * </pre>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class EnvironmentDriver implements Driver {
    
    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(EnvironmentDriver.class.getName());

    /**
     * Constructor.
     */
    public EnvironmentDriver() {
        try {
            DriverManager.registerDriver(this);
        } catch (SQLException se) {
            LOGGER.severe("SQL error occured while registering JDBC driver");
        }
    }

    /**
     * Connect to the database.
     * 
     * @param url the URL.
     * @param info the connection properties. 
     * @return the connection
     * @see Driver#connect(java.lang.String, java.util.Properties)
     * @throws SQLException when a SQL error occurs.
     */
    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        String name = url.substring("jdbc:environment:".length());
        String delegateUrl = null;
        Properties delegateProperties = new Properties();
        for(String key : System.getenv().keySet()) {
            if (key.startsWith("PIRANHA_ENVIRONMENT." + name + ".URL")) {
                delegateUrl = System.getenv("PIRANHA_ENVIRONMENT." + name + ".URL");
            }
        }
        Driver driver = DriverManager.getDriver(delegateUrl);
        return driver.connect(delegateUrl, delegateProperties);
    }

    /**
     * De we accept the URL?
     * 
     * @return true if we do, false otherwise.
     * @see Driver#acceptsURL(java.lang.String)
     */
    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return url != null && url.contains("jdbc:environment:");
    }

    /**
     * Get the property information.
     * 
     * @param url the URL.
     * @param info the properties
     * @return the list of property information.
     * @see Driver#getPropertyInfo(java.lang.String, java.util.Properties)
     */
    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        String delegateUrl = null;
        Properties delegateInfo = null;
        Driver driver = DriverManager.getDriver(delegateUrl);
        return driver.getPropertyInfo(delegateUrl, delegateInfo);
    }

    /**
     * Get the major version.
     *
     * @return 1.
     */
    @Override
    public int getMajorVersion() {
        return 1;
    }

    /**
     * Get the minor version.
     *
     * @return 0.
     */
    @Override
    public int getMinorVersion() {
        return 0;
    }

    /**
     * Are we JDBC compliant.
     *
     * @return true.
     */
    @Override
    public boolean jdbcCompliant() {
        return true;
    }

    /**
     * Get the parent logger.
     * 
     * @return the parent logger.
     * @see Driver#getParentLogger()
     */
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(EnvironmentDriver.class.getName());
    }
}
