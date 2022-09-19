# Debugging a REST service with VSCode

If you are looking to debug a REST service with VSCode then follow along!

In 6 steps you will learn how to start debugging the REST service. They are:

1. Create the Maven POM file
1. Add the application class
1. Add the endpoint
1. Add the VSCode tasks.json file
1. Add the VSCode launch.json file
1. Debug the application

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
    <groupId>cloud.piranha.guides.coreprofile</groupId>
    <artifactId>rest</artifactId>
    <version>1-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>REST service</name>
    <properties>
        <jakarta.jakartaee-core-api.version>10.0.0</jakarta.jakartaee-core-api.version>
        <junit.version>5.9.0</junit.version>
        <piranha-maven-plugin.version>22.9.0</piranha-maven-plugin.version>
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
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>3.0.0-M7</version>
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
                <version>3.3.2</version>
                <configuration>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>
        </plugins>
    </build>
    <profiles>
        <profile>
            <id>debug</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>cloud.piranha.maven.plugins</groupId>
                        <artifactId>piranha-maven-plugin</artifactId>
                        <version>${piranha-maven-plugin.version}</version>
                        <configuration>
                            <jvmArguments>-Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=9009</jvmArguments>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
    <repositories>
        <repository>
            <id>jakarta-staging</id>
            <url>https://jakarta.oss.sonatype.org/content/repositories/staging/</url>
        </repository>
    </repositories>
</project>
```

Note the POM file contains a `debug` profile in the profiles section that
we will leverage later on to activate the NetBeans debugging.

## Add the application class

Add the Application class in the `src/main/java` directory, which allows you to
set the application path using the @ApplicationPath annotation.

```java
package rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("")
public class RestApplication extends Application {
}
```

## Add the endpoint

And we are adding a simple 'Hello World' endpoint that is listening on the 
`/helloworld` path.

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

## Add the VSCode tasks.json file

To integrate debugging into VSCode we'll need a tasks.json file that will
trigger the proper Maven goals BEFORE we start the debugger. For that 
lets first create the `.vscode` directory in the root directory of your
Maven project (if it does not exist). And then we will create the 
`tasks.json` file in the `.vscode` directory. It should contain the content
as below.

```json
{
    "version": "2.0.0",
    "tasks": [
        {
            "label": "Start",
            "type": "shell",
            "command": "mvn -Pdebug package pre-integration-test",
            "isBackground": true,
            "problemMatcher": "$tsc-watch",
        }
    ]
}
```

## Add the VSCode launch.json file

The next step is to configure the `launch.json` file to allow it to
attach to the REST service after it gets started by means of the 
`Start` entry in the `tasks.json` file.

For that we will create the `tasks.json` file in the `.vscode` directory
with the content as below.

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Debug",
            "preLaunchTask": "Start",
            "request": "attach",
            "hostName": "localhost",
            "port": "9009"
        }
    ]
}
```

## Debug the application

Open the `HelloWorldBean.java` file and set a breakpoint on the line where
the `Hello World` response is returned.

Then on your left bar click on the `Debug` icon.

And make sure that `Debug` is selected and click the run icon to the left of it.

Then browse to `http://localhost:8080/rest/helloworld/`.

Now you'll see it hit your breakpoint.

## Conclusion

Setting up debugging for VSCode requires a little bit of plumbing, but once 
the Maven profile, the VSCode `tasks.json` and `launch.json` files are in 
place it all comes together nicely!

## References

1. [Piranha Core Profile](https://piranha.cloud/core-profile/)
1. [Piranha Maven plugin documentation](https://piranha.cloud/maven/piranha-maven-plugin/plugin-info.html)
1. [ZIP file containing sources](netbeans-debug.zip)

[Back](../)
