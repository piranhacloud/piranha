# Create a Hello World web application

If you are looking to create a simple Hello World web application with Piranha
Server to get started then look no further!

In 5 steps you will learn how to create the web application. They are:

1. Create the Maven POM file
1. Add the helloworld.html page
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
    <groupId>cloud.piranha.guides.server</groupId>
    <artifactId>helloworld</artifactId>
    <version>1-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>Piranha Server - HelloWorld web application</name>
    <properties>
        <java.version>17</java.version>
        <junit.version>5.10.0-M1</junit.version>
        <maven-compiler-plugin.version>3.11.0</maven-compiler-plugin.version>
        <maven-failsafe-plugin.version>3.0.0</maven-failsafe-plugin.version>
        <maven-war-plugin.version>3.3.2</maven-war-plugin.version>
        <piranha.distribution>server</piranha.distribution>
        <piranha.version>23.6.0</piranha.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
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
        <finalName>helloworld</finalName>
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
                <configuration>
                    <distribution>${piranha.distribution}</distribution>
                </configuration>
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
                .newBuilder(new URI("http://localhost:8080/helloworld/helloworld.html"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertTrue(response.body().contains("Hello World!"));
    }
}
```

## Test the application

The application is setup to use JUnit to do integration testing using the 
Piranha Maven plugin so when you are building the application it will also 
execute an integration test validating the web application works.

To build and test the application execute the following command:

```bash
  mvn install
```

## Deploy the application

To deploy your application you will need 2 pieces. 

1. The Piranha Server distribution zip.
2. The WAR file you just produced. 

For the Piranha Server distribution zip go to Maven Central. 

For the WAR file see the `target` directory. 

Once you have downloaded the Piranha Server distribution zip extract it into
an empty directory and then copy the WAR file in the `webapps` directory.

From the `bin` directory of Piranha Server start the server using the command line below:

```bash
  ./start.sh
```

## Conclusion

As you can see getting started with Piranha Server is straightforward!

