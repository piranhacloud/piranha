# Running Piranha Server on Docker

## Starting Piranha Server on port 8080

By default Piranha Server listens on port 8080. The command line below exposes
port 8080 on your local machine and binds it to port 8080 inside the Docker 
container.

```
docker run --rm -it -p 8080:8080 piranhacloud/server:VERSION 
```

Where VERSION is the version you want to use. Note if you want to test out the
new and upcoming features you can use `snapshot`.

## Mounting your own webapps directory

If you do not want to create your own Docker image containing Piranha Server and
your web applications you can bind a volume to the Piranha webapps directory like
illustrated below. 

```
docker run --rm -it -p 8080:8080 -v $PWD:/usr/local/piranha/webapps \
  piranhacloud/server:VERSION
```

Note this will disable all web applications that are shipped
with Piranha Server by default as you are replacing it with your own set of 
web applications.

[Home](../)
