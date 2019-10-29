#!/bin/sh

cd ..
java -jar lib/piranha-runner-installed.jar &
PID=$!
echo $PID >> tmp/piranha.pid
fg $PID 
