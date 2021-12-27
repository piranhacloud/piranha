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
package cloud.piranha.extension.apache.fileupload.tests;

import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationRequest;
import cloud.piranha.extension.apache.fileupload.ApacheMultiPartManager;
import jakarta.servlet.MultipartConfigElement;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the ApacheMultiPartManager class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class ApacheMultiPartManagerTest {

    /**
     * Test getPart method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetPart() throws Exception {
        DefaultWebApplication application = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setMultipartConfig(new MultipartConfigElement(""));
        request.setContentType("multipart/form-data");
        request.setMethod("POST");
        ApacheMultiPartManager manager = new ApacheMultiPartManager();
        assertNull(manager.getPart(application, request, "part_test"));
    }
}
