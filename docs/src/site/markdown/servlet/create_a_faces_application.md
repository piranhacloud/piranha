# Create a Faces application

If you are looking to use Piranha Servlet with Jakarta Faces then look no
further!

In 8 steps you will learn how to use Jakarta Faces on Piranha Servlet. They are:

1. Create the Maven POM file
1. Add the hellofaces.xhtml page
1. Add the HelloBean.java file
1. Add the web.xml file
1. Add the beans.xml file
1. Add an integration test
1. Test the application
1. Deploy the application

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
    <groupId>cloud.piranha.test.servlet</groupId>
    <artifactId>faces</artifactId>
    <version>24.11.0-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>Create a Jakarta Faces application</name>
    <properties>
        <!-- dependencies -->
        <junit.version>5.11.0</junit.version>
        <mojarra.version>4.0.7</mojarra.version>
        <weld.version>5.1.3.Final</weld.version>
        <!-- other -->
        <java.version>21</java.version>
        <piranha.distribution>servlet</piranha.distribution>
        <piranha.version>24.9.0</piranha.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <!-- plugins -->
        <build-helper-maven-plugin.version>3.6.0</build-helper-maven-plugin.version>
        <maven-compiler-plugin.version>3.13.0</maven-compiler-plugin.version>
        <maven-failsafe-plugin.version>3.5.0</maven-failsafe-plugin.version>
        <maven-war-plugin.version>3.4.0</maven-war-plugin.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.glassfish</groupId>
            <artifactId>jakarta.faces</artifactId>
            <version>${mojarra.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.jboss.weld.servlet</groupId>
            <artifactId>weld-servlet-shaded</artifactId>
            <version>${weld.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>cloud.piranha.extension</groupId>
            <artifactId>piranha-extension-redhat-weld</artifactId>
            <version>${piranha.version}</version>
            <scope>runtime</scope>
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
        <finalName>faces</finalName>
        <plugins>
            <plugin>
                <groupId>cloud.piranha.maven</groupId>
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
                    <distribution>servlet</distribution>
                    <httpPort>${httpPort}</httpPort>
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
                <configuration>
                    <forkCount>1</forkCount>
                    <systemPropertyVariables>
                        <httpPort>${httpPort}</httpPort>
                    </systemPropertyVariables>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>${maven-war-plugin.version}</version>
                <configuration>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>build-helper-maven-plugin</artifactId>
                <version>${build-helper-maven-plugin.version}</version>
                <executions>
                    <execution>
                        <id>reserve-network-port</id>
                        <goals>
                            <goal>reserve-network-port</goal>
                        </goals>
                        <phase>package</phase>
                        <configuration>
                            <portNames>
                                <portName>httpPort</portName>
                            </portNames>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

## Add the Hello Faces XHTML file

Add the helloworld.xhtml file in the `src/main/webapp` directory.

```html
<?xml version='1.0' encoding='UTF-8' ?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html lang="en"
      xmlns="http://www.w3.org/1999/xhtml"
      xmlns:f="jakarta.faces.core"
      xmlns:h="jakarta.faces.html"
      xmlns:pt="jakarta.faces.passthrough">
    <h:head>
        <title>Jakarta Faces application</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    </h:head>
    <h:body>
        <div>Jakarta Faces application</div>
        <h:form>
                <h:outputText value="#{helloBean.hello}"/>
            <br/>
        </h:form>
    </h:body>
</html>
```

## Add the HelloBean.java file

Add the HelloBean.java file in the `src/main/java/hello` directory.

```java
package hello;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named(value = "helloBean")
@RequestScoped
public class HelloBean {

    private String hello = "Hello from Jakarta Faces!";

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }
}
```

## Add the web.xml file

Add the web.xml file in the `src/main/webapp/WEB-INF` directory.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<web-app version="3.0" xmlns="http://java.sun.com/xml/ns/javaee" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd">
    <servlet>
        <servlet-name>Faces Servlet</servlet-name>
        <servlet-class>jakarta.faces.webapp.FacesServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>Faces Servlet</servlet-name>
        <url-pattern>*.xhtml</url-pattern>
    </servlet-mapping>
</web-app>
```

## Add the beans.xml file

Add the beans.xml file in the `src/main/webapp/WEB-INF` directory.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://xmlns.jcp.org/xml/ns/javaee"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/beans_2_0.xsd"
       bean-discovery-mode="all">
</beans>
```

## Add an integration test

As we want to make sure the application gets tested before we release an 
integration test is added which will be executed as part of the build.

We'll add the integration test to the `src/test/java` directory.

```java
package hello;

import java.net.URI;
import java.net.http.HttpClient;
import static java.net.http.HttpClient.Redirect.ALWAYS;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class HelloIT {
    
    private String portNumber = System.getProperty("httpPort");

    @Test
    public void testHelloFacesXhtml() throws Exception {
        HttpClient client = HttpClient
                .newBuilder()
                .connectTimeout(Duration.ofSeconds(60))
                .followRedirects(ALWAYS)
                .build();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:" + portNumber + "/faces/hellofaces.xhtml"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertTrue(response.body().contains("Hello from Jakarta Faces!"));
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

1. The Piranha Servlet runtime JAR.
2. The WAR file you just produced. 

For the WAR file see the `target` directory. For the Piranha Servlet
distribution go to Maven Central. And then the following command line will
deploy your application:

```bash
  java -jar piranha-dist-servlet.jar --war-file faces.war
```

## Conclusion

As you can integrating Jakarta Faces with Piranha Servlet can be done quickly!

