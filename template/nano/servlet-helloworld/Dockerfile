#
# Stage used to build the custom JLink runtime
#
FROM adoptopenjdk:15 AS builder
RUN cd /usr/local && \
    curl -O https://archive.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz && \
    tar xfvz apache-maven-3.6.3-bin.tar.gz && \
    rm apache-maven-3.6.3-bin.tar.gz
COPY . /root
RUN export PATH=$PATH:/usr/local/apache-maven-3.6.3/bin && \
    cd /root && \
    mvn --no-transfer-progress clean install

#
# Stage used to create a zip file of the custom JLink runtime
#
FROM debian:10-slim AS zip
RUN apt-get update && apt-get install -y zip
COPY --from=builder /root/target/runtime /usr/local/azure/runtime
COPY --from=builder /root/src/main/azure /usr/local/azure
RUN cd /usr/local/azure ; zip -r ../azure.zip *

#
# Stage used to create a runnable Debian image based on the custom JLink runtime
#
FROM debian:10-slim
COPY --from=builder /root/target/runtime /usr/local/runtime
EXPOSE 8080
CMD ["/usr/local/runtime/bin/launcher"]
