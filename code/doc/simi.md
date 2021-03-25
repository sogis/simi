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
* Konfiguration der Verbindung auf den LDAP-Server:
  * **CUBA_WEB_LDAP_ENABLED:** LDAP Authentifizierung aktiv? Bsp: TRUE / FALSE
  * **CUBA_WEB_LDAP_URLS:** LDAP URL. Bsp: LDAP://192_168_1_1:389
  * **CUBA_WEB_LDAP_BASE:** Distinguished Name, auf welchen gebunden wird. Bsp: OU=EMPLOYEES,DC=MYCOMPANY,DC=COM
  * **CUBA_WEB_LDAP_USER:** Benutzer, mit welchem verbunden wird. Bsp: CN=SYSTEM USER,OU=EMPLOYEES,DC=MYCOMPANY,DC=COM
  * **CUBA_WEB_LDAP_PASSWORD:** Passwort, mit welchem verbunden wird. Bsp: SYSTEM_USER_PASSWORD
  * **CUBA_WEB_REQUIRE_PASSWORD_FOR_NEW_USERS:** Muss bei LDAP Auth auf false gesetzt sein, damit kein "Kuba Passwort" für
  den Benutzer gesetzt wird.
  * **CUBA_WEB_LDAP_USER_LOGIN_FIELD:** Name des Attributes im LDAP-Verzeichnis, welches gegen den Login-Namen verglichen wird.
* Konfiguration der URL des Schemareaders:
  * **SIMI_SCHEMAREADER_URL:** Url, auf welcher der Schemareader erreichbar ist. Bsp: "http://localhost/schemareader"
* Konfiguration der Suche in den GRETL-Repos (Anzeige der Abhängigkeiten):
  * **SIMI_GITSEARCH_URL:** Url, auf welche die Git-Suchen abgesetzt werden. Bsp: "https://api.github.com/search/code"
  * **SIMI_GITSEARCH_REPOS:** Liste aller zu durchsuchenden Git-Repos, mittels "|" getrennt. Bsp: "sogis/gretljobs|oereb/jobs"
  
## Kopieren von Data-Products

Die im GUI harmlos erscheinende Kopierfunktion ist aufgrund der vielen zu berücksichtigenden Beziehungen
ziemlich komplex.

Die Klassen innerhalb des Kopierkontextes werden kopiert (dupliziert), die ausserhalb werden referenziert. 
Nach dem Kopiervorgang zeigt also sowohl das Original wie auch die Kopie auf das gleiche ausserhalb des
Kopierkontextes liegende Objekt.

Hinweis: Die Vererbungen sind in den Diagrammen nicht abgebildet.

### Tableview

![Tableview Copy](simi_resources/copy-tableview.png)

### Rasterview

![Rasterview Copy](simi_resources/copy-rasterview.png)

### FacadeLayer

![Facadelayer Copy](simi_resources/copy-facadelayer.png)

### Layergroup

![Layergroup Copy](simi_resources/copy-layergroup.png)

### Map

![Map Copy](simi_resources/copy-map.png)








