#
# If you are NOT running on Linux make sure you pass --platform linux/amd64
# when you are building your container image.
#
FROM ubuntu
RUN useradd -m piranha
#
# Put the piranha-servlet.jar in the directory where this Dockerfile resides.
#
ADD piranha-dist-servlet.jar /home/piranha
WORKDIR /home/piranha
RUN chown piranha:piranha piranha-dist-servlet.jar
#
# Download JDK from https://github.com/CRaC/openjdk-builds/releases and put it
# in the directory where this Dockerfile resides.
# before creating container image.
#
ADD openjdk-17-crac+3_linux-x64.tar.gz /opt
ENV PATH=$PATH:/opt/openjdk-17-crac+3_linux-x64/bin
USER piranha
CMD ["java", "-jar", "piranha-dist-servlet.jar"]
