# Piranha Servlet

Piranha Servlet is our distribution that delivers you with the equivalent of 
what Tomcat and Jetty deliver to you packaged up as a single runnable JAR. If
you are looking to run a single web application AND you want make an jump from
Tomcat or Jetty to Piranha, then try out this distribution!

The following components are available in the Piranha Servlet distribution:

* Jakarta Authentication
* Jakarta Expression Language
* Jakarta Pages
* Jakarta Servlet
* Jakarta WebSocket

## Download Piranha Servlet

Piranha Servlet is available as a JAR file from Maven Central.

The download link below will take you to the location on Maven Central where you
can download any version of Piranha Servlet Select the version by clicking
on the directory link and then download the JAR file from there.

<a href="https://repo1.maven.org/maven2/cloud/piranha/dist/piranha-dist-servlet/">Download</a>

Note the name of the JAR file you want to download should look like
piranha-dist-servlet-X.Y.Z.jar    

## Quickstart

Assuming you have downloaded the JAR file as previously indicated running 
Piranha Servlet is as simple as:

```
java -jar piranha-dist-servlet-X.Y.Z.jar --war-file your-webapplication.war
```

## Running using Docker

If you want to run Piranha Servlet using Docker we have a base image
available for you using Eclipse Temurin as the underlying runtime.

See https://github.com/piranhacloud/piranha/pkgs/container/servlet for
more information and how to pull that image.

To use this as a base image you would create your own Dockerfile and add your 
WAR file using a COPY and change the CMD to point to your WAR file as
illustrated below:

```Dockerfile
FROM ghcr.io/piranhacloud/servlet:latest
COPY target/my.war my.war
USER root
RUN chown -R piranha:piranha /home/piranha
USER piranha
CMD ["java", "-jar", "piranha-dist-servlet.jar", "--war-file", "my.war"]
```

## Guides

1. [Create a Hello World web application](create_a_hello_world_web_application.html)
1. [Create a Jakarta Pages application](create_a_jakarta_pages_application.html)
1. [Create a WebSocket application](create_a_websocket_application.html)
1. [Run a web application on CRaC](run_a_web_application_on_crac.html)
