package test1;

import cloud.piranha.spring.boot.starter.EmbeddedPiranhaServletWebServerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Test1Application {

    @Bean
    public ServletWebServerFactory getFactory() {
        return new EmbeddedPiranhaServletWebServerFactory();
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Test1Application.class, args);
    }
}
