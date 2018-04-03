/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
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
