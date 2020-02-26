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
 * Piranha Nano is the smallest in our lineup and it delivers you with a very
 * opinionated partial embeddable implementation of a Servlet container. While
 * very small it most certainly is very capable and it is very easy to use.
 * </p>
 *
 * <p>
 * Internally Piranha Nano uses a very light-weight request and response chain
 * that is illustrated by the image below. Note since there is no notion of
 * mapping the request URI there is only ONE chain of Filters and a Servlet. The
 * arrows illustrate the flow the order in which the Filters and the Servlet
 * will be called.
 * </p>
 *
 * <p>
 * <img alt="Nano request and response chain" src="doc-files/request-response.png">
 * </p>
 *
 * <h2>Recommendation</h2>
 *
 * <p>
 * We recommend using {@link cloud.piranha.nano.NanoPiranhaBuilder} to create
 * your instance of {@link cloud.piranha.nano.NanoPiranha} as it will take care
 * of the initialization of the Filters and the Servlet.
 * </p>
 *
 * <h2>Expectations and assumptions</h2>
 *
 * <ul>
 * <li>You take care of class loading (if more isolation is needed)</li>
 * <li>You add and initialize the filters you want to use in the right
 * order</li>
 * <li>You add and initialize the one servlet you want to use (optional)</li>
 * <li>You setup the request object</li>
 * <li>You setup the response object</li>
 * </ul>
 *
 * <h2>Limitations</h2>
 *
 * <ul>
 * <li>Does not support the ServletContainerInitializer API</li>
 * <li>Does not support the ServletContextListener API</li>
 * <li>Does not support the ServletRequestListener API</li>
 * <li>Does not support request dispatching API</li>
 * <li>Does not support asynchronous Servlet API</li>
 * <li>Does not support HTTP sessions</li>
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
 *   &lt;artifactId&gt;piranha-nano&lt;/artifactId&gt;
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
 * @see cloud.piranha.nano.NanoPiranhaBuilder
 */
package cloud.piranha.nano;
