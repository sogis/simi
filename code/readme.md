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
  
  ## Kleine Verbesserungen V 1.1.x
  * Konsequent Anzeige von "\[NULL\]" oder ähnlich bei Attributen, die null sind.

  ## Mögliche Erweiterungen / Anpassungen V 1.2

  ### Externe WMS / WMTS

  * Gruppierungs-Limitationen aus Frühlingsrelease auflösen
  * Notwendige Eigenschaften in Metamodell vollständig abbilden
  * Prüfen, ob die extern verfügbaren Eigenschaften (Titel, Beschreibung, ...) im Rahmen der Pipeline zur Laufzeit abgeholt werden sollen

  ### Print

  * Konzeptionellen Entscheid für heutige Lösung nachvollziehen
  * Der Print-WMS umfasst ca. 5% mehr Ebenen wie der publizierte (=105%). Macht das so Sinn, oder sollte der Print nur die 5 zusätzlichen Prozent umfassen?
  * Zusammenhang mit Last und Antwortzeiten beachten

  ### Transparenz

  * Überschreibung der Transparenz der Einzelebenen eines Facadelayers? (In PropertiesInFacade)