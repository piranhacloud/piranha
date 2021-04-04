#!/bin/bash

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

exec ${JAVA_BIN} ${JAVA_ARGS} -Djava.util.logging.config.file=etc/logging.properties ${INIT_OPTIONS} &

PID=$!
echo $PID >> tmp/piranha.pid
wait $PID
