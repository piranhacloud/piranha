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
# Turn SSL on.
#
# SSL_ON=--ssl

#
# Set the SSL debug mode which is useful for debugging SSL connections.
#
# SSL_DEBUG=-Djavax.net.debug=ssl

#
# Set the key store to use.
#
# SSL_KEY_STORE=-Djavax.net.ssl.keyStore=etc/keystore.jks

#
# Set the key store password to use.
#
# SSL_KEY_STORE_PASSWORD=-Djavax.net.ssl.keyStorePassword=password

${JAVA_BIN} ${DEBUG_OPTIONS} ${JAVA_ARGS} ${SSL_DEBUG} ${SSL_KEY_STORE} ${SSL_KEY_STORE_PASSWORD} \
  -Djava.util.logging.config.file=etc/logging.properties -jar \
  lib/piranha-server.jar ${SSL_ON} &

echo $! >> tmp/piranha.pid