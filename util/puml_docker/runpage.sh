# Startet den Plant-UML server in Hintergrund - Adresse: http://localhost:8080

docker run \
  -p 8080:8080 \
  --name puml \
  plantuml/plantuml-server:jetty

docker rm -f /puml # Docker sagt, der container sei noch vorhanden, obwohl "puml_docker container ls" nichts zurückgibt...?

