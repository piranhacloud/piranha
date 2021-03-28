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
package cloud.piranha.micro.core;

import static org.jboss.weld.environment.deployment.discovery.jandex.Jandex.INDEX_ATTRIBUTE_NAME;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import jakarta.annotation.Priority;

import org.jboss.jandex.Index;
import org.jboss.jandex.IndexReader;
import org.jboss.weld.environment.deployment.discovery.BeanArchiveBuilder;
import org.jboss.weld.environment.deployment.discovery.BeanArchiveHandler;

/**
 * An Archive handler for Weld that implicitly reads the classes in
 * an archive deployed to Piranha via the META-INF/piranha.idx file.
 * 
 * @author Arjan Tijms
 *
 */
@Priority(10)
public class PiranhaBeanArchiveHandler implements BeanArchiveHandler {

    @Override
    public BeanArchiveBuilder handle(String beanArchiveReference) {
        
        String indexURL = null;
        
        // We're only handling the classes in the application archive, which is represented
        // by /WEB-INF/classes
        if ("/WEB-INF/classes".equals(beanArchiveReference)) {
            indexURL = "shrinkwrap://cloud.piranha.modular.classes/META-INF/jandex.idx";
        } else {
            try {
                URL url = new URL(beanArchiveReference);
                String protocol = url.getProtocol();
                if (!protocol.equals("shrinkwrap")) {
                    return null;
                }
                
                indexURL = "shrinkwrap://" + url.getHost() + "/META-INF/jandex.idx";
                
            } catch (MalformedURLException e1) {
                throw new IllegalStateException(e1);
            }
        }
        
        // The beanArchiveBuilder is a builder the native archive type for Weld.
        // It roughly corresponds to a Shrinkwrap Archive builder.
        BeanArchiveBuilder beanArchiveBuilder = new BeanArchiveBuilder();
        
        // Get the class and annotation index 
        Index index = getIndex(indexURL);
        
        beanArchiveBuilder.setAttribute(INDEX_ATTRIBUTE_NAME, index);
        
        // Populate the Weld Archive with all the classes from the index, representing
        // the original application archive.
        index.getKnownClasses()
             .stream()
             .map(e -> e.asClass().name().toString())
             .forEach(beanArchiveBuilder::addClass);
        
        return beanArchiveBuilder;
    }
    
    Index getIndex(String indexURL) {
        try (InputStream indexStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(indexURL)) {
            return new IndexReader(indexStream).read();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

}
