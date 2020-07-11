# Piranha Embedded

This distribution of Piranha delivers you with an embeddable implementation of a 
Servlet container. It is used extensively within the Piranha project itself to 
test all the Servlet functionality.

## Example usage

```java
EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
    .servlet("SnoopServlet", SnoopServlet.class.getName())
    .servletMapping("SnoopServlet", "/SnoopServlet")
    .build()
    .initialize()
    .start();

EmbeddedRequest request = new EmbeddedRequestBuilder()
    .servletPath("/SnoopServlet")
    .build();

EmbeddedResponse response = new EmbeddedResponseBuilder()
    .build();

piranha.service(request, response);

assertTrue(response.getResponseAsString().endsWith("SUCCESS"));

piranha
    .stop()
    .destroy();
```

The example above adds the `SnoopServlet` to Piranha Embedded. It creates a `GET`
request at path `/SnoopServlet` and it create the response. And it then asks
Piranha Embedded to process it. And then it asserts the response contains
`SUCCESS`.

## Recommendation
 
 We recommend using the EmbeddedPiranhaBuilder to create your instances of
EmbeddedPiranha.

## Expectations and assumptions

1. You take care of class loading (if more isolation is needed).
1. You setup the request object.
1. You setup the response object.

## Limitations

1. Only supports one web application per Piranha Embedded instance.

## Maven coordinates

Please use the following dependency.

````xml
<dependency>
    <groupId>cloud.piranha</groupId>
    <artifactId>piranha-embedded</artifactId>
    <version>y.m.p</version>
</dependency>
````

where `y` is the year, `m` is the month and `p` is the patch version of the
release you want to use.

## Where are the sources?

See GitHub at
[https://github.com/piranhacloud/piranha/tree/master/embedded](https://github.com/piranhacloud/piranha/tree/master/embedded)

[Home](../overview.md)
