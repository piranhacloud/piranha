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
package cloud.piranha.micro;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * The configuration settings for Piranha Micro
 *
 *
 * @author Arjan Tijms
 *
 */
public class MicroConfiguration {

    private String httpServer;
    private String version;
    private String extensions;
    private String dependencies;
    private String repositories;
    private boolean offline;
    private int port;

    private List<String> extensionsList;
    private List<String> repositoriesList;
    private List<String> mergedDependencies;
    

    /**
     * Default constructor. Initializes most of the stuff from System properties.
     */
    public MicroConfiguration() {
        this(
            System.getProperty("piranha.version", MicroConfiguration.class.getPackage().getImplementationVersion()),
            System.getProperty("piranha.extensions", "micro-core,micro-servlet"), 
                System.getProperty("piranha.dependencies", ""),
            System.getProperty("piranha.repositories", "https://repo1.maven.org/maven2"), 
            Boolean.valueOf(System.getProperty("piranha.offline", "false")),
            Integer.valueOf(System.getProperty("piranha.port", "8080")),
            System.getProperty("piranha.http.server", "impl"),

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
     * @param httpServer the HTTP server implementation to use.
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
        String httpServer,

        List<String> extensionsList,
        List<String> repositoriesList,
        List<String> mergedDependencies) {
        
        this.version = version;
        this.extensions = extensions;
        this.dependencies = dependencies;
        this.repositories = repositories;
        this.offline = offline;
        this.port = port;
        this.httpServer = httpServer;

        this.extensionsList = extensionsList;
        this.repositoriesList = repositoriesList;
        this.mergedDependencies = mergedDependencies;
    }

    public MicroConfiguration postConstruct() {
        extensionsList = stream(extensions.split(","))
                .map(extension -> extension.trim())
                .collect(toList());

        Stream<String> dependenciesFromExtensionsStream = extensionsList.stream()
                .map(extension -> "cloud.piranha.extension:piranha-extension-" + extension + ":" + version);

        Stream<String> directDependenciesStream = stream(dependencies.split(","))
                .map(dep -> dep.trim())
                .filter(dep -> !dep.isEmpty());

        repositoriesList = stream(repositories.split(","))
                .map(repo -> repo.trim())
                .filter(repo -> !repo.isEmpty())
                .collect(toList());


        mergedDependencies = Stream.of(
            Stream.of("cloud.piranha.http:piranha-http-" + httpServer + ":" + version),
            dependenciesFromExtensionsStream,
            directDependenciesStream
        ).flatMap(Function.identity()).collect(toList());

        return this;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getExtensions() {
        return extensions;
    }

    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }

    public String getRepositories() {
        return repositories;
    }

    public void setRepositories(String repositories) {
        this.repositories = repositories;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }
    
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<String> getExtensionsList() {
        return extensionsList;
    }

    public List<String> getRepositoriesList() {
        return repositoriesList;
    }

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
}
