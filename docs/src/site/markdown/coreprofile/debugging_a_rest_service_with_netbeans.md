# Debugging a REST service with NetBeans

If you are looking to debug a REST service with NetBeans then follow along!

In 5 steps you will learn how to start debugging the REST service. They are:

1. Create the Maven POM file
1. Add the application class
1. Add the endpoint
1. Add the NetBeans nbactions-default.xml file
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
    <artifactId>netbeans-debug</artifactId>
    <version>24.10.0-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>Piranha Core Profile - Debugging a REST service with NetBeans</name>
    <properties>
        <jakartaee.version>10.0.0</jakartaee.version>
        <java.version>17</java.version>
        <junit.version>5.10.0-M1</junit.version>
        <maven-compiler-plugin.version>3.11.0</maven-compiler-plugin.version>
        <maven-failsafe-plugin.version>3.0.0</maven-failsafe-plugin.version>
        <maven-war-plugin.version>3.3.2</maven-war-plugin.version>
        <piranha.version>23.6.0</piranha.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
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
        <finalName>netbeans-debug</finalName>
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
    <profiles>
        <profile>
            <id>debug</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>cloud.piranha.maven.plugins</groupId>
                        <artifactId>piranha-maven-plugin</artifactId>
                        <version>${piranha.version}</version>
                        <configuration>
                            <jvmArguments>-Xdebug -agentlib:jdwp=transport=dt_socket,server=n,suspend=n,address=${jpda.address}</jvmArguments>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
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

## Add the NetBeans nbactions-default.xml

To integrate debugging into the NetBeans IDE we'll create the `nbactions-default.xml` file in the root directory of your Maven project. It should contain the content as below.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<actions>
    <action>
        <actionName>debug</actionName>
        <packagings>
            <packaging>war</packaging>
            <packaging>ear</packaging>
            <packaging>ejb</packaging>
        </packagings>
        <goals>
            <goal>package</goal>
            <goal>pre-integration-test</goal>
        </goals>
        <properties>
            <netbeans.deploy.debugmode>true</netbeans.deploy.debugmode>
            <jpda.listen>true</jpda.listen>
        </properties>
        <activatedProfiles>
            <activatedProfile>debug</activatedProfile>
        </activatedProfiles>
    </action>
</actions>
```

## Debug the application

Open the `HelloWorldBean.java` file and set a breakpoint on the line where
the `Hello World` response is returned.

Then right click on your project node and select `Debug`.

After a short while you will see NetBeans change to the debugger view and the threads of your running application will appear.

Then browse to `http://localhost:8080/rest/helloworld/`.

Now you'll see it hit your breakpoint.

## Conclusion

Setting up debugging for NetBeans requires a little bit of plumbing, but once 
the Maven profile and the NetBeans nbactions-default.xml file are in place you
are set!

## References

1. [Piranha Core Profile](index.html)
1. [Piranha Maven plugin documentation](../maven-plugin/index.html)

