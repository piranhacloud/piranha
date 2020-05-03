# Piranha

<table style="background: rgb(0,0,0,0); border: 0px">
  <tr style="background: rgb(0,0,0,0);">
    <td style="padding: 50px 50px 0px 50px; background: rgb(0,0,0,0);">
       <img src="https://avatars2.githubusercontent.com/u/57717577?u=57c9127c1d165a17c5d3d6b2d985cd9fc363ecf1f" width="200" height="200" style="border: 0px">
    </td>
    <td style="background: rgb(0,0,0,0);">
       <hr width="1" size="150">
    </td>
    <td style="vertical-align:middle; padding: 50px 50px 0px 50px; background: rgb(0,0,0,0);">
       Release coming soon!
    </td>
  </tr>

  <tr style="background: rgb(0,0,0,0);">
    <td style="vertical-align:top; background: rgb(0,0,0,0);">
      <sup><i>You want Cloud? We got Clout!</i></sup>
    </td>
  </tr>
</table>

Piranha is a cloud native runtime for various libraries, specifically for running Jakarta EE and MicroProfile libs in a non-AS centric way. Optimized for unit testing and running on a flat classpath.

## Can Piranha really strip a server to the core in under a minute?

A traditional Jakarta EE runtime is an installed application server acting as a deployment target, which supports running multiple applications via archives, which are each isolated by classloaders, and serves these applications via remote protocols such as HTTP.

Piranha's scalable architecture can strip away all these features until only the bare libraries remain. A Faces application can be run having only the libraries on the flat classpath, and not even having an HTTP server running. Whether this can be done in under a minute depends on the skill and experience of the Piranha dev ;)

## Scalable architecture

Piranha can scale down to essentially a unit testing framework, up to something more akin to a traditional application server, and many steps in between.

* [Piranha Nano](nano/README.md)
* [Piranha Embedded](embedded/README.md)
* Piranha Micro
* Piranha Server

## Cloud Native

The cloud deployment model focusses on small, immutable units. With containers like Docker and platforms like Kubernetes, the archive and deployment features of a traditional application server are not necessarily needed anymore, but the Jakarta EE and MicroProfile APIs themselves are still really useful. Piranha can focus on only these APIs, and completely omitting (not just hiding) all functionality related to archives and deployment, making it optimally suited for a cloud environment.  

## Unit (like) testing

Because traditional Jakarta EE products come with the deployment model, there is a bit more involved with testing code. Tools like Arquillian make integration testing quite easy, but often developers like a more unit testing oriented approach as well.

Piranha can be used almost as a mocking framework, except its not mocking but using The Real Thing:

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

piranha.stop()
    .destroy();
```

## Piranha team accolades

* Java Champion
* Spec lead/project lead
* Duke Choice Award
* Worked on GlassFish, Payara, OmniFaces, Mojarra, Soteria, and more.

<p align="center">
    <img src="https://avatars2.githubusercontent.com/u/57717577?u=57c9127c1d165a17c5d3d6b2d985cd9fc363ecf1f" width="200" height="200">
</p>
