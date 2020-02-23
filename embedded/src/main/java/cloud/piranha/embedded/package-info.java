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
 * Piranha Embedded delivers you with an embeddable implementation of a Servlet
 * container. It is used extensively within the Piranha project itself to test
 * all the Servlet functionality.
 * </p>
 *
 * <p>
 * The image below illustrates how the request and response handling is done by
 * Piranha Embedded. When an {@link cloud.piranha.embedded.EmbeddedRequest} comes
 * in it uses a {@link cloud.piranha.api.WebApplicationRequestMapper} to
 * determine which FilterChain needs to process the incoming request.
 * </p>
 *
 * <p>
 * <img alt="Embedded request and response handling" src="doc-files/request-response.png">
 * </p>
 *
 * <h2>Recommendation</h2>
 *
 * <p>
 * We recommend using {@link cloud.piranha.embedded.EmbeddedPiranhaBuilder} to
 * create your instances of {@link cloud.piranha.embedded.EmbeddedPiranha}..
 * </p>
 *
 * <h2>Expectations and assumptions</h2>
 *
 * <ul>
 * <li>You take care of class loading (if more isolation is needed)</li>
 * <li>You setup the request object</li>
 * <li>You setup the response object</li>
 * </ul>
 *
 * <h2>Limitations</h2>
 *
 * <ul>
 * <li>Only supports one web application per
 * {@link cloud.piranha.embedded.EmbeddedPiranha} instance.</li>
 * </ul>
 *
 * <h2>Maven coordinates</h2>
 *
 * <p>
 * Please use the following dependency.
 * </p>
 *
 * <pre>
 * &lt;dependency&gt;
 *   &lt;groupId&gt;cloud.piranha&lt;/groupId&gt;
 *   &lt;artifactId&gt;piranha-embedded&lt;/artifactId&gt;
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
 * @see cloud.piranha.embedded.EmbeddedPiranhaBuilder
 */
package cloud.piranha.embedded;
