#!/bin/bash
set -m

cd ..

if [ -z ${JAVA_HOME} ]; then
    echo Using default java
    JAVA_BIN=java
else
    echo Using JAVA_HOME: ${JAVA_HOME}
    JAVA_BIN=${JAVA_HOME}/bin/java
fi

if [[ "${PIRANHA_JPMS}" == "true" ]]; then
    INIT_OPTIONS="--module-path lib -m cloud.piranha.server.lite"
else
    INIT_OPTIONS="-jar lib/piranha-server-lite.jar"
fi


if [[ "$*" == *"--suspend"* ]]; then
    JAVA_ARGS="${JAVA_ARGS} -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:9009"
fi

#
# Set the SSL debug mode which is useful for debugging SSL connections.
#
# SSL_DEBUG=-Djavax.net.debug=ssl

CMD="${JAVA_BIN} ${JAVA_ARGS} ${SSL_DEBUG} \
  -Djava.util.logging.config.file=etc/logging.properties \
  ${INIT_OPTIONS} $*"

echo Starting Piranha using command: ${CMD}

touch tmp/piranha.pid

if [[ "$*" == *"--verbose"* ]]; then
    ${CMD}
else
   if [[ "$*" == *"--run"* ]]; then
      exec ${CMD} &
      PID=$!
      echo $PID >> tmp/piranha.pid
      wait $PID
   else
      ${CMD} &
      echo $! >> tmp/piranha.pid
   fi
fi
