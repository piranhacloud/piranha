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
package cloud.piranha.database.environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the EnvironmentOrPropertyDriver class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class EnvironmentDriverTest {

    /**
     * Test connect method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Disabled
    @Test
    public void testConnect() throws Exception {
//        System.getenv().put("PIRANHA_ENVIRONMENT.0.URL", "jdbc:h2:mem:test");
        Connection connection = DriverManager.getConnection("jdbc:environment:0");
        assertNotNull(connection);
    }

    /**
     * Test acceptsURL method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testAcceptsURL() throws Exception {
        assertNotNull(DriverManager.getDriver("jdbc:environment:0"));
    }

    /**
     * Test getPropertyInfo method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Disabled
    @Test
    public void testGetPropertyInfo() throws Exception {
        assertNotNull(DriverManager.getDriver("jdbc:environment:0").
                getPropertyInfo("jdbc:environment:0", new Properties()));
    }

    /**
     * Test getMajorVersion method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetMajorVersion() throws Exception {
        assertEquals(1, DriverManager.getDriver("jdbc:environment:0").getMajorVersion());
    }

    /**
     * Test getMinorVersion method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetMinorVersion() throws Exception {
        assertEquals(0, DriverManager.getDriver("jdbc:environment:0").getMinorVersion());
    }

    /**
     * Test jdbcCompliant method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testJdbcCompliant() throws Exception {
        assertTrue(DriverManager.getDriver("jdbc:environment:0").jdbcCompliant());
    }

    /**
     * Test getParentLogger method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetParentLogger() throws Exception {
        assertNotNull(DriverManager.getDriver("jdbc:environment:0").getParentLogger());
    }
}
