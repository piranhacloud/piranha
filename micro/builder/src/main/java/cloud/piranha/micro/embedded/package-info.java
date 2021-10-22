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
/**
 * <p>
 * This package delivers you with an embeddable Servlet container that hosts
 * only a single application without exposing an HTTP endpoint. It is used
 * extensively within the Piranha project itself to test all the Servlet
 * functionality.
 * </p>
 *
 * <p>
 * The image below illustrates how the request and response handling is done by
 * Piranha Embedded. When an {@link cloud.piranha.embedded.EmbeddedRequest}
 * comes in it uses a
 * {@link cloud.piranha.core.api.WebApplicationRequestMapper} to determine
 * which FilterChain needs to process the incoming request.
 * </p>
 *
 * <p>
 * <img alt="Embedded request and response handling" src="doc-files/request-response.png">
 * </p>
 * 
 * <h2>How do I use Piranha Embedded?</h2>
 *
 * <p>
 * See our <a href="https://piranha.cloud/embedded/">documentation</a> for more
 * information.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @see cloud.piranha.embedded.EmbeddedPiranhaBuilder
 */
package cloud.piranha.micro.embedded;
