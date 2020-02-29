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
/**
 * <p>
 * The Piranha Server package delivers you with a Servlet container that is
 * capable of hosting several web applications.
 * </p>
 *
 * <h2>Installing Piranha Server</h2>
 *
 * <p>
 * Download the ZIP file from
 * <a href="https://repo1.maven.org/maven2/cloud/piranha/piranha-server">Maven
 * Central</a> and unzip it in a directory of your choice.
 * </p>
 *
 * <h2>Start the server</h2>
 *
 * <p>
 * Starting the server is done by issuing the command line below from the
 * <code>bin</code> directory.
 * </p>
 *
 * <pre>
 * start.sh
 * </pre>
 *
 * <h2>Stop the server</h2>
 *
 * <p>
 * Stopping the server is done by issuing the command line below from the
 * <code>bin</code> directory.
 * </p>
 *
 * <pre>
 * stop.sh
 * </pre>
 *
 * <h2>Deploying a web application</h2>
 *
 * <p>
 * Deploying a web application is a simple as copying your WAR file to the
 * <code>webapps</code> directory. Note if the server is already running you
 * will need to stop and start the server.
 * </p>
 *
 * <h2>Architecture diagram</h2>
 *
 * <p>
 * The image below illustrates how the request and response handling is done by
 * Piranha Server. When a request comes in to the HTTP server it dispatches it
 * to the {@link cloud.piranha.api.WebApplicationServer} which uses a
 * {@link cloud.piranha.api.WebApplicationServerRequestMapper} to determine
 * which web application needs to serve the request and it the dispatches to it
 * and then in turn the WebApplication uses
 * {@link cloud.piranha.api.WebApplicationRequestMapper} to determine which
 * FilterChain needs to process the incoming request and it dispatches to it.
 * </p>
 *
 * <p>
 * <img alt="Request and response handling" src="doc-files/request-response.png">
 * </p>
 *
 * <h2>Maven coordinates</h2>
 *
 * <pre>
 * &lt;dependency&gt;
 *   &lt;groupId&gt;cloud.piranha&lt;/groupId&gt;
 *   &lt;artifactId&gt;piranha-serverd&lt;/artifactId&gt;
 *   &lt;version&gt;y.m.p&lt;/version&gt;
 *   &lt;version&gt;y.m.p&lt;/version&gt;
 * &lt;dependency&gt;
 * </pre>
 *
 * <p>
 * where y is the year, m is the month and p is the patch version of the release
 * you want to use.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
package cloud.piranha.server;
