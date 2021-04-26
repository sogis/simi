# SIMI

Webapplikation zur Pflege der Metainformationen der GDI-SO (**S**patial **I**nfrastructure **M**etadata **I**nterface)

## Konfigurieren und Starten

Die Konfiguration erfolgt mittels der folgenden Umgebungsvariablen:

* Konfiguration der Verbindung auf die Meta-DB:
  * **CUBA_DATASOURCE_USERNAME:** Benutzername der DB-Connection. Bsp: "postgres"
  * **CUBA_DATASOURCE_PASSWORD:** Passwort der DB-Connection. Bsp: "postgres"
  * **CUBA_DATASOURCE_DBNAME:** Name der Datenbank. Bsp: "simi"
  * **CUBA_DATASOURCE_HOST:** Name des Hosts des Postgres-Clusters. Bsp: "localhost"
  * **CUBA_DATASOURCE_PORT:** Nummer des Ports des Postgres-Clusters. Bsp: 5432
* Konfiguration der URL des Schemareaders:
  * **SIMI_SCHEMAREADER_URL:** Url, auf welcher der Schemareader erreichbar ist. Bsp: "http://localhost/schemareader"
* Konfiguration der Suche in den GRETL-Repos (Anzeige der Abhängigkeiten):
  * **SIMI_GITSEARCH_URL:** Url, auf welche die Git-Suchen abgesetzt werden. Bsp: "https://api.github.com/search/code"
  * **SIMI_GITSEARCH_REPOS:** Liste aller zu durchsuchenden Git-Repos, mittels "," getrennt. Bsp: "sogis/gretljobs,oereb/jobs"