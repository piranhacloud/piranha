# Create a WebSocket application

If you are looking to use Piranha Servlet with WebSockets then you landed at the
right spot!

In 4 steps you will learn how to use WebSockets on Piranha Servlet. They are:

1. Create the Maven POM file
1. Add the ChatEndpoint.java file
1. Deploy the application
1. Test the application

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
    <groupId>cloud.piranha.guides.servlet</groupId>
    <artifactId>websocket</artifactId>
    <version>24.11.0-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>Piranha Servlet - WebSocket Chat application</name>
    <properties>
        <jakarta.websocket.version>2.1.0</jakarta.websocket.version>
        <java.version>17</java.version>
        <maven-compiler-plugin.version>3.11.0</maven-compiler-plugin.version>
        <maven-war-plugin.version>3.3.2</maven-war-plugin.version>
        <piranha.distribution>servlet</piranha.distribution>
        <piranha.version>23.6.0</piranha.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <dependency>
            <groupId>jakarta.websocket</groupId>
            <artifactId>jakarta.websocket-api</artifactId>
            <version>${jakarta.websocket.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>jakarta.websocket</groupId>
            <artifactId>jakarta.websocket-client-api</artifactId>
            <version>${jakarta.websocket.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
    <build>
        <finalName>websocket</finalName>
        <plugins>
            <plugin>
                <groupId>cloud.piranha.maven.plugins</groupId>
                <artifactId>piranha-maven-plugin</artifactId>
                <version>${piranha.version}</version>
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

## Add the ChatEndpoint.java file

Add the ChatEndpoint.java file in the `src/main/java/chat` directory.

```java
package chat;

import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat")
public class ChatEndpoint {

     @OnMessage
     public String onMessage(String message, Session session) {
         return message;
     }
}
```

## Deploy the application

To deploy your application you will need 2 pieces. 

1. The Piranha Servlet runtime JAR.
2. The WAR file you just produced. 

For the WAR file see the `target` directory. For the Piranha Servlet
distribution go to Maven Central. And then the following command line will
deploy your application:

```bash
  java -jar piranha-dist-servlet.jar --war-file websocket.war
```

## Test the application

To test the application connect to `ws://localhost:8080/websocket/chat` using
a WebSocket client.

## Conclusion

As you can creating a WebSocket application using Piranha Servlet is quite easy!

