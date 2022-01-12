# Erzeugen / Aktualisieren des Datenbankschemas

Die sauberste zur Verfügung stehende Möglichkeit ist das Ausführen der vom Framework dafür erstellten Gradle-Tasks 
`createDb` und `updateDb`.

Die Verbindungsparameter für die Datenbank werden dabei wie für die Webapplikation selbst über die Umgebungsvariablen
`CUBA_DATASOURCE_*` gesetzt.
 
Beispiel mit Befehl updateDb und lokaler Datenbank (Github-Repo "simi" clonen und Befehl im Verzeichnis **/webapp** ausführen):

```
export CUBA_DATASOURCE_JDBCURL=jdbc:postgresql://localhost:5432/postgres?currentSchema=simi
export CUBA_DATASOURCE_USERNAME=postgres
export CUBA_DATASOURCE_PASSWORD=postgres

./gradlew updateDb
```

