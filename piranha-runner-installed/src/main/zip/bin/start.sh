#!/bin/sh

cd ..
java -jar lib/piranha-runner-installed.jar &
echo $! >> tmp/piranha.pid

