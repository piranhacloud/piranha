FROM eclipse-temurin:21
RUN useradd -m piranha
ADD target/piranha-dist-coreprofile.jar /home/piranha
WORKDIR /home/piranha
RUN chown piranha:piranha piranha-dist-coreprofile.jar
USER piranha
CMD ["java", "-jar", "piranha-dist-coreprofile.jar"]
