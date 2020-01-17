cd ..

echo 'PID FILE' >> tmp\piranha.pid
start /b java -Djava.util.logging.config.file=etc/logging.properties -jar lib\piranha-server.jar

cd bin


