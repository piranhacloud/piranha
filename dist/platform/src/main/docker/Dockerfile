FROM eclipse-temurin:21
RUN useradd -m piranha
ADD target/piranha-dist-platform.tar.gz /home/piranha/
RUN chown -R piranha:piranha /home/piranha
WORKDIR /home/piranha/piranha/bin
USER piranha
CMD ["/bin/bash", "./run.sh"]
