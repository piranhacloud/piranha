# Using Project CRaC

If you are looking to get CRaC-ing with Piranha Servlet you can follow along!

_Make sure you are using a CRaC enabled JDK available from 
https://github.com/CRaC/openjdk-builds/releases or from any vendor that delivers
a CRaC capable OpenJDK distribution._

In 8 steps you will learn how to deploy CRaC with Piranha Servlet. They are:

1. Create the Maven POM file
1. Add the application class
1. Add the endpoint
1. Add an integration test
1. Test the application
1. Start the application
1. Create the checkpoint
1. Restart the application

## Create the Maven POM file

Create an empty directory to store your Maven project. Inside of that directory 
create the ```pom.xml``` file with the content as below.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project
    xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>cloud.piranha.guides.crac</groupId>
    <artifactId>crac</artifactId>
    <version>24.11.0-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>Piranha Core Profile on Project CRaC</name>
    <properties>
        <!-- dependencies -->
        <jakartaee.version>10.0.0</jakartaee.version>
        <junit.version>5.11.0-M2</junit.version>
        <piranha.version>24.7.0</piranha.version>
        <!-- other -->
        <java.version>21</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <!-- plugins -->
        <maven-compiler-plugin.version>3.13.0</maven-compiler-plugin.version>
        <maven-failsafe-plugin.version>3.3.1</maven-failsafe-plugin.version>
        <maven-war-plugin.version>3.4.0</maven-war-plugin.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>jakarta.platform</groupId>
            <artifactId>jakarta.jakartaee-core-api</artifactId>
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
        <finalName>crac</finalName>
        <plugins>
            <plugin>
                <groupId>cloud.piranha.maven.plugins</groupId>
                <artifactId>piranha-maven-plugin</artifactId>
                <version>${piranha.version}</version>
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

As we want to make sure the application gets tested before we release an 
integration test is added which will be executed as part of the build.

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
                .newBuilder(new URI("http://localhost:8080/crac/helloworld"))
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

For the WAR file see the `target` directory. For the Piranha Core Profile
distribution go to Maven Central. And then the following command line will
deploy your application:

```bash
  java -XX:CRaCCheckpointTo=cr -jar piranha-dist-coreprofile.jar --enable-crac --write-pid --war-file crac.war &
```

## Create the checkpoint

You have your application currently running so now it is time to create a
checkpoint.

To ask the JVM to create a checkpoint use the command line below:

```bash
  jcmd piranha-dist-coreprofile.jar JDK.checkpoint
```

The application will exit and the chechkpoint files are in the `cr` directory.

## Restart the application

To restart the application for the checkpoint you can use the following command
line:

```bash
  java -XX:CRaCRestoreFrom=cr
```

## Conclusion

As you can see using CRaC with Piranha Core Profile is matter of the right JDK
and the right command line switch.

## References

1. [CRaC Project](https://wiki.openjdk.org/display/crac/Main)
