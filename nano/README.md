# Piranha Nano

This distribution of Piranha is the smallest in our lineup and it delivers you
with a very opinionated embeddable partial implementation of a Servlet container.
While very small it most certainly is very capable and it is very easy to use.

## Example usage

```java
NanoPiranha piranha = new NanoPiranhaBuilder()
    .servlet("TestHelloWorldServlet", new TestHelloWorldServlet())
    .build();

NanoRequest request = new NanoRequestBuilder()
    .method("GET")
    .servletPath("/index.html")
    .build();

ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
NanoResponse response = new NanoResponseBuilder()
    .outputStream(outputStream)
    .build();

piranha.service(request, response);
```

The example above adds the `TestHelloWorldServlet` to Piranha Nano. It creates
a `GET` request at path `index.html` and it creates a response with a
`ByteArrayOutputStream` as it output stream. And it then asks Piranha Nano to
process it.

## Expectations and assumptions

1. You take care of class loading (if more isolation is needed)
1. You add and initialize the filters you want to use in the right order
1. You add and initialize the one servlet you want to use (optional)
1. You setup the request object
1. You setup the response object

## Limitations

1. Does not support ServletContainerInitializers
1. Does not support ServletContextListeners
1. Does not support ServletRequestListeners
1. Does not support request dispatching API
1. Does not support asynchronous Servlet API
1. Does not support HTTP sessions

## Maven coordinates

Please use the following dependency.

````xml
<dependency>
    <groupId>cloud.piranha</groupId>
    <artifactId>piranha-nano</artifactId>
    <version>y.m.p</version>
</dependency>
````

where `y` is the year, `m` is the month and `p` is the patch version of the
release you want to use.

## Where are the sources?

See GitHub at
[https://github.com/piranhacloud/piranha/tree/master/nano](https://github.com/piranhacloud/piranha/tree/master/nano)

## Do you have any more examples?

1. [Processing JSP pages](pages.md)

[Home](../overview.md)
