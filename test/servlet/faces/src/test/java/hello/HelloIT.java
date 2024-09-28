package hello;

import java.net.URI;
import java.net.http.HttpClient;
import static java.net.http.HttpClient.Redirect.ALWAYS;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class HelloIT {
    
    private String portNumber = System.getProperty("httpPort");

    @Test
    public void testHelloFacesXhtml() throws Exception {
        HttpClient client = HttpClient
                .newBuilder()
                .connectTimeout(Duration.ofSeconds(60))
                .followRedirects(ALWAYS)
                .build();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:" + portNumber + "/faces/hellofaces.xhtml"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertTrue(response.body().contains("Hello from Jakarta Faces!"));
    }
}
