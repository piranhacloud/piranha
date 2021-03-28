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
package cloud.piranha.micro.loader;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 *
 * @author Arjan Tijms
 *
 */
public class MicroDeployOutcome {

    /**
     * Stores the deployed servlets.
     */
    private Set<String> deployedServlets;

    /**
     * Stores the deployed applications.
     */
    private Consumer<Map<String, Object>> deployedApplication;
    
    /**
     * The context root to which the application was deployed. This is the first 
     * path after the the host.
     */
    private String deployedContextRoot;

   

    /**
     * @param deployMap the deployed application.
     * {@return the deploy outcome}
     */
    @SuppressWarnings("unchecked")
    public static MicroDeployOutcome ofMap(Map<String, Object> deployMap) {
        MicroDeployOutcome deployOutcome = new MicroDeployOutcome();
        deployOutcome.setDeployedServlets((Set<String>) deployMap.get("deployedServlets"));
        deployOutcome.setDeployedApplication((Consumer<Map<String, Object>>) deployMap.get("deployedApplication"));
        deployOutcome.setDeployedContextRoot((String) deployMap.get("deployedContextRoot"));
        
        return deployOutcome;
    }

    /**
     * {@return the deployed servlets}
     */
    public Set<String> getDeployedServlets() {
        return deployedServlets;
    }

    /**
     * Set the deployed servlets.
     *
     * @param deployedServlets the deployed servlets.
     */
    public void setDeployedServlets(Set<String> deployedServlets) {
        this.deployedServlets = deployedServlets;
    }

    /**
     * Get the deployed application.
     *
     * @return the deployed application map.
     */
    public Consumer<Map<String, Object>> getDeployedApplication() {
        return deployedApplication;
    }

    /**
     * Set the deployed application.
     *
     * @param deployedApplication the deployed application.
     */
    public void setDeployedApplication(Consumer<Map<String, Object>> deployedApplication) {
        this.deployedApplication = deployedApplication;
    }
    
    /**
     * {@return the deployed context root}
     */
    public String getDeployedContextRoot() {
        return deployedContextRoot;
    }

    /**
     * Set the deployed context root
     * 
     * @param deployedContextRoot the deployed context root
     */
    public void setDeployedContextRoot(String deployedContextRoot) {
        this.deployedContextRoot = deployedContextRoot;
    }
}
