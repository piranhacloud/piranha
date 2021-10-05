#!/bin/sh

cd ..

if [ -z ${JAVA_HOME} ]; then
    echo Using default java
    JAVA_BIN=java
else
    echo Using JAVA_HOME: ${JAVA_HOME}
    JAVA_BIN=${JAVA_HOME}/bin/java
fi

if [[ "${PIRANHA_JPMS}" == "true" ]]; then
    INIT_OPTIONS="--module-path lib -m cloud.piranha.server"
else
    INIT_OPTIONS="-jar lib/piranha-server.jar"
fi

if [[ "$*" == *"--suspend"* ]]; then
    JAVA_ARGS="${JAVA_ARGS} -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:9009"
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

CMD="${JAVA_BIN} ${JAVA_ARGS} ${SSL_DEBUG} ${SSL_KEY_STORE} ${SSL_KEY_STORE_PASSWORD} \
  -Djava.util.logging.config.file=etc/logging.properties \
  ${INIT_OPTIONS} ${SSL_ON}"

echo Starting Piranha using command: ${CMD}

if [[ "$*" == *"--verbose"* ]]; then
    ${CMD}
else
    ${CMD} &
fi

echo $! >> tmp/piranha.pid
