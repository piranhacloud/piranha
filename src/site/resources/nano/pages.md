# Using JSP pages with Piranha Nano

Below is a quick recipe to get Piranha Nano serving JSP pages.

## JSP page

```jsp
<%@page contentType="text/html" pageEncoding="UTF-8" session="false"%>
Hello JSP
```

## Java code

```java
NanoPiranha piranha = new NanoPiranhaBuilder()
    .directoryResource("src/test/jsp")
    .servlet("JSP Servlet", new JspServlet())
    .servletInitParam("JSP Servlet", "classpath", System.getProperty("java.class.path"))
    .servletInitParam("JSP Servlet", "compilerSourceVM", "1.8")
    .servletInitParam("JSP Servlet", "compilerTargetVM", "1.8")
    .build();

NanoRequest request = new NanoRequestBuilder()
    .method("GET")
    .servletPath("/index.jsp")
    .build();

ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
NanoResponse response = new NanoResponseBuilder()
    .outputStream(outputStream)
    .build();

piranha.service(request, response);
```

Note in this example ``src/test/jsp`` is the directory on your fileystem where you
put the ``index.jsp`` page

## Maven coordinates

Please use the following dependencies for the example

````xml
<dependency>
    <groupId>cloud.piranha</groupId>
    <artifactId>piranha-nano</artifactId>
    <version>y.m.p</version>
<dependency>
<dependency>
    <groupId>org.glassfish.web</groupId>
    <artifactId>jakarta.servlet.jsp</artifactId>
    <version>2.3.5</version>
</dependency>
````

where `y` is the year, `m` is the month and `p` is the patch version of the
release you want to use.

[Back](README.md)
