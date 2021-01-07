<%@page contentType="text/plain" pageEncoding="UTF-8" import="java.io.*" autoFlush="false"%>
<%
    out.close();
    ServletContext ctx = (ServletContext) application;
    try {
        out.flush();
    } catch (Throwable t) {
        if (t instanceof IOException) {
            ctx.setAttribute("flush.exception", t);
        }
    }

    try {
        out.println();
    } catch (Throwable t) {
        if (t instanceof IOException) {
            ctx.setAttribute("write.exception", t);
        }
    }

    try {
        out.close();
    } catch (Throwable t) {
        ctx.setAttribute("close.exception", t);
    }
%>
