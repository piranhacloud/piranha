<%@page contentType="text/plain" pageEncoding="UTF-8" import="java.io.*" autoFlush="false"%>
<%
    if (out.getBufferSize() > 0) {
        out.println("Arbitrary text");
        out.flush();
        out.println("Test FAILED");
        try {
            out.clearBuffer();
        } catch (Throwable t) {
            if (t instanceof IOException) {
                out.println("Test FAILED");
                return;
            } else {
                out.println("Test PASSED");
            }
        }
        out.println("Test PASSED");
    } else {
        out.println("Test PASSED");
    } 
%>
