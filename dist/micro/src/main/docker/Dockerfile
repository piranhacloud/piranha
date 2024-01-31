FROM eclipse-temurin:21
RUN useradd -m piranha
ADD target/piranha-dist-micro.jar /home/piranha
WORKDIR /home/piranha
RUN chown piranha:piranha piranha-dist-micro.jar
USER piranha
CMD ["java", "-jar", "piranha-dist-micro.jar"]
