# Modular applications with JPMS

Starting with 21.1.0, Piranha support for running applications in JPMS layers.

For this example, we'll create a simple Servlet to show how to enable this function. You can download the Piranha Micro here: https://github.com/piranhacloud/piranha/releases.

Start it by adding a module-info in the `src/main/java` folder of your application:
```java
module modular.application {
    exports modular.application;
    requires jakarta.servlet;
}
```

Then, create a Servlet. In this case let's output the module name and print the stack trace to see all the code leading to the invocation of the servlet and which module it belongs to:

```java
package modular.application;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class SimpleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        response.setContentType("text/plain");  
        response.getWriter().println("Running from " + this.getClass().getModule());  
        new Exception().printStackTrace(response.getWriter());  
    }
}
```

Now, run the application with the `cloud.piranha.modular.enable` property enabled:
```
java -Dcloud.piranha.modular.enable=true -jar piranha-micro-21.1.0.jar --war app.war
```

You should be able to see the module name and the stack trace with module information accessing `http://localhost:8080/hello`:
```
Running from module modular.application
java.lang.Exception
	at modular.application@1.0-SNAPSHOT/modular.application.SimpleServlet.doGet(SimpleServlet.java:16)
	at jakarta.servlet@21.1.0/jakarta.servlet.http.HttpServlet.service(HttpServlet.java:148)
	at jakarta.servlet@21.1.0/jakarta.servlet.http.HttpServlet.service(HttpServlet.java:184)
	at cloud.piranha.webapp.impl@21.1.0/cloud.piranha.webapp.impl.DefaultFilterChain.doFilter(DefaultFilterChain.java:115)
	at cloud.piranha.security.exousia@21.1.0/cloud.piranha.security.exousia.AuthorizationFilter.doFilter(AuthorizationFilter.java:95)
	at cloud.piranha.security.exousia@21.1.0/cloud.piranha.security.exousia.AuthorizationFilter.doFilter(AuthorizationFilter.java:85)
	at cloud.piranha.webapp.impl@21.1.0/cloud.piranha.webapp.impl.DefaultFilterChain.doFilter(DefaultFilterChain.java:111)
	at cloud.piranha.security.eleos@21.1.0/cloud.piranha.security.eleos.AuthenticationFilter.doFilter(AuthenticationFilter.java:89)
	at cloud.piranha.security.eleos@21.1.0/cloud.piranha.security.eleos.AuthenticationFilter.doFilter(AuthenticationFilter.java:83)
	at cloud.piranha.webapp.impl@21.1.0/cloud.piranha.webapp.impl.DefaultFilterChain.doFilter(DefaultFilterChain.java:111)
	at cloud.piranha.security.exousia@21.1.0/cloud.piranha.security.exousia.AuthorizationPreFilter.doFilter(AuthorizationPreFilter.java:106)
	at cloud.piranha.security.exousia@21.1.0/cloud.piranha.security.exousia.AuthorizationPreFilter.doFilter(AuthorizationPreFilter.java:91)
	at cloud.piranha.webapp.impl@21.1.0/cloud.piranha.webapp.impl.DefaultFilterChain.doFilter(DefaultFilterChain.java:111)
	at cloud.piranha.webapp.impl@21.1.0/cloud.piranha.webapp.impl.DefaultServletRequestDispatcher.request(DefaultServletRequestDispatcher.java:163)
	at cloud.piranha.webapp.impl@21.1.0/cloud.piranha.webapp.impl.DefaultWebApplication.service(DefaultWebApplication.java:1603)
	at cloud.piranha.http.webapp@21.1.0/cloud.piranha.http.webapp.HttpWebApplicationServer.service(HttpWebApplicationServer.java:281)
	at cloud.piranha.http.webapp@21.1.0/cloud.piranha.http.webapp.HttpWebApplicationServer.process(HttpWebApplicationServer.java:239)
	at cloud.piranha.http.impl@21.1.0/cloud.piranha.http.impl.DefaultHttpServerProcessingThread.run(DefaultHttpServerProcessingThread.java:85)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:630)
	at java.base/java.lang.Thread.run(Thread.java:832)
```

Another feature is that you can declare services in the module-info.java instead of creating a file in the /META-INF/services folder.

To try this out, let's create a ServletContainerInitializer.

```java
package modular.application;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import java.util.Set;

public class SimpleInitializer implements ServletContainerInitializer {  
    @Override  
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {  
        System.out.println("Initializer invoked: " + this.getClass().getModule());
    }  
}
```

Add it to the module-info.java:
```java
module modular.application {
    exports modular.application;
    provides jakarta.servlet.ServletContainerInitializer with modular.application.SimpleInitializer;
    requires jakarta.servlet;
}
```

Compiling and running, you should be able to see the result in the terminal:
```
Initializer invoked: module modular.application
```

## Notes
 - Your libraries will be promoted to automatic modules unless they have split packages, otherwise, they will be part of the unnamed module.
 - You can use the following properties to change the behavior of the layer: `cloud.piranha.modular.add-reads`, `cloud.piranha.modular.add-exports` and `cloud.piranha.modular.add-opens`;
