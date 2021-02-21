package helloworld;

import cloud.piranha.http.impl.DefaultHttpServer;
import cloud.piranha.http.nano.NanoHttpServerProcessor;
import cloud.piranha.nano.NanoPiranha;
import cloud.piranha.nano.NanoPiranhaBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HelloWorldServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, ServletException {
        try (PrintWriter writer = response.getWriter()) {
            writer.println("Hello World");
            writer.flush();
        }
    }

    /**
     * Main method.
     * 
     * <p>
     *  Note that the FUNCTIONS_CUSTOMERHANDLER_PORT environment variable is
     *  used by Azure Functions to determine which port needs to be used to host
     *  your function. This port is randomly picked by Azure Functions and thus
     *  has to be honored when deploying to Azure Functions.
     * </p>
     * 
     * @param arguments the command line arguments.
     * @throws Exception when a serious error occurs.
     */
    public static void main(String[] arguments) throws Exception {
        NanoPiranha piranha = new NanoPiranhaBuilder()
                .servlet("HelloWorld", new HelloWorldServlet())
                .build();
        int port = System.getenv("FUNCTIONS_CUSTOMHANDLER_PORT") != null 
                ? Integer.parseInt(System.getenv("FUNCTIONS_CUSTOMHANDLER_PORT")) : 8080;
        DefaultHttpServer server = new DefaultHttpServer(port, 
                new NanoHttpServerProcessor(piranha), false);
        server.start();
    }
}
