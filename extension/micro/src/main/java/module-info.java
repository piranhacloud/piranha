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

import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.extension.micro.MicroExtension;

/**
 * This module delivers the meta extension for Piranha Micro.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
module cloud.piranha.extension.micro {

    exports cloud.piranha.extension.micro;
    opens cloud.piranha.extension.micro;
    provides WebApplicationExtension with MicroExtension;
    requires cloud.piranha.core.api;
    requires cloud.piranha.extension.apache.fileupload;
    requires cloud.piranha.extension.datasource;
    requires cloud.piranha.extension.eclipselink;
    requires cloud.piranha.extension.exousia;
    requires cloud.piranha.extension.expressly;
    requires cloud.piranha.extension.jersey;
    requires cloud.piranha.extension.naming.cdi;
    requires cloud.piranha.extension.policy;
    requires cloud.piranha.extension.scinitializer;
    requires cloud.piranha.extension.security.jakarta;
    requires cloud.piranha.extension.security.servlet;
    requires cloud.piranha.extension.servletannotations;
    requires cloud.piranha.extension.tempdir;
    requires cloud.piranha.extension.transact;
    requires cloud.piranha.extension.wasp;
    requires cloud.piranha.extension.webxml;
    requires cloud.piranha.extension.welcomefile;
}
