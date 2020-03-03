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
 * The Piranha Micro package delivers you with a Servlet container that hosts
 * one web application.
 * </p>
 *
 * <h2>Installing Piranha Micro</h2>
 *
 * <p>
 * Download the JAR file from
 * <a href="https://repo1.maven.org/maven2/cloud/piranha/piranha-micro">Maven
 * Central</a>.
 * </p>
 *
 * <h2>Running with a WAR file</h2>
 *
 * <p>
 * If you have a WAR file you can use the command line below:
 * </p>
 *
 * <pre>
 * java -jar piranha-micro.jar --war your_webapplication.war
 * </pre>
 *
 * <h2>Overriding the web application directory</h2>
 *
 * <p>
 * If you want to override the directory Piranha Micro uses for extracting the
 * web application you can use the command line below:
 * </p>
 *
 * <pre>
 * java -jar piranha-micro.jar --war your_webapplication.war --webapp your_webapp_directory
 * </pre>
 *
 * <h2>Running with an exploded WAR file</h2>
 *
 * <p>
 * If you have an exploded directory containing the contents of your WAR file
 * you can use the command line below:
 * </p>
 *
 * <pre>
 * java -jar piranha-micro.jar --webapp your_webapp_directory
 * </pre>
 *
 * <h2>Architecture diagram</h2>
 *
 * <p>
 * The image below illustrates how the request and response handling is done by
 * Piranha Micro. When a request comes in to the HTTP server it dispatches it
 * to the {@link cloud.piranha.api.WebApplicationServer} which in turn dispatches
 * it to the WebApplication which then in turn uses
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
 *   &lt;artifactId&gt;piranha-micro&lt;/artifactId&gt;
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
package cloud.piranha.micro;
