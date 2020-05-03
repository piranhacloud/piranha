#!/bin/sh

cd ..

if [ -z ${JAVA_HOME} ]; then
    echo Using default java
    JAVA_BIN=java 
else
    echo Using JAVA_HOME: ${JAVA_HOME}
    JAVA_BIN=${JAVA_HOME}/bin/java
fi

#
# SSL_DEBUG - set the SSL debug mode which is useful for debugging SSL connections
#             on the server side
#
# SSL_DEBUG=-Djavax.net.debug=ssl

#
# SSL_KEY_STORE - set the key store to use.
#
SSL_KEY_STORE=-Djavax.net.ssl.keyStore=etc/keystore.jks

#
# SSL_KEY_STORE_PASSWORD - set the key store password to use.
#
SSL_KEY_STORE_PASSWORD=-Djavax.net.ssl.keyStorePassword=password

${JAVA_BIN} ${SSL_DEBUG} ${SSL_KEY_STORE} ${SSL_KEY_STORE_PASSWORD} -Djava.util.logging.config.file=etc/logging.properties -jar lib/piranha-server.jar --ssl  &

echo $! >> tmp/piranha.pid
