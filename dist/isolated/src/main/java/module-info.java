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

import cloud.piranha.http.api.HttpServer;

/**
 * This module delivers the Piranha Isolated distribution.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
module cloud.piranha.dist.isolated {
    
    opens cloud.piranha.dist.isolated;
    exports cloud.piranha.dist.isolated;
    requires transitive cloud.piranha.core.api;
    requires cloud.piranha.embedded;
    requires cloud.piranha.extension.servlet;
    requires cloud.piranha.feature.exitonstop;
    requires cloud.piranha.feature.http;
    requires cloud.piranha.feature.https;
    requires cloud.piranha.feature.logging;
    requires cloud.piranha.http.webapp;
    requires cloud.piranha.micro.builder;
    requires cloud.piranha.micro.loader;
    requires cloud.piranha.resource.shrinkwrap;
    requires org.jboss.jandex;
    requires shrinkwrap.api;
    requires shrinkwrap.resolver.api.maven;
    requires java.logging;
    uses HttpServer;
}
