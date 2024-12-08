package cloud.piranha.test.coreprofile.no_servlet_class;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class EchoIT {
    
    private String httpPort = System.getProperty("httpPort");
 
    @Test
    void testEcho() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:" + httpPort + "/piranha-test-coreprofile-no_servlet_class/echo"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertEquals("echo", response.body());
    }
}
