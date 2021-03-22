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

import static java.util.Arrays.stream;
import static java.util.function.Function.identity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * The configuration for Piranha Micro.
 *
 * @author Arjan Tijms
 */
public class MicroConfiguration {

    /**
     * Stores the HTTP server implementation.
     */
    private String httpServer;
    
    /**
     * Stores the HTTP start flag.
     */
    private boolean httpStart;
    
    /**
     * Stores the version.
     */
    private String version;
    
    /**
     * Stores the extensions.
     */
    private String extensions;
    
    /**
     * Stores the dependencies.
     */
    private String dependencies;
    
    /**
     * Stores the repositories.
     */
    private String repositories;
    
    /**
     * Stores the offline flag.
     */
    private boolean offline;
    
    /**
     * Stores the port.
     */
    private int port;
    
    /**
     * If true, the context root for the web app is taken from the war name.
     */
    private boolean rootIsWarName;

    /**
     * Stores the root.
     */
    private String root;

    /**
     * Stores the list of extensions.
     */
    private List<String> extensionsList;
    
    /**
     * Stores the list of repositories.
     */
    private List<String> repositoriesList;
    
    /**
     * Stores the merged dependencies.
     */
    private List<String> mergedDependencies;

    /**
     * Default constructor. Initializes most of the stuff from System
     * properties.
     */
    public MicroConfiguration() {
        this(
                System.getProperty("piranha.version", MicroConfiguration.class.getPackage().getImplementationVersion()),
                System.getProperty("piranha.extensions", "micro-core,micro"),
                System.getProperty("piranha.dependencies", ""),
                System.getProperty("piranha.repositories", "https://repo1.maven.org/maven2"),
                Boolean.valueOf(System.getProperty("piranha.offline", "false")),
                Integer.valueOf(System.getProperty("piranha.port", "8080")),
                Boolean.valueOf(System.getProperty("piranha.rootIsWarName", "false")),
                System.getProperty("piranha.root"),
                System.getProperty("piranha.http.server", "impl"),
                Boolean.valueOf(System.getProperty("piranha.http.start", "true")),
                null,
                null,
                null);
    }

    /**
     * Constructor.
     *
     * @param version Piranha version.
     * @param extensions Piranha extensions.
     * @param dependencies Piranha dependencies.
     * @param repositories Piranha repositories.
     * @param offline Offline flag.
     * @param port http port on which Piranha listens to requests.
     * @param rootIsWarName sets that the war name should be used for the root context
     * @param root the context root for web applications
     * @param httpServer the HTTP server implementation to use.
     * @param httpStart whether or not to start the HTTP server.
     * @param extensionsList List of extensions.
     * @param repositoriesList List of repos.
     * @param mergedDependencies List of merged dependencies.
     */
    public MicroConfiguration(
            String version,
            String extensions,
            String dependencies,
            String repositories,
            boolean offline,
            int port,
            boolean rootIsWarName,
            String root,
            String httpServer,
            boolean httpStart,
            List<String> extensionsList,
            List<String> repositoriesList,
            List<String> mergedDependencies) {

        this.version = version;
        this.extensions = extensions;
        this.dependencies = dependencies;
        this.repositories = repositories;
        this.offline = offline;
        this.port = port;
        this.rootIsWarName = rootIsWarName;
        this.root = root;
        this.httpServer = httpServer;
        this.httpStart = httpStart;

        this.extensionsList = extensionsList;
        this.repositoriesList = repositoriesList;
        this.mergedDependencies = mergedDependencies;
    }

    /**
     * Handle post construct.
     * 
     * @return the configuration.
     */
    public MicroConfiguration postConstruct() {
        if (root != null) {
            if (root.equalsIgnoreCase("ROOT")) {
                root = "";
            } else if (!root.startsWith("/")) {
                root = "/" + root;
            }
        }

        extensionsList = stream(extensions.split(","))
                .map(String::trim)
                .toList();

        Stream<String> dependenciesFromExtensionsStream = extensionsList.stream()
                .map(extension -> "cloud.piranha.extension:piranha-extension-" + extension + ":" + version);

        Stream<String> directDependenciesStream = stream(dependencies.split(","))
                .map(String::trim)
                .filter(dep -> !dep.isEmpty());

        repositoriesList = stream(repositories.split(","))
                .map(String::trim)
                .filter(repo -> !repo.isEmpty())
                .toList();

        mergedDependencies = Stream.of(
                Stream.of("cloud.piranha.http:piranha-http-" + httpServer + ":" + version),
                dependenciesFromExtensionsStream,
                directDependenciesStream
        ).flatMap(identity()).toList();

        return this;
    }

    /**
     * Construct a map for the configuration.
     * 
     * @return the map.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> config = new HashMap<>();
        config.put("micro.port", getPort());
        config.put("micro.rootIsWarName", rootIsWarName);
        if (getRoot() != null) {
            config.put("micro.root", getRoot());
        }
        config.put("micro.http.start", httpStart);

        return config;
    }

    /**
     * Get the version.
     * 
     * @return the version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set the version.
     * 
     * @param version the version.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Get the extensions.
     * 
     * @return the extensions.
     */
    public String getExtensions() {
        return extensions;
    }

    /**
     * Set the extensions.
     * 
     * @param extensions the extensions.
     */
    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }

    /**
     * Get the repositories.
     * 
     * @return the repositories.
     */
    public String getRepositories() {
        return repositories;
    }

    /**
     * Set the repositories.
     * 
     * @param repositories the repositories.
     */
    public void setRepositories(String repositories) {
        this.repositories = repositories;
    }

    /**
     * Are we offline.
     * 
     * @return true if so, false otherwise.
     */
    public boolean isOffline() {
        return offline;
    }

    /**
     * Set the offline flag.
     * 
     * @param offline the offline flag.
     */
    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    /**
     * Get the port.
     * 
     * @return the port.
     */
    public int getPort() {
        return port;
    }

    /**
     * Set the port.
     * 
     * @param port the port.
     */
    public void setPort(int port) {
        this.port = port;
    }
    
    /**
     * Whether the war name is used for the context root
     * 
     * @return whether the war name is used for the context root
     */
    public boolean isRootIsWarName() {
        return rootIsWarName;
    }

    /**
     * Sets that the war name should be used for the root context of a web app.
     * 
     * <p>
     * Setting this to true overrides the explicit context root set via the <code>setRoot</code> method.
     * 
     * @param rootIsWarName
     */
    public void setRootIsWarName(boolean rootIsWarName) {
        this.rootIsWarName = rootIsWarName;
    }

    /**
     * Get the root.
     * 
     * @return the root.
     */
    public String getRoot() {
        return root;
    }

    /**
     * Set the root.
     * 
     * @param root the root.
     */
    public void setRoot(String root) {
        this.root = root;
    }

    /**
     * Get the list of extensions.
     * 
     * @return the list of extensions.
     */
    public List<String> getExtensionsList() {
        return extensionsList;
    }

    /**
     * Get the list of repositories.
     * 
     * @return the list of repositories.
     */
    public List<String> getRepositoriesList() {
        return repositoriesList;
    }

    /**
     * Get the merged dependencies.
     * 
     * @return the merged dependencies.
     */
    public List<String> getMergedDependencies() {
        return mergedDependencies;
    }

    /**
     * Get the HTTP server engine.
     *
     * @return the HTTP server engine.
     */
    public String getHttpServer() {
        return httpServer;
    }

    /**
     * Set the HTTP server engine to use.
     *
     * @param httpServer the HTTP server engine.
     */
    public void setHttpServer(String httpServer) {
        this.httpServer = httpServer;
    }

    /**
     * Checks whether the config asks to start the http server
     *
     * @return true if an http server will be started, false otherwise
     */
    public boolean isHttpStart() {
        return httpStart;
    }

    /**
     * Sets whether the config asks to start the http server
     *
     * @param httpStart the HTTP start flag.
     */
    public void setHttpStart(boolean httpStart) {
        this.httpStart = httpStart;
    }
}
