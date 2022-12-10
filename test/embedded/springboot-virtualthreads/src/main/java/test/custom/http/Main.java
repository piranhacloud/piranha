package test.custom.http;

import cloud.piranha.http.virtual.VirtualHttpServer;
import cloud.piranha.spring.starter.embedded.EmbeddedPiranhaServletWebServerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

    /**
     * Create the Piranha Embedded ServletWebServerFactory.
     *
     * @return the factory.
     */
    @Bean
    public ServletWebServerFactory factory() {
        return new EmbeddedPiranhaServletWebServerFactory();
    }

    /**
     * Customize the Piranha Embedded integration.
     *
     * @return the customizer.
     */
    @Bean
    public WebServerFactoryCustomizer<EmbeddedPiranhaServletWebServerFactory> customizer() {
        return factory -> factory.setHttpServer(new VirtualHttpServer());
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
