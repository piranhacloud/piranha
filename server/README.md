# Piranha Server

If you are looking for how to run Piranha Server on Docker, see 
[Running Piranha Server on Docker](docker.md).

## Installing Piranha Server

Download the [zip / tar.gz file](https://repo1.maven.org/maven2/cloud/piranha/piranha-server)
from Maven Central and extract it into a directory of your choice.

## Start the server

Starting the server is done by issuing the command line below from the `bin` 
directory.

```
start.sh
```

## Stop the server

Stopping the server is done by issuing the command line below from the `bin` 
directory.

```
stop.sh
```

## Deploying a web application

Deploying a web application is a simple as copying your WAR file to the `webapps`
directory. Note if the server is already running you will need to stop and start
the server.

## Where are the sources?

See GitHub at
[https://github.com/piranhacloud/piranha/tree/master/server](https://github.com/piranhacloud/piranha/tree/master/server)


## Additional shared documentation

1. [Adding more mime types to your web application](../shared/mime-types.md)

[Home](../overview.md)
