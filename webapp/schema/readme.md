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

Falls Tabellen angepasst oder neu angelegt werden müssen nachfolgend die Rechte noch angepasst werden

```
psql "sslmode=require host=hostname dbname=dbname" -p 5432 -U simi_user -W --single-transaction -c 'GRANT SELECT ON ALL TABLES IN SCHEMA simi TO simi_read;'
psql "sslmode=require host=hostname dbname=dbname" -p 5432 -U simi_user -W --single-transaction -c 'GRANT SELECT,INSERT,UPDATE,DELETE ON ALL TABLES IN SCHEMA simi TO simi_write;'
```

