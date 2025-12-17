FROM eclipse-temurin:11

RUN mkdir /opt/app
WORKDIR /opt/app
COPY tmp/app.jar .
COPY uber-jar-logback.xml .

EXPOSE 8080
#Log4j 2 CVE-2021-44228
ENV LOG4J_FORMAT_MSG_NO_LOOKUPS=true

CMD ["java", "-XX:MaxRAMPercentage=80.0", "-Dlogback.configurationFile=file:./uber-jar-logback.xml", "-jar", "app.jar"]
