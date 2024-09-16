# Create a Piranha Embedded GraalVM application

Can you create a GraalVM application with Piranha Embedded?

In 6 steps you will learn how to so. They are:

1. Create the Maven POM file
1. Add the application class
1. Add the servlet class
1. Add the module-info
1. Build the GraalVM binary
1. Run the GraalVM binary

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
    <artifactId>graalvm</artifactId>
    <version>24.10.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>Piranha Embedded - Create a GraalVM application</name>
    <properties>
        <exec-maven-plugin.version>3.1.0</exec-maven-plugin.version>
        <java.version>17</java.version>
        <maven-compiler-plugin.version>3.11.0</maven-compiler-plugin.version>
        <maven-jar-plugin.version>3.3.0</maven-jar-plugin.version>
        <piranha.version>23.6.0</piranha.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <build>
        <finalName>helloworld</finalName>
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
                    <mainClass>helloworld.HelloWorld</mainClass>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>${maven-jar-plugin.version}</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>helloworld.HelloWorld</mainClass>
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
    <profiles>
        <profile>
            <id>graalvm</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.graalvm.nativeimage</groupId>
                        <artifactId>native-image-maven-plugin</artifactId>
                        <version>21.2.0</version>
                        <executions>
                            <execution>
                                <goals>
                                    <goal>native-image</goal>
                                </goals>
                                <phase>package</phase>
                            </execution>
                        </executions>
                        <configuration>
                            <skip>false</skip>
                            <imageName>helloworld</imageName>
                            <buildArgs>-H:Log=registerResource:5 -H:IncludeResourceBundles=jakarta.servlet.LocalStrings -H:IncludeResourceBundles=jakarta.servlet.http.LocalStrings --no-fallback --install-exit-handlers</buildArgs>
                        </configuration>
                    </plugin>                    
                </plugins>
            </build>
        </profile>
    </profiles>
</project>
```

## Add the application class

Add the HelloWorldApplication class in the `src/main/java` directory, which 
allows you to configure the EmbeddedPiranha instance as well as the
DefaultHttpServer instance.

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

## Add the servlet

And we are adding a simple HelloWorld servlet.

```java
package helloworld;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HelloWorldServlet extends HttpServlet {
    
    /**
     * Constructor.
     * 
     * <p>
     *  Note the constructor is needed here to make it so we can generate the
     *  GraalVM binary. See META-INF/native-image for more information.
     * </p>
     */
    public HelloWorldServlet() {
    }
    
    @Override
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, ServletException {
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Hello World!");
            writer.flush();
        }
    }
}
```

## Add the module info

Now add the module-info.java to the `src/main/java` directory.

```
module helloworld {

    exports helloworld;
    requires cloud.piranha.http.api;
    requires cloud.piranha.http.impl;
    requires cloud.piranha.http.webapp;
    requires jakarta.servlet;
}
```

## Build the application

The following command line will build your application:

```bash
  mvn verify
```

## Build the GraalVM binary

The following command line will use Docker to build the GraalVM binary:

```bash
  docker build -t graalvm -f Dockerfile .
```

## Execute the GraalVM binary

The following command will use the generated Docker image to execute your
GraalVM binary.

```bash
  docker run --rm -it -p 8080:8080 graalvm
```

## Conclusion

As you can see creating a GraalVM application with Piranha Embedded is a few
easy steps away!

## References

1. [Piranha Embedded](index.html)
