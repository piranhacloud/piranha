FROM eclipse-temurin:17
RUN useradd -m piranha
ADD target/piranha-dist-microprofile.jar /home/piranha
WORKDIR /home/piranha
RUN chown piranha:piranha piranha-dist-microprofile.jar
USER piranha
CMD ["java", "-jar", "piranha-dist-microprofile.jar"]
