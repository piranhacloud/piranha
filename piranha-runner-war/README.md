
# Manorrock Piranha WAR runner

The Manorrock Piranha WAR runner module delivers you with the capability to run
your web application from the command line using a single JAR and your WAR.

## How to use?

Download the JAR file containing the WAR runner from
[Maven Central](http://repo1.maven.org/maven2/com/manorrock/piranha/piranha-runner-war/).

And then start it using the command line below

```shell
  java -jar piranha-runner-war.jar --war your_webapplication.war
```

If you want to override what temporary directory the runner uses for the web application you can specify it by add the snippet below

```shell
--webapp your_temporary_directory
```
