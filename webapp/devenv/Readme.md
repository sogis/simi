# Laufzeitumgebung für die Entwicklung

Der Ordner umfasst die Skripte zum Start der Docker-Container
* der Entwicklungs-Datenbanken
* des Schemareader-Service

Der Schemareader zeigt auf die Standarddatenbank "postgres". Diese umfasst Beispiel-Geodaten im Schema "tiger".

Doku zum Schemareader in seinem [Repo](https://github.com/sogis/simi-schemareader)

Falls Beispielsweise nur die Entwicklungs-DB gestartet werden soll:

```shell script
docker-compose start databases
```

anstatt

```shell script
docker-compose up
```

## Zugänge für Datenbanken und Schemareader-Service

**Simi-DB:** Standardzugriff via localhost port 5432

```
jdbc:postgresql://localhost:5432/simi
```

**Geo-DB:** Standardzugriff via localhost port 5432

```
jdbc:postgresql://localhost:5432/postgres
```

**Schemareader-Service:** Zugriff via localhost port 8081

```
http://localhost:8081/geodb?schema=tiger
```