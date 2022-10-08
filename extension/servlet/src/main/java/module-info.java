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

/**
 * The Servlet extension module.
 *
 * <p>
 *  This module of modules delivers the extensions for a Servlet Container 
 *  alike runtime.
 * </p>
 * <ul>
 *  <li>Annotation Scanning</li>
 *  <li>Apache Commons File Upload (Multipart)</li>
 *  <li>Async</li>
 *  <li>Locale Encoding</li>
 *  <li>Logging</li>
 *  <li>Mime-type</li>
 *  <li>Naming (JNDI)</li>
 *  <li>Java Policy</li>
 *  <li>ServletContainerInitializer</li>
 *  <li>Servlet Security</li>
 *  <li>Servlet Annotations</li>
 *  <li>TEMPDIR</li>
 *  <li>WaSP (Pages)</li>
 *  <li>web.xml</li>
 *  <li>Welcome File</li>
 * </ul>
 */
module cloud.piranha.extension.servlet {
    
    exports cloud.piranha.extension.servlet;
    opens cloud.piranha.extension.servlet;
    requires cloud.piranha.core.api;
    requires cloud.piranha.extension.annotationscan;
    requires cloud.piranha.extension.apache.fileupload;
    requires cloud.piranha.extension.async;
    requires cloud.piranha.extension.naming;
    requires cloud.piranha.extension.localeencoding;
    requires cloud.piranha.extension.logging;
    requires cloud.piranha.extension.mimetype;
    requires cloud.piranha.extension.policy;
    requires cloud.piranha.extension.scinitializer;
    requires cloud.piranha.extension.security.servlet;
    requires cloud.piranha.extension.servletannotations;
    requires cloud.piranha.extension.tempdir;
    requires cloud.piranha.extension.wasp;
    requires cloud.piranha.extension.webxml;
    requires cloud.piranha.extension.welcomefile;
}
