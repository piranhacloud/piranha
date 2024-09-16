# Create a Hello World web application

If you are looking to create a simple Hello World web application with Piranha
Embedded you are at the right place!

In 3 steps you will learn how to create the web application. They are:

1. Create the Maven POM file
1. Add the Hello World Servlet
1. Add the Hello World Application

## Create the Maven POM file

Create an empty directory to store your Maven project. Inside of that directory create the ```pom.xml``` file with the content as below.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>cloud.piranha.guides.embedded</groupId>
    <artifactId>helloworld</artifactId>
    <version>24.10.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>Piranha Embedded - Create a Hello World web application</name>
    <properties>
        <!-- dependencies -->
        <piranha.version>24.8.0</piranha.version>
        <!-- other -->
        <java.version>21</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <!-- plugins -->
        <exec-maven-plugin.version>3.4.1</exec-maven-plugin.version>
        <maven-compiler-plugin.version>3.13.0</maven-compiler-plugin.version>
        <maven-jar-plugin.version>3.4.2</maven-jar-plugin.version>
    </properties>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven-compiler-plugin.version}</version>
                <configuration>
                    <release>${java.version}</release>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>${exec-maven-plugin.version}</version>
                <configuration>
                    <mainClass>helloworld.HelloWorldApplication</mainClass>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>${maven-jar-plugin.version}</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>helloworld.HelloWorldApplication</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin> 
        </plugins>
    </build>
    <dependencies>
        <dependency>
            <groupId>cloud.piranha</groupId>
            <artifactId>piranha-embedded</artifactId>
            <version>${piranha.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>cloud.piranha.http</groupId>
            <artifactId>piranha-http-impl</artifactId>
            <version>${piranha.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>cloud.piranha.http</groupId>
            <artifactId>piranha-http-webapp</artifactId>
            <version>${piranha.version}</version>
            <scope>compile</scope>
        </dependency>
    </dependencies>
</project>
```

## Add the Hello World Servlet

Add the HelloWorldServlet.java file in the `src/main/webapp/helloworld` directory.

```java
package helloworld;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HelloWorldServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, ServletException {
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Hello World");
            writer.flush();
        }
    }
}
```

## Add the Hello World application

Add the HelloWorldApplication.java file in the `src/main/webapp/helloworld` directory.

```java
package helloworld;

import cloud.piranha.embedded.EmbeddedPiranha;
import cloud.piranha.embedded.EmbeddedPiranhaBuilder;
import cloud.piranha.http.impl.DefaultHttpServer;
import cloud.piranha.http.webapp.HttpWebApplicationServerProcessor;

public class HelloWorldApplication {

    public static void main(String[] arguments) throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servlet("HelloWorld", HelloWorldServlet.class)
                .servletMapping("HelloWorld", "/*")
                .buildAndStart();
        
        DefaultHttpServer server = new DefaultHttpServer(8080, 
                new HttpWebApplicationServerProcessor(piranha), false);
        
        server.start();
    }
}
```


## Conclusion

As you can see getting started with Piranha Embedded is easy!
