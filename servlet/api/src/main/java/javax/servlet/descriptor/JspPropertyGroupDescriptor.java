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
package javax.servlet.descriptor;

import java.util.Collection;

/**
 * The JspPropertyGroupDescriptor API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface JspPropertyGroupDescriptor {

    /**
     * Get the default buffer size.
     *
     * @return the default buffer size.
     */
    public String getBuffer();

    /**
     * Get the default content type type.
     *
     * @return the default content type, or null if not specified.
     */
    public String getDefaultContentType();

    /**
     * Get deferred-syntax-allowed-as-literal config.
     *
     * @return the deferred-syntax-allowed-as-literal config.
     */
    public String getDeferredSyntaxAllowedAsLiteral();

    /**
     * Get the EL ignored config.
     *
     * @return the EL ignored config.
     */
    public String getElIgnored();

    /**
     * Get the error-on-undeclared-namespace config.
     *
     * @return the error-on-undeclared-namespace config.
     */
    public String getErrorOnUndeclaredNamespace();

    /**
     * Get the include-codes config.
     *
     * @return the include-codes config.
     */
    public Collection<String> getIncludeCodas();

    /**
     * Get the include-prelude config.
     *
     * @return the include-prelude config.
     */
    public Collection<String> getIncludePreludes();

    /**
     * Get the is-xml config.
     *
     * @return the is-xml config.
     */
    public String getIsXml();

    /**
     * Get the scripting invalid config.
     *
     * @return the scripting invalid config.
     */
    public String getScriptingInvalid();

    /**
     * Get the page encoding.
     *
     * @return the page encoding.
     */
    public String getPageEncoding();

    /**
     * Get the trim-directive-whitespaces config.
     *
     * @return the trim-directive-whitespaces config.
     */
    public String getTrimDirectiveWhitespaces();

    /**
     * Get the URL patterns.
     *
     * @return the URL patterns.
     */
    public Collection<String> getUrlPatterns();
}
