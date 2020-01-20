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
package cloud.piranha.servlet.webxml;

import java.util.ArrayList;
import java.util.List;

/**
 * The web.xml manager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXmlManager  {
    
    /**
     * Stores the application scoped key.
     */
    public static final String KEY = WebXmlManager.class.getName();
    
    /**
     * Stores the unparsed web fragments.
     */
    private final ArrayList<WebXml> unparsedWebFragments = new ArrayList<>();
    
    /**
     * Stores the unparsed web.xml.
     */
    private WebXml unparsedWebXml;

    /**
     * Stores the web.xml.
     */
    private WebXml webXml;
    
    /**
     * Get the unparsed web fragments.
     * 
     * @return the unparsed web fragments.
     */
    public List<WebXml> getUnparsedWebFragments() {
        return unparsedWebFragments;
    }

    /**
     * Get the web.xml.
     * 
     * @return the web.xml.
     */
    public WebXml getWebXml() {
        return webXml;
    }
    
    /**
     * Set the web.xml.
     *
     * @param webXml the web.xml.
     */
    public void setWebXml(WebXml webXml) {
        this.webXml = webXml;
    }

    public WebXml getUnparsedWebXml() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setUnparsedWebXml(WebXml unparsedWebXml) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
