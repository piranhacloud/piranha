# Testing with our container image

If you are in container land this guide will show you how to use our container
image for testing.

In 5 steps you will learn how to it. They are:

1. Create the Maven POM file
1. Add the helloworld.html page
1. Add an integration test
1. Create the Dockerfile
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
    <groupId>cloud.piranha.guides.webprofile</groupId>
    <artifactId>docker</artifactId>
    <version>24.11.0-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>Testing with our container image</name>
    <properties>
        <!-- dependencies -->
        <jakartaee.version>10.0.0</jakartaee.version>
        <junit.version>5.10.0-M1</junit.version>
        <!-- plugins -->
        <docker-maven-plugin.version>0.43.4</docker-maven-plugin.version>
        <maven-compiler-plugin.version>3.11.0</maven-compiler-plugin.version>
        <maven-failsafe-plugin.version>3.0.0</maven-failsafe-plugin.version>
        <maven-war-plugin.version>3.3.2</maven-war-plugin.version>
        <!-- other -->
        <java.version>21</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <dependency>
            <groupId>jakarta.platform</groupId>
            <artifactId>jakarta.jakartaee-web-api</artifactId>
            <version>${jakartaee.version}</version>
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
        <finalName>image</finalName>
        <plugins>
            <plugin>
                <groupId>io.fabric8</groupId>
                <artifactId>docker-maven-plugin</artifactId>
                <version>${docker-maven-plugin.version}</version>
                <configuration>
                    <images>
                        <image>
                            <alias>image</alias>
                            <build>
                                <buildx>
                                    <platforms>
                                        <platform>linux/amd64</platform>
                                        <platform>linux/arm64</platform>
                                    </platforms>
                                </buildx>
                                <contextDir>${basedir}</contextDir>
                                <dockerFile>src/main/docker/Dockerfile</dockerFile>
                            </build>
                            <run> 
                                <ports> 
                                    <port>8080:8080</port>
                                </ports>
                                <wait> 
                                    <http>
                                        <url>http://localhost:8080/helloworld.html</url>
                                    </http>
                                    <time>20000</time>
                                </wait>
                            </run>
                        </image>
                    </images>
                </configuration>
                <executions>
                    <execution>
                        <id>start</id>
                        <phase>pre-integration-test</phase>
                        <goals>
                            <goal>build</goal>
                            <goal>start</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>stop</id>
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
                <version>${maven-compiler-plugin.version}</version>
                <configuration>
                    <release>${java.version}</release>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>${maven-failsafe-plugin.version}</version>
                <executions>
                    <execution>
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
                <version>${maven-war-plugin.version}</version>
                <configuration>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

## Add the Hello World HTML file

Add the helloworld.html file in the `src/main/webapp` directory.

```html
<!DOCTYPE html>

<html>
    <head>
        <title>Hello World</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <div>Hello World!</div>
    </body>
</html>
```

## Add an integration test

As we want to make sure the application gets tested before we release an 
integration test is added which will be executed as part of the build.

We'll add the integration test to the `src/test/java` directory.

```java
package helloworld;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class HelloWorldIT {
 
    @Test
    void testHelloWorldHtml() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:8080/helloworld.html"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertTrue(response.body().contains("Hello World!"));
    }
}
```

## Create the Dockerfile

Create the `Dockerfile` in the `src/main/docker` directory with the following
content:

```dockerfile
FROM ghcr.io/piranhacloud/webprofile:23.12.0
USER piranha
COPY target/image.war /home/piranha/ROOT.war
CMD ["java", "-jar", "piranha-dist-webprofile.jar", "--war-file", "ROOT.war"]
```

## Test the application

The application is setup to use the Docker Maven plugin to do integration 
testing so when you are building the application it will also execute an
integration test validating the web application works.

To build and test the application execute the following command:

```bash
  mvn install
```

## Conclusion

As you can see getting testing with our container image is easy!

