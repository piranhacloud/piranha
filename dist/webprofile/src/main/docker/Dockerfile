FROM eclipse-temurin:21
RUN useradd -m piranha
ADD target/piranha-dist-webprofile.jar /home/piranha
WORKDIR /home/piranha
RUN chown piranha:piranha piranha-dist-webprofile.jar
USER piranha
CMD ["java", "-jar", "piranha-dist-webprofile.jar"]
