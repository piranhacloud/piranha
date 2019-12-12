
# Piranha Micro

The Piranha Micro module delivers you with the capability to run
your web application from the command line using a single JAR, with either an
exploded directory containing the contents of your WAR file, or with your WAR
file.

## How to use?

Download the JAR file.

If you have a WAR file you can use the command line below

```shell
  java -jar piranha-micro.jar --war your_webapplication.war
```

If you want to override the directory the WAR runner uses for extracting the web
application you can use the below instead

```shell
  java -jar piranha-micro.jar --war your_webapplication.war --webapp your_webapp_directory
```

If you have an exploded directory containing the contents of your WAR file you
can use the command line below

```shell
  java -jar piranha-micro.jar --webapp your_webapp_directory
```
