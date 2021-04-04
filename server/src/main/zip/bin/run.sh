#!/bin/bash

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

exec ${JAVA_BIN} ${JAVA_ARGS} -Djava.util.logging.config.file=etc/logging.properties ${INIT_OPTIONS} &

PID=$!
echo $PID >> tmp/piranha.pid
wait $PID
