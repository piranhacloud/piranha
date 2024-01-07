/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
 * This package delivers you with a Servlet container that hosts only a single
 * application.
 * </p>
 *
 * <h2>Architecture diagram</h2>
 *
 * <p>
 * The image below illustrates how the request and response handling is done by
 * Piranha Micro. When a request comes in to the HTTP server it dispatches it to
 * the WebApplicationServer which in turn
 * dispatches it to the <code>WebApplication</code> which then in turn uses
 * <code>WebApplicationRequestMapper</code> to determine
 * which FilterChain needs to process the incoming request and it dispatches to
 * it.
 * </p>
 *
 * <p>
 * <img alt="Request and response handling" src="doc-files/request-response.png">
 * </p>
 * 
 * <h2>How do I use Piranha Micro?</h2>
 *
 * <p>
 * See our <a href="https://piranha.cloud/micro/">documentation</a> for more
 * information.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
package cloud.piranha.dist.micro;
