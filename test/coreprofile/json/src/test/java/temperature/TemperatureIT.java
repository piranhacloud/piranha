package temperature;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class TemperatureIT {
    
    private String httpPort = System.getProperty("httpPort");
 
    @Test
    void testCelsius() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:" + httpPort + "/temperature/celsius/18.5"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertEquals("{\"scale\":\"CELSIUS\",\"temperature\":18.5}", response.body());
    }
 
    @Test
    void testFahrenheit() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:" + httpPort + "/temperature/fahrenheit/68.0"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertEquals("{\"scale\":\"FAHRENHEIT\",\"temperature\":68.0}", response.body());
    }
}
