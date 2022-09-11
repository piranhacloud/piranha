# Create a REST service

If you are looking to create a REST service with Piranha then consider Piranha Core Profile. It features a runtime ideally suited for REST and micro services.

In 6 steps you will learn how to create the REST service. They are:

1. Create the Maven POM file
1. Add the application class
1. Add the endpoint
1. Add an integration test
1. Test the application
1. Deploy the application

## Create the Maven POM file

Create an empty directory to store your Maven project. Inside of that directory create the ```pom.xml``` file with the content as below.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project
    xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>cloud.piranha.guides.coreprofile</groupId>
    <artifactId>rest</artifactId>
    <version>1-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>REST service</name>
    <properties>
        <jakarta.jakartaee-core-api.version>10.0.0</jakarta.jakartaee-core-api.version>
        <junit.version>5.9.0</junit.version>
        <piranha-maven-plugin.version>22.9.0-SNAPSHOT</piranha-maven-plugin.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <dependency>
            <groupId>jakarta.platform</groupId>
            <artifactId>jakarta.jakartaee-core-api</artifactId>
            <version>${jakarta.jakartaee-core-api.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-params</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <build>
        <finalName>rest</finalName>
        <plugins>
            <plugin>
                <groupId>cloud.piranha.maven.plugins</groupId>
                <artifactId>piranha-maven-plugin</artifactId>
                <version>${piranha-maven-plugin.version}</version>
                <executions>
                    <execution>
                        <id>pre-integration-test</id>
                        <phase>pre-integration-test</phase>
                        <goals>
                            <goal>start</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>post-integration-test</id>
                        <phase>post-integration-test</phase>
                        <goals>
                            <goal>stop</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
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
        </plugins>
    </build>
    <repositories>
        <repository>
            <id>jakarta-staging</id>
            <url>https://jakarta.oss.sonatype.org/content/repositories/staging/</url>
        </repository>
    </repositories>
</project>
```

## Add the application class

Add the Application class in the `src/main/java` directory, which allows you to set the application path using the @ApplicationPath annotation.

```java
package rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("")
public class RestApplication extends Application {
}
```

## Add the endpoint

And we are adding a simple 'Hello World' endpoint that is listening on the `/helloworld` path.

```java
package rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/helloworld")
@RequestScoped
public class HelloWorldBean {

    @GET
    public String helloWorld() {
        return "Hello World!";
    }
}
```

## Add an integration test

As we want to make sure the application gets tested before we release an integration test is added which will be executed as part of the build.

We'll add the integration test to the `src/test/java` directory.

```java
package rest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class HelloWorldIT {
 
    @Test
    void testHelloWorld() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/rest/helloworld"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertTrue(response.body().contains("Hello World!"));
    }
}

```

## Test the application

The application is setup to use JUnit to do integration testing using the Piranha Maven plugin so when you are building the application it will also execute an integration test validating the endpoint works.

To build and test the application execute the following command:

```bash
  mvn install
```

## Deploy the application

To deploy your application you will need 2 pieces. 

1. The Piranha Core Profile runtime JAR.
2. The WAR file you just produced. 

For the WAR file see the `target` directory. For the Piranha Core Profile distribution go to Maven Central. And then the following command line will deploy your application:

```bash
  java -jar piranha-dist-coreprofile.jar --war-file rest.war
```

## Conclusion

As you can see getting started with Piranha Core Profile does not have to take long.

## References

1. [Piranha Core Profile](https://piranha.cloud/core-profile/)
1. [Piranha Maven plugin documentation](https://piranha.cloud/maven/piranha-maven-plugin/plugin-info.html)
1. [ZIP file containing sources](rest.zip)

[Back](../)
