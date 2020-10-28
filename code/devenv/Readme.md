# Laufzeitumgebung für die Entwicklung

Der Ordner umfasst die Skripte zum Start der Docker-Container
* der Entwicklungs-Datenbank
* des Schemareader-Service

Doku zum Schemareader in seinem [Repo](https://github.com/simi-so/schemareader)

Der Schemareader zeigt der Abgeschlossenheit halber auf die Entwicklungs-Datenbank selbst.

Falls Beispielsweise nur die Entwicklungs-DB gestartet werden soll:

```shell script
docker-compose start devdb
```

anstatt

```shell script
docker-compose up
```

## Zugänge für Db und Schemareader-Service

**Entwicklungs-DB:** Standardzugriff via localhost port 5432

```
jdbc:postgresql://localhost:5432/simi
```

**Schemareader-Service:** Zugriff via localhost port 8081

```
http://localhost:8081/simi?schema=pub
```

Details siehe auch [docker-compose.yml](docker-compose.yml)