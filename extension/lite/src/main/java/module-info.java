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
 * The Lite extension.
 *
 * <p>
 *  This module delivers the Lite extension which in turn enables the following
 *  support:
 * </p>
 * <ul>
 *  <li>Annotation scanning support</li>
 *  <li>JNDI support</li>
 *  <li>Policy support</li>
 *  <li>Servlet Security support</li>
 * </ul>
 */
module cloud.piranha.extension.lite {
    exports cloud.piranha.extension.lite;
    opens cloud.piranha.extension.lite;
    requires cloud.piranha.core.api;
    requires transitive cloud.piranha.extension.annotationscan;
    requires transitive cloud.piranha.extension.herring;
    requires transitive cloud.piranha.extension.policy;
    requires transitive cloud.piranha.extension.security.servlet;
    requires transitive cloud.piranha.extension.standard.localeencoding;
    requires transitive cloud.piranha.extension.standard.mimetype;
    requires transitive cloud.piranha.extension.standard.servletannotations;
    requires transitive cloud.piranha.extension.standard.scinitializer;
    requires transitive cloud.piranha.extension.standard.tempdir;
    requires transitive cloud.piranha.extension.standard.webxml;
}
