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

import cloud.piranha.http.api.HttpServer;
import cloud.piranha.core.api.WebApplicationExtension;

/**
 * The Piranha Server module.
 *
 * <p>
 *  This module and distribution is deprecated. Please use the 
 *  cloud.piranha.dist.server module and its distribution instead.
 * </p>
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 * @deprecated
 */
@Deprecated(since = "22.10.0", forRemoval = true)
module cloud.piranha.server {
    exports cloud.piranha.server;
    opens cloud.piranha.server;
    requires cloud.piranha.core.api;
    requires cloud.piranha.core.impl;
    requires cloud.piranha.extension.servlet;
    requires cloud.piranha.extension.standard;
    requires cloud.piranha.extension.webprofile;
    requires cloud.piranha.http.api;
    requires cloud.piranha.http.webapp;
    requires java.logging;
    requires java.naming;
    uses HttpServer;
    uses WebApplicationExtension;
}
