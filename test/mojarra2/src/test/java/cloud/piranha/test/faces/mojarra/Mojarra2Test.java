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
package cloud.piranha.test.faces.mojarra;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.jupiter.api.Test;

import cloud.piranha.micro.embedded.MicroEmbeddedPiranhaBuilder;

/**
 * The JUnit tests for the Hello Jakarta Faces web application.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @author Arjan Tijms (arjan.tijms@gmail.com)
 */
class Mojarra2Test {

    /**
     * Test /index.html.
     *
     * @throws Exception
     */
    @Test
    void testIndexHtml1() throws Exception {
    	String result =
    	new MicroEmbeddedPiranhaBuilder()
    		.archive(
				ShrinkWrap
					.create(WebArchive.class)
	                  .addAsWebResource(new StringAsset( """
                          <!DOCTYPE html>

                          <html lang="en" xmlns:h="http://xmlns.jcp.org/jsf/html">
                              <h:head>
                                  <title>Hello Jakarta Faces</title>
                              </h:head>    
                              <h:body>
                                  Hello Jakarta Faces
                              </h:body>
                          </html>
                          """), "index.xhtml")
	                  .addAsWebInfResource(EmptyAsset.INSTANCE, "faces-config.xml")
	                  .addAsLibraries(
                		  Maven.resolver()
                		       .resolve(
            		    		   //"org.glassfish:jakarta.faces:3.0.0", // Needs fix for #1444
            		    		   "jakarta.websocket:jakarta.websocket-api:2.0.0"
                		    	)
                		       .withTransitivity().as(JavaArchive.class)))
        		.buildAndStart()
        		.service("/index.xhtml")
        		.getResponseAsString();
    		
        System.out.println(result);
        assertTrue(result.contains("Hello Jakarta Faces"));
    }
}
