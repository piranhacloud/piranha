/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package hello;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.stream.JsonParser;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import java.io.StringReader;

/**
 * The HelloJson bean.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("")
@RequestScoped
public class HelloBean {

    /**
     * Stores the injected 'Hello Inject!'.
     */
    @Inject
    private HelloInject injected;

    /**
     * Stores the intercept bean.
     */
    @Inject
    private HelloInterceptBean interceptBean;

    /**
     * Say 'Hello Inject!' using the Inject annotation.
     *
     * @return 'Hello Inject!'.
     */
    @GET
    @Path("/helloInject")
    public String helloInject() {
        return injected.helloInject();
    }

    /**
     * Say 'Hello Intercepted!' using an Interceptor.
     *
     * @return 'Hello Intercepted!'.
     */
    @GET
    @Path("/helloIntercept")
    public String helloIntercept() {
        return interceptBean.helloIntercept();
    }

    /**
     * Say 'Hello World!' in JSON format which gets generated using JSON-B.
     *
     * @return 'Hello World!' in JSON format.
     */
    @GET
    @Produces("application/json")
    @Path("/helloJsonB")
    public HelloJson helloJsonB() {
        return new HelloJson();
    }

    /**
     * Post 'Hello Json-P!' in JSON format which gets parsed using JSON-P.
     *
     * @param jsonString a JSON string.
     * @return 'Hello Json-P!' in JSON format.
     */
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/helloJsonP")
    public HelloJson helloJsonP(String jsonString) {
        HelloJson helloWorld = new HelloJson();
        try ( JsonParser parser = Json.createParser(new StringReader(jsonString));) {
            parser.next();
            String string = parser.getString();
            helloWorld.setHelloWorld(string);
        }
        return helloWorld;
    }

    /**
     * Say 'Hello World!'.
     *
     * @return 'Hello World!'.
     */
    @GET
    @Path("/helloWorld")
    public String helloWorld() {
        return "Hello World!";
    }
}
