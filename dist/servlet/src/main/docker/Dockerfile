FROM eclipse-temurin:17
RUN useradd -m piranha
ADD target/piranha-dist-servlet.jar /home/piranha
WORKDIR /home/piranha
RUN chown piranha:piranha piranha-dist-servlet.jar
USER piranha
CMD ["java", "-jar", "piranha-dist-servlet.jar"]
