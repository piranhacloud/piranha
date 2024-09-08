# Create a Piranha Embedded JLink application

Can you create a JLink application with Piranha Embedded?

In 6 steps you will learn how to so. They are:

1. Create the Maven POM file
1. Add the application class
1. Add the servlet class
1. Add the module-info
1. Build the application
1. Run the application

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
    <artifactId>jlink</artifactId>
    <version>24.3.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>Piranha Embedded - JLink application</name>
    <properties>
        <exec-maven-plugin.version>3.2.0</exec-maven-plugin.version>
        <java.version>21</java.version>
        <jrelease-maven-plugin.version>1.10.0</jrelease-maven-plugin.version>
        <maven-compiler-plugin.version>3.12.1</maven-compiler-plugin.version>
        <maven-dependency-plugin.version>3.6.1</maven-dependency-plugin.version>
        <maven-jar-plugin.version>3.3.0</maven-jar-plugin.version>
        <piranha.version>24.2.0</piranha.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <main.class>helloworld.HelloWorldApplication</main.class>
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
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <version>${maven-dependency-plugin.version}</version>
                <executions>
                    <execution>
                        <id>copy-dependencies</id>
                        <phase>prepare-package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${project.build.directory}/modules</outputDirectory>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>${exec-maven-plugin.version}</version>
                <configuration>
                    <mainClass>${main.class}</mainClass>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>${maven-jar-plugin.version}</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>${main.class}</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.jreleaser</groupId>
                <artifactId>jreleaser-maven-plugin</artifactId>
                <version>${jrelease-maven-plugin.version}</version>
                <configuration>
                    <gitRootSearch>true</gitRootSearch>
                    <jreleaser>
                        <project>
                            <description>${project.name}</description>
                            <copyright>Manorrock.com</copyright>
                        </project>
                        <assemble>
                            <jlink>
                                <helloworld>
                                    <active>ALWAYS</active>
                                    <imageName>{{distributionName}}-{{projectVersion}}</imageName>
                                    <java>
                                        <mainModule>helloworld</mainModule>
                                        <mainClass>${main.class}</mainClass>
                                    </java>
                                    <copyJars>true</copyJars>
                                    <mainJar>
                                        <path>${project.build.directory}/${project.artifactId}-${project.version}.jar</path>
                                    </mainJar>
                                    <jars>
                                        <jar>
                                            <pattern>${project.build.directory}/modules/*.jar</pattern>
                                        </jar>
                                    </jars>
                                </helloworld>
                            </jlink>
                        </assemble>
                    </jreleaser>
                </configuration>
                <executions>
                    <execution>
                        <id>assemble</id>
                        <phase>verify</phase>
                        <goals>
                            <goal>assemble</goal>
                        </goals>
                    </execution>
                </executions>
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

## Add the module info

Now add the module-info.java to the `src/main/java` directory.

```
module helloworld {

    exports helloworld;
    requires cloud.piranha.embedded;
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

## Run the application

In the previous step you created the JLink custom image. To execute the 
application go to the `target/jreleaser` directory and look for the `bin` 
directory and execute the `helloworld` wrapper in it. And then point your
browser to `http://localhost:8080` to see it in action.

## Conclusion

As you can see creating a JLink runtime image with Piranha Embedded is a few
easy steps away!

## References

1. [Piranha Embedded](index.html)
1. [JReleaser](https://jreleaser.org/)

