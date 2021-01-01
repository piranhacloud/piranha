/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import cloud.piranha.http.api.HttpServer;
import cloud.piranha.micro.core.CdiExtension;
import cloud.piranha.micro.core.PiranhaBeanArchiveHandler;
import cloud.piranha.webapp.api.WebApplicationExtension;
import jakarta.enterprise.inject.spi.Extension;
import org.jboss.weld.environment.deployment.discovery.BeanArchiveHandler;

module cloud.piranha.micro.core {
    
    exports cloud.piranha.micro.core;

    provides BeanArchiveHandler with PiranhaBeanArchiveHandler;
    provides Extension with CdiExtension;

    requires cloud.piranha.api;
    requires cloud.piranha.cdi.weld;
    requires cloud.piranha.http.api;
    requires cloud.piranha.http.webapp;
    requires cloud.piranha.naming.api;
    requires cloud.piranha.naming.thread;
    requires cloud.piranha.resource.shrinkwrap;
    requires cloud.piranha.security.jakarta;
    requires cloud.piranha.servlet.api;
    requires cloud.piranha.webapp.api;
    requires cloud.piranha.webapp.impl;
    requires jakarta.annotation;
    requires jakarta.cdi;
    requires jakarta.security.enterprise.api;
    requires java.naming;
    requires java.logging;
    requires java.xml;
    requires org.jboss.jandex;
    requires shrinkwrap.api;
    requires weld.environment.common;

    uses HttpServer;
    uses WebApplicationExtension;
}
