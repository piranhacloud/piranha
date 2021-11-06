#!/bin/sh

cd ..

if [ -z ${JAVA_HOME} ]; then
    echo Using default java
    JAVA_BIN=java 
else
    echo Using JAVA_HOME: ${JAVA_HOME}
    JAVA_BIN=${JAVA_HOME}/bin/java
fi


if [[ "$PIRANHA_DEBUG" = "true" ]]; then
   DEBUG_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
fi

if [[ "$PIRANHA_DEBUG" == "suspend" ]]; then
   DEBUG_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005"
fi

echo ${DEBUG_OPTIONS}

#
# Set the SSL debug mode which is useful for debugging SSL connections.
#
# SSL_DEBUG=-Djavax.net.debug=ssl

${JAVA_BIN} ${DEBUG_OPTIONS} ${JAVA_ARGS} ${SSL_DEBUG} \
  -Djava.util.logging.config.file=etc/logging.properties -jar \
  lib/piranha-server.jar $* &

echo $! >> tmp/piranha.pid