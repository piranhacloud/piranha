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
 *  This module delivers the temporary directory functionality required for web
 *  applications.
 * </p>
 * <p>
 *  It includes the following:
 * </p>
 * <ul>
 *  <li>A WebApplicationExtension</li>
 *  <li>A ServletContainerInitializer</li>
 * </ul>
 * <h2>The WebApplicationExtension</h2>
 * <p>
 *  The extension is responsible for adding the ServletContainerInitializer.
 * </p>
 * <h2>The ServletContainerInitializer</h2>
 * <p>
 *  The initializer is responsible for creating the temporary directory on the
 *  filesystem and setting the context attribute to point to that directory.
 * </p>
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
module cloud.piranha.extension.tempdir {
    exports cloud.piranha.extension.tempdir;
    opens cloud.piranha.extension.tempdir;
    requires cloud.piranha.core.api;
}
