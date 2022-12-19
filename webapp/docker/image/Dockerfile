FROM openjdk:11-bullseye

USER root

WORKDIR /home/simi

COPY ca-certificates/ /usr/local/share/ca-certificates/
RUN update-ca-certificates
RUN chown -R 1001:0 . && \
    chmod -R g+rw .
COPY tmp/app.jar .
COPY uber-jar-logback.xml .
RUN ls -la .

USER 1001
EXPOSE 8080

#Log4j 2 CVE-2021-44228
ENV LOG4J_FORMAT_MSG_NO_LOOKUPS=true

CMD ["java", "-XX:MaxRAMPercentage=80.0", "-Dlogback.configurationFile=file:./uber-jar-logback.xml", "-jar", "app.jar"]
