FROM ubuntu

#
# Put the piranha-servlet.jar in the directory where this Dockerfile resides.
#
ADD piranha-dist-servlet.jar /root

#
# Download JDK from https://github.com/CRaC/openjdk-builds/releases and put it
# in the directory where this Dockerfile resides. Adjust the filenames below if
# you are using a newer release.
#
ADD openjdk-17-crac+3_linux-x64.tar.gz /opt
ENV JAVA_HOME=/opt/openjdk-17-crac+3_linux-x64
ENV PATH=$PATH:/opt/openjdk-17-crac+3_linux-x64/bin

#
# Add checkpoint directory.
#
ADD cr cr

#
# Restore java process from checkpoint.
#
CMD ["java", "-XX:CRaCRestoreFrom=cr"]
