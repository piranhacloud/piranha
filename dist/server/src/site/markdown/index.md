# Piranha Server

Piranha Server is our distribution that delivers you the equivalent of what 
Tomcat and Jetty deliver to you. If you are looking to make an easy jump from
Tomcat or Jetty then try out this distribution!

The following components are available in the Piranha Server distribution:

* Jakarta Authentication
* Jakarta Expression Language
* Jakarta Pages
* Jakarta Servlet
* Jakarta WebSocket

## Download Piranha Server

Piranha Server is available as a zip or tar.gz file from Maven Central.

The download link below will take you to the location on Maven Central where you
can download any version of Piranha Server. Select the version by clicking
on the directory link and then download the zip or tar.gz file from there.

<a href="https://repo1.maven.org/maven2/cloud/piranha/dist/piranha-dist-server/">Download</a>

Note the name of the JAR file you want to download should look like
piranha-dist-server-X.Y.Z.zip or piranha-dist-server-X.Y.Z.tar.gz 

## Starting the server

Assuming you have downloaded the zip or tar.gz file as previously indicated you
will need to unzip it into a directory of your choice. Then by going to the 
`bin` directory you can run Piranha Server by issuing the following command 
line:

```
./start.sh
```

On Windows use `start.cmd` instead.

## Stopping the server

To stop the server use:

```
./stop.sh
```

On Windows use `stop.cmd` instead.

## Deploying your application

Deploying your web application is as simple as copying your WAR file into the 
`webapps` directory of the server and then stopping and starting the server.

## Running using Docker

If you want to run Piranha Server using Docker we have a base image
available for you using Eclipse Temurin as the Java runtime.

See https://github.com/orgs/piranhacloud/packages/container/package/server for
more information and how to pull that image.

To use this as a base image you would create your own Dockerfile and add your 
WAR file using a COPY as illustrated below:

```Dockerfile
FROM ghcr.io/piranhacloud/server:latest
COPY target/my.war /home/piranha/piranha/webapps/my.war
```
## Documentation

1. [Create a Hello World web application](create_a_hello_world_web_application.html)
