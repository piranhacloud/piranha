/*
 * Copyright (c) 2002-2025 Manorrock.com. All Rights Reserved.
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
package integration;

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
 * The Integration bean.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("")
@RequestScoped
public class IntegrationBean {

    /**
     * Stores the DependencyInjectionBean.
     */
    @Inject
    private DependencyInjectionBean dependencyInjection;

    /**
     * Stores the intercept bean.
     */
    @Inject
    private InterceptBean interceptBean;

    /**
     * Validate the correct string is returned using the bean injected using the
     * Inject annotation.
     *
     * @return 'Dependency Injection works!'.
     */
    @GET
    @Path("/dependencyInjection")
    public String dependencyInjection() {
        return dependencyInjection.dependencyInjection();
    }

    /**
     * Validate the correct string is returned using an interceptor.
     *
     * @return 'Interceptor work!'.
     */
    @GET
    @Path("/intercept")
    public String intercept() {
        return interceptBean.intercept();
    }

    /**
     * Validate JSON Binding works.
     *
     * @return 'JSON Binding works!' in JSON format.
     */
    @GET
    @Produces("application/json")
    @Path("/jsonb")
    public Jsonb jsonb() {
        return new Jsonb();
    }

    /**
     * Validate JSON Processing works.
     *
     * @param jsonString a JSON string.
     * @return 'JSON processing works!' in JSON format.
     */
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/jsonp")
    public Jsonb jsonp(String jsonString) {
        Jsonb jsonb = new Jsonb();
        try ( JsonParser parser = Json.createParser(new StringReader(jsonString));) {
            parser.next();
            String string = parser.getString();
            jsonb.setString(string);
        }
        return jsonb;
    }

    /**
     * Say 'REST works!'.
     *
     * @return 'Hello World!'.
     */
    @GET
    @Path("/rest")
    public String rest() {
        return "REST works!";
    }
}
