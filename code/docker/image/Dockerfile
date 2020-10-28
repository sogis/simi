FROM openjdk:11-slim

USER root

WORKDIR /home/simi
COPY tmp/app.jar /home/simi
COPY uber-jar-logback.xml /home/simi

RUN cd /home/simi && \
    chown -R 1001:0 /home/simi && \
    chmod -R g+rw /home/simi && \
    ls -la /home/simi

USER 1001
EXPOSE 8080
CMD java -XX:MaxRAMPercentage=80.0 -Dlogback.configurationFile=file:./uber-jar-logback.xml -jar app.jar
