
# Manorrock Piranha Single Threaded

The Manorrock Piranha Single Threaded module is a module that can be used to run
Manorrock Piranha constrained to a single server thread.

## How to use?

    SingleThreadHttpServer server = new SingleThreadHttpServer();
    server.start();

This snippet above will start the single threaded server on port 8080.