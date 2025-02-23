package cloud.piranha.docker.coreprofile;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoreProfileIT {

    /**
     * Test to verify that the Docker container for Piranha Core Profile starts correctly,
     * maps port 8080, and responds with a status code 200 when accessed via HTTP.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testBasicFunctionality() throws Exception {
        try (GenericContainer<?> container = new GenericContainer<>(
            DockerImageName.parse("ghcr.io/piranhacloud/coreprofile:latest"))
                .withExposedPorts(8080)) {
            
            container.start();

            Integer mappedPort = container.getMappedPort(8080);
            assertTrue(mappedPort != null && mappedPort > 0, "Port 8080 should be mapped");

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + mappedPort))
                    .build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            assertEquals(200, response.statusCode(), "Response code should be 200");
        }
    }
}
