FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD signalevent-0.0.1.jar signalevent.jar
RUN sh -c 'touch /signalevent.jar'
EXPOSE 8035
ENTRYPOINT [ "java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/signalevent.jar" ]