# Use an alternate HTTP server

If you want to use an alternate HTTP server you can specify it using the 
`--http` argument.

The following alternate HTTP servers are available:

1. grizzly - the Grizzly HTTP server
1. netty - the Netty HTTP server
1. singlethread - the single threaded HTTP server (deprecated).
1. undertow - the Undertow HTTP server

So if you want to use the Netty server you would use the following command-line:

```shell
java -jar piranha-micro.jar --war your_webapplication.war --http netty
```

[Back](README.md)
