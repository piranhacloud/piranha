# Piranha Core Profile

Piranha Core Profile is our distribution that delivers Jakarta EE Core Profile
support. If you are looking to implement micro services then consider this 
distribution!

The following components are available in the Piranha Core Profile distribution:

* Jakarta Annotations
* Jakarta Contexts and Dependency Injection (CDI Lite section)
* Jakarta Dependency Injection
* Jakarta Interceptors
* Jakarta JSON Processing
* Jakarta JSON Binding
* Jakarta RESTful Web Services

## Download Piranha Core Profile

Piranha Core Profile is available as a JAR file from Maven Central.

The download link below will take you to the location on Maven Central where you
can download any version of Piranha Core Profle. Select the version by clicking
on the directory link and then download the JAR file from there.

<a href="https://repo1.maven.org/maven2/cloud/piranha/dist/piranha-dist-coreprofile/">Download</a>

Note the name of the JAR file you want to download should look like
piranha-dist-coreprofile-X.Y.Z.jar    

## Quickstart

Assuming you have downloaded the JAR file as previously indicated running 
Piranha Core Profile is as simple as:

```
java -jar piranha-dist-coreprofile-X.Y.Z.jar --war-file your-webapplication.war
```

## Running using Docker

If you want to run Piranha Core Profile using Docker we have a base image
available for you using Eclipse Temurin as the underlying runtime.

See https://github.com/piranhacloud/piranha/pkgs/container/coreprofile for
more information and how to pull that image.

To use this as a base image you would create your own Dockerfile and add your 
WAR file using a COPY and change the CMD to point to your WAR file as
illustrated below:

```Dockerfile
FROM ghcr.io/piranhacloud/coreprofile:latest
COPY target/my.war my.war
USER root
RUN chown -R piranha:piranha /home/piranha
USER piranha
CMD ["java", "-jar", "piranha-dist-coreprofile.jar", "--war-file", "my.war"]
```

## Guides

1. [Create a REST service](create_a_rest_service.html)
1. [Create a JSON REST service](create_a_json_rest_service.html)
1. [Debugging a REST service with NetBeans](debugging_a_rest_service_with_netbeans.html)
1. [Debugging a REST service with VSCode](debugging_a_rest_service_with_vscode.html)
1. [Testing with JUnit 5 and Arquillian](testing_with_junit5_and_arquillian.html)
1. [Using Project CRaC](using_project_crac.html)

