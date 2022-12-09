package test1;

import cloud.piranha.spring.starter.embedded.EmbeddedPiranhaServletWebServerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Test1Application {

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
        return new WebServerFactoryCustomizer<EmbeddedPiranhaServletWebServerFactory>() {
            @Override
            public void customize(EmbeddedPiranhaServletWebServerFactory factory) {
                factory.setPort(8080);
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Test1Application.class, args);
    }
}
