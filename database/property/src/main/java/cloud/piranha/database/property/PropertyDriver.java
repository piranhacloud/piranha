/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.database.property;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * A JDBC driver that looks for system properties to delegate to a another JDBC
 * driver.
 *
 * <p>
 * E.g. if you set the JDBC url in your application to be jdbc:property:0 it
 * will look for system properties of the format:
 * </p>
 * <pre>
 *    property.0.url=
 *    property.0.property.1=name=value
 *    property.0.property.2=name2=value2
 * </pre>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class PropertyDriver implements Driver {

    /**
     * Constructor.
     */
    public PropertyDriver() {
        try {
            DriverManager.registerDriver(this);
        } catch (SQLException se) {

        }
    }

    /**
     * @see Driver#connect(java.lang.String, java.util.Properties)
     */
    public Connection connect(String url, Properties info) throws SQLException {
        String name = url.substring("jdbc:property:".length());
        String delegateUrl = null;
        Properties delegateProperties = new Properties();
        for (String key : System.getProperties().stringPropertyNames()) {
            if (key.startsWith("property." + name + ".url")) {
                delegateUrl = System.getProperty("property." + name + ".url");
            }
        }
        Driver driver = DriverManager.getDriver(delegateUrl);
        return driver.connect(delegateUrl, delegateProperties);
    }

    /**
     * @see Driver#acceptsURL(java.lang.String)
     */
    public boolean acceptsURL(String url) throws SQLException {
        return url != null && url.contains("jdbc:property:");
    }

    /**
     * @see Driver#getPropertyInfo(java.lang.String, java.util.Properties)
     */
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
    public int getMajorVersion() {
        return 1;
    }

    /**
     * Get the minor version.
     *
     * @return 0.
     */
    public int getMinorVersion() {
        return 0;
    }

    /**
     * Are we JDBC compliant.
     *
     * @return true.
     */
    public boolean jdbcCompliant() {
        return true;
    }

    /**
     * @see Driver#getParentLogger()
     */
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(PropertyDriver.class.getName());
    }
}
