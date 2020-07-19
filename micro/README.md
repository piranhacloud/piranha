# Piranha Micro

## Installing Piranha Micro
 
Download the JAR file from [Maven Central](https://repo1.maven.org/maven2/cloud/piranha/piranha-micro).

## Running with a WAR file

If you have a WAR file you can use the command line below:

```
java -jar piranha-micro.jar --war your_webapplication.war
```
 
## Running with an exploded WAR file

If you have an exploded directory containing the contents of your WAR file you can
use the command line below:

```
java -jar piranha-micro.jar --webapp your_webapp_directory
```

## Changing the HTTP port
 
If you want to use a different HTTP port (e.g to port 8888) you can use `--port`
as illustrated by the command line below:

```
java -jar piranha-micro.jar --war your_webapplication.war --port 8888
```

## Limitations

1. Only supports one web application per JVM.

## Where are the sources?

See GitHub at
[https://github.com/piranhacloud/piranha/tree/master/micro](https://github.com/piranhacloud/piranha/tree/master/micro)

## Additional shared documentation

1. [Adding more mime types to your web application](../shared/mime-types.md)

[Home](../overview.md)
