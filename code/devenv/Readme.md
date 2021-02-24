# Laufzeitumgebung für die Entwicklung

Der Ordner umfasst die Skripte zum Start der Docker-Container
* der Entwicklungs-Datenbanken
* des Schemareader-Service

Der Schemareader zeigt auf die Standarddatenbank "postgres". Diese umfasst Beispiel-Geodaten im Schema "tiger".

Doku zum Schemareader in seinem [Repo](https://github.com/simi-so/schemareader)

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

Details siehe auch [docker-compose.yml](docker-compose.yml)

## Dummy-Jenkins Instanz für das Entwickeln der Integration in die Pipeline

Im docker-compose.yml ist dafür der Service confpipe mit named volume "jenkins_home" konkfiguriert.
Das named volume ist nicht Teil von git, kann aber bei Verlust, ... schnell wieder erstellt werden.

Es braucht die folgenden Einstellungen:
* Keinerlei Jenkins-Plugins notwendig
* Einen Job, mit welchem die Config-Pipeline simuliert wird. Beispielsweise "confpipe"
  * Start von ausserhalb aktivieren mit dummy security token "mytoken"
  * Parameter "uuid" konfigurieren. In diesen schreibt Simi eine eigene UUID um mittels polling zu erkennen, 
  wann ein job aus der queue genommen und ausgeführt wird.
* Globale Sicherheit konfigurieren, damit anonyme das GUI von Jenkins lesend anzeigen können.