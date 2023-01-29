# Running Piranha Embedded with Spring Boot

How can you integrate Piranha Embedded with Spring Boot?

In 4 steps you will learn how to so. They are:

1. Create the Maven POM file
1. Add the application class
1. Add the endpoint
1. Deploy the application

## Create the Maven POM file

Create an empty directory to store your Maven project. Inside of that directory create the ```pom.xml``` file with the content as below.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>cloud.piranha.guides.embedded</groupId>
    <artifactId>springboot</artifactId>
    <version>1-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>Running Piranha Embedded with Spring Boot</name>
    <properties>
        <piranha.version>23.1.0</piranha.version>
        <spring-boot.version>3.0.1</spring-boot.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <scope>compile</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-tomcat</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>cloud.piranha.spring</groupId>
            <artifactId>spring-boot-starter-piranha-embedded</artifactId>
            <version>${piranha.version}</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.10.1</version>
                <configuration>
                    <release>17</release>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.3.2</version>
                <configuration>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>${spring-boot.version}</version>
            </plugin>
        </plugins>
    </build>
</project>
```

## Add the application class

Add the Application class in the `src/main/java` directory, which allows you to 
configure the ServletWebServerFactory to be used.

```java
package hello;

import cloud.piranha.spring.starter.embedded.EmbeddedPiranhaServletWebServerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HelloApplication {

    @Bean
    public ServletWebServerFactory factory() {
        return new EmbeddedPiranhaServletWebServerFactory();
    }

    @Bean
    public WebServerFactoryCustomizer<EmbeddedPiranhaServletWebServerFactory> customizer() {
        return new WebServerFactoryCustomizer<EmbeddedPiranhaServletWebServerFactory>() {
            @Override
            public void customize(EmbeddedPiranhaServletWebServerFactory factory) {
                factory.setPort(8080);
            }
        };
    }

    public static void main(String[] arguments) {
        SpringApplication.run(HelloApplication.class, arguments);
    }
}
```

## Add the endpoint

And we are adding a simple 'Hello' endpoint that is listening on the `/hello` path.

```java
package hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
```

## Deploy the application

The following command line will deploy your application:

```bash
  java -jar springboot.jar
```

## Conclusion

As you can see Piranha Embedded and Spring Boot can work together.

## References

1. [Piranha Embedded](https://piranha.cloud/embedded/)

[Up](../)
