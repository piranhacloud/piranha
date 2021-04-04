#!/bin/sh

cd ..

if [ -z ${JAVA_HOME} ]; then
    echo Using default java
    JAVA_BIN=java 
else
    echo Using JAVA_HOME: ${JAVA_HOME}
    JAVA_BIN=${JAVA_HOME}/bin/java
fi

if [[ "${PIRANHA_JPMS}" == "false" ]]; then
    INIT_OPTIONS="-jar lib/piranha-server.jar"
else
    INIT_OPTIONS="--module-path lib -m cloud.piranha.server"
fi

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

${JAVA_BIN} ${JAVA_ARGS} ${SSL_DEBUG} ${SSL_KEY_STORE} ${SSL_KEY_STORE_PASSWORD} \
  -Djava.util.logging.config.file=etc/logging.properties \
  ${INIT_OPTIONS} ${SSL_ON} &

echo $! >> tmp/piranha.pid
