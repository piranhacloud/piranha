FROM eclipse-temurin:17
RUN useradd -m piranha
ADD target/piranha-dist-isolated.tar.gz /home/piranha/
RUN chown -R piranha:piranha /home/piranha
WORKDIR /home/piranha/piranha/bin
USER piranha
CMD ["/bin/bash", "./run.sh"]
