/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package test.micro.mojarra;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.File;
import java.util.concurrent.TimeUnit;

import me.alexpanov.net.FreePortFinder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * The integration tests for the Mojarra web application.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class MojarraIT {

    /**
     * Stores the process.
     */
    static private Process process;

    /**
     * Stores the port
     */
    private static int port;

    /**
     * Stores the web client.
     */
    private WebClient webClient;

    /**
     * Cleanup after tests.
     */
    @AfterAll
    public static void afterAll() {
        process.destroyForcibly();
    }

    /**
     * Cleanup after each test.
     */
    @AfterEach
    public void afterEach() {
        webClient.close();
    }

    /**
     * Setup before all tests.
     *
     * @throws Exception when an error occurs.
     */
    @BeforeAll
    public static void beforeAll() throws Exception {
        port = FreePortFinder.findFreeLocalPort();
        process = new ProcessBuilder()
                .directory(new File("target"))
                .command("java",
                        // "-Xdebug",
                        // "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5000",
                        "-jar",
                        "piranha-micro-shrinkwrap.jar",
                        "--port",
                        String.valueOf(port),
                        "--war",
                        "mojarra.war")
                .start();
        
        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * Setup before each test.
     */
    @BeforeEach
    public void beforeEach() {
        webClient = new WebClient();
    }

    /**
     * Test accessing /index.xhtml.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    @Disabled
    void testIndexXhtml() throws Exception {
        HtmlPage page = webClient.getPage("http://localhost:" + port + "/index.xhtml");
        assertTrue(page.asXml().contains("<body>Hello Mojarra</body>"));
    }
}
