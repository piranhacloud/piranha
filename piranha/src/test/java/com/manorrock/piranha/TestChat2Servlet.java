/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A test chat servlet.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestChat2Servlet extends HttpServlet {

    /**
     * Stores the async queue.
     */
    private static final ConcurrentLinkedQueue<AsyncContext> ASYNC_QUEUE = new ConcurrentLinkedQueue<>();

    /**
     * Stores the message queue.
     */
    private static final LinkedBlockingQueue<String> MESSAGE_QUEUE = new LinkedBlockingQueue<>();

    /**
     * Stores the message thread.
     */
    private Thread messageThread = null;

    /**
     * Stores serial version UID.
     */
    private static final long serialVersionUID = -2919167206889576860L;

    /**
     * Initialize the servlet.
     *
     * @param config the servlet config.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        Runnable messageRunnable = () -> {
            boolean done = false;
            while (!done) {
                String message;
                try {
                    message = MESSAGE_QUEUE.take();
                    ASYNC_QUEUE.stream().forEach((context) -> {
                        try {
                            PrintWriter writer = context.getResponse().getWriter();
                            writer.println(message);
                            writer.flush();
                        } catch (IOException exception) {
                            ASYNC_QUEUE.remove(context);
                        }
                    });
                } catch (InterruptedException exception) {
                    done = true;
                }
            }
        };
        /**
         * Run the notifier thread.
         */
        messageThread = new Thread(messageRunnable);
        messageThread.start();
    }

    /**
     * Get request, which initializes the async processing.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        response.setHeader("Cache-Control", "private");
        response.setHeader("Pragma", "no-cache");
        PrintWriter writer = response.getWriter();
        writer.flush();
        final AsyncContext context = request.startAsync(request, response);
        context.setTimeout(300000);
        context.addListener(new AsyncListener() {

            /**
             * Handle the completion.
             *
             * @param event the event.
             */
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                ASYNC_QUEUE.remove(context);
            }

            /**
             * Handle the timeout.
             *
             * @param event the event.
             */
            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                ASYNC_QUEUE.remove(context);
            }

            /**
             * Handle an error.
             *
             * @param event the event.
             */
            @Override
            public void onError(AsyncEvent event) throws IOException {
                ASYNC_QUEUE.remove(context);
            }

            /**
             * Handle starting async processing.
             *
             * @param event the event.
             */
            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
            }
        });
        ASYNC_QUEUE.add(context);
    }

    /**
     * Post the action.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain");
        response.setHeader("Cache-Control", "private");
        response.setHeader("Pragma", "no-cache");
        String action = request.getParameter("action");
        String name = request.getParameter("name");
        if (null != action) {
            switch (action) {
                case "login": {
                    String message = name + " has joined.";
                    notify(message);
                    response.getWriter().println("success");
                    break;
                }
                case "post": {
                    String content = request.getParameter("message");
                    String message = name + " said " + content;
                    notify(message);
                    response.getWriter().println("success");
                    break;
                }
                default:
                    response.sendError(422, "Unable to process");
                    break;
            }
        }
    }

    /**
     * Destroy the servlet.
     */
    @Override
    public void destroy() {
        ASYNC_QUEUE.clear();
        messageThread.interrupt();
    }

    /**
     * Notify.
     *
     * @param message the message.
     * @throws IOException when an I/O error occurs.
     */
    private void notify(String message) throws IOException {
        try {
            MESSAGE_QUEUE.put(message);
        } catch (InterruptedException exception) {
            throw new IOException(exception);
        }
    }
}
