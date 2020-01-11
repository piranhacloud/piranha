#!/bin/sh

cd ..

if [ -z ${JAVA_HOME} ]; then
    echo Using default java
    JAVA_BIN=java 
else
    echo Using JAVA_HOME: ${JAVA_HOME}
    JAVA_BIN=${JAVA_HOME}/bin/java
fi

${JAVA_BIN} -Djava.util.logging.config.file=etc/logging.properties -jar lib/piranha-server.jar  &

echo $! >> tmp/piranha.pid
