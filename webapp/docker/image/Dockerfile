FROM openjdk:11-bullseye

USER root

WORKDIR /home/simi

RUN chown -R 1001:0 . && \
    chmod -R g+rw .
COPY tmp/app.jar /home/simi
COPY uber-jar-logback.xml /home/simi
RUN ls -la .

USER 1001
EXPOSE 8080

#Log4j 2 CVE-2021-44228
ENV LOG4J_FORMAT_MSG_NO_LOOKUPS=true

CMD ["java", "-XX:MaxRAMPercentage=80.0", "-Dlogback.configurationFile=file:./uber-jar-logback.xml", "-jar", "app.jar"]
