# Testing with JUnit 5 and Arquillian 

If you are looking to test with JUnit 5 and Arqullien then this guide is for you!

In 5 steps you will learn how to do it. 

They are:

1. Create the Maven POM file
1. Add the application class
1. Add the endpoint
1. Add an integration test
1. Test the application

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
    <artifactId>arquillian</artifactId>
    <version>1-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>Testing with JUnit 5 and Arquillian</name>
    <properties>
        <arquillian.version>1.7.0.Alpha12</arquillian.version>
        <jakarta.jakartaee-core-api.version>10.0.0</jakarta.jakartaee-core-api.version>
        <junit.version>5.9.0</junit.version>
        <piranha.version>22.9.0</piranha.version>
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
            <groupId>cloud.piranha.arquillian</groupId>
            <artifactId>piranha-arquillian-jarcontainer</artifactId>
            <version>${piranha.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.jboss.arquillian.junit5</groupId>
            <artifactId>arquillian-junit5-container</artifactId>
            <version>${arquillian.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
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
        <finalName>arquillian</finalName>
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
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>3.0.0-M7</version>
                <executions>
                    <execution>
                        <id>integration-test</id>
                        <phase>integration-test</phase>
                        <goals>
                            <goal>integration-test</goal>
                            <goal>verify</goal>
                        </goals>
                    </execution>
                </executions>
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
package arquillian;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("")
public class HelloArquillianApplication extends Application {
}
```

## Add the endpoint

And we are adding a simple 'Hello World' endpoint that is listening on the `/helloworld` path.

```java
package arquillian;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/helloarquillian")
@RequestScoped
public class HelloArquillianBean {

    @GET
    public String helloArquillian() {
        return "Hello Arquillian!";
    }
}
```

## Add an integration test

As we want to make sure the application gets tested before we release an 
integration test is added which will be executed as part of the build.

We'll add the integration test to the `src/test/java` directory.

```java
package helloarquillian;

import arquillian.HelloArquillianApplication;
import arquillian.HelloArquillianBean;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ArquillianExtension.class)
public class HelloArquillianIT {

    @ArquillianResource
    private URL baseUrl;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return create(WebArchive.class)
                .addClass(HelloArquillianApplication.class)
                .addClass(HelloArquillianBean.class);
    }

    @Test
    @RunAsClient
    public void testHelloArquillian() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(new URI(baseUrl + "helloarquillian"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertTrue(response.body().contains("Hello Arquillian!"));
    }
}
```

## Test the application

The application is now setup to use JUnit 5 and Arquillian to do the integration 
testing. So when you are building the application it will also execute an 
integration test validating the endpoint works.

To build and test the application execute the following command:

```bash
  mvn install
```

## Conclusion

As you can see using JUnit 5 and Arquillian is pretty straightforward!

## References

1. [Piranha Core Profile](https://piranha.cloud/core-profile/)
1. [Arquillian](https://arquillian.org/)
1. [ZIP file containing sources](arquillian.zip)

[Back](../)
