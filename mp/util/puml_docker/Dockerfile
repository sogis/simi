FROM plantuml/plantuml-server

ENV PLANTUML_VERSION=1.2019.13
ENV LANG de_CH.UTF-8
RUN wget "http://downloads.sourceforge.net/project/plantuml/${PLANTUML_VERSION}/plantuml.${PLANTUML_VERSION}.jar" -O plantuml.jar
#RUN ["java", "-Djava.awt.headless=true", "-jar", "plantuml.jar", "-version"]
#RUN ["dot", "-version"]
ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "plantuml.jar", "-p"]
#CMD ["-tsvg"]
