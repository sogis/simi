# SIMI

Webapplikation zur Pflege der Metainformationen der GDI-SO (**S**patial **I**nfrastructure **M**etadata **I**nterface)

## Konfigurieren und Starten des Docker-Image

Die SIMI-Images liegen auf Docker-Hub: <https://hub.docker.com/r/sogis/simi>

Der Grossteil der Funktionalitäten benötigt einzig einen DB-Connect auf die Meta-DB. Für diese sind die Umgebungsvariablen ```CUBA_DATASOURCE_*``` zu setzen sowie ```CUBA_AUTOMATICDATABASEUPDATE=true```, damit das (leere) Schema mit den notwendigen Tabellen initialisiert wird. 

**ZU BEACHTEN** Das Framework kann beim Starten nur im Schema public automatisch die Tabellen anlegen. Zu schnellen Ausprobieren also im Schema "public" arbeiten.

Siehe [doc/docker-compose.yml](doc/docker-compose.yml) als Beispiel einer Minimal-Konfiguration.

## Log-Output des konfigurierten Environment

Variablen können im Framework an verschiedensten Stellen konfiguriert werden (ENV, *.properties, Sys-Tabellen, ...). Damit schnell erkennbar wird, welche Variable zur laufzeit wie konfiguriert ist, werden die Werte beim Startup (verschlüsselt) in Log-Output geschrieben.

    ...
    ... - SIMI Configration values 
    ...----------------------------------------------------
    ... - 'CUBA_DATASOURCE_JDBCURL': [jdbc:postgresql://localhost:5432/postgres?currentSchema=public]
    ... - 'CUBA_DATASOURCE_PASSWORD': [Secret value with 8 digits]
    ... - 'CUBA_DATASOURCE_USERNAME': [postgres]
    ...


## Versionierung (Image Tags)

Die Versionierung folgt dem Pattern **\[major\].\[minor\].\[revision\]**

Auslöser für Versions-Inkrementierungen:

* **major:** Umfangreiche Erweiterung des Metamodells und / oder der Funktionalität
* **minor:** Breaking Change des Metamodells für die Trafos oder Änderung der notwendigen ENV-Variablen. 
* **revision:** (Kleinere) funktionale Anpassung

Als Nummer der revision wird automatisch die "Build-Nummer" der github action übernommen. 
Major und minor werden bewusst von der Entwicklung gesetzt.

## Umgebungsvariablen

Die Konfiguration erfolgt mittels der folgenden Umgebungsvariablen:

* **Konfiguration der Verbindung auf die Meta-DB:**
  * CUBA_DATASOURCE_USERNAME: Benutzername der DB-Connection. Bsp: "postgres"
  * CUBA_DATASOURCE_PASSWORD: Passwort der DB-Connection. Bsp: "postgres"
  * CUBA_DATASOURCE_JDBCURL: Jdbc Url für das Verbinden auf die DB (inklusive Info welches Schema). Bsp: jdbc:postgresql://localhost:5432/simi?currentSchema=simi.   
    Bemerkung: Host, Port, ... sollen nicht mehr separat angegeben werden, da in der jdbc URL enthalten.
* **Konfiguration der Authentifizierung (LDAP, ...):**
  * CUBA_WEB_LDAP_ENABLED: LDAP Authentifizierung aktiv? Bsp: TRUE / FALSE
  * CUBA_WEB_LDAP_URLS: LDAP URL. Bsp: LDAP://192_168_1_1:389
  * CUBA_WEB_LDAP_BASE: Distinguished Name, auf welchen gebunden wird. Bsp: OU=EMPLOYEES,DC=MYCOMPANY,DC=COM
  * CUBA_WEB_LDAP_USER: Benutzer, mit welchem verbunden wird. Bsp: CN=SYSTEM USER,OU=EMPLOYEES,DC=MYCOMPANY,DC=COM
  * CUBA_WEB_LDAP_PASSWORD: Passwort, mit welchem auf das LDAP verbunden wird.
  * CUBA_WEB_LDAP_USERLOGINFIELD: Name des Attributes im LDAP-Verzeichnis, welches gegen den Login-Namen 
  verglichen wird. Bei AD häufig "sAMAccountName".
  * CUBA_WEB_REQUIREPASSWORDFORNEWUSERS: Muss bei LDAP Auth auf false gesetzt sein, damit kein "Kuba Passwort" für
  den Benutzer verlangt wird.
  * CUBA_WEB_STANDARDAUTHENTICATIONUSERS: Komma getrennte Liste der Benutzer, welche trotz aktiviertem LDAP-Login mittels Standard 
  (cuba) Login authentifiziert werden. 
* **Konfiguration der URL des Schemareaders:**
  * SIMI_SCHEMAREADER_URL: Url, auf welcher der Schemareader erreichbar ist. Bsp: "http://localhost/schemareader"   
    **Hinweis:** Die Namen der Datenbanken in der SIMI-DB müssen mit dbs.key in der Schemareader-Konfig übereinstimmen.
* **Konfiguration der Suche in den GRETL-Repos (Anzeige der Abhängigkeiten):**
  * SIMI_GITSEARCH_URL: Url, auf welche die Git-Suchen abgesetzt werden. Bsp: "https://api.github.com/search/code"
  * SIMI_GITSEARCH_REPOS: Liste aller zu durchsuchenden Git-Repos, mittels "," getrennt. Bsp: "sogis/gretljobs,oereb/jobs"
* **Konfiguration Oauth-Tokenausgabe für die Publikations-Notifikation vom Publisher (GRETL)**
  * CUBA_REST_CLIENT_ID: Benutzername für die Tokenausgabe
  * CUBA_REST_CLIENT_SECRET: Passwort für die Tokenausgabe mit Präfix. Vor dem Passwort muss der **Präfix {noop}** stehen.
  * Siehe zur Service-Konfiguration auch das folgende Kapitel...
  
## Service zur Publikations-Notifikation durch den GRETL-Publisher

Für die Berechtigung zur Nutzung des Clients müssen zwei Konfigurationen vorgenommen werden:

* Setzen der beiden Umgebungsvariablen CUBA_REST_CLIENT_*
* Erstellung und Berechtigung des Benutzers "gretl" via Admin-GUI
  * Die Berechtigung muss beinhalten:
    * Die bestehende Rolle rest-api-access
    * Eine selbst erstellte Rolle für vollen Zugriff auf simiTheme_PublishedSubArea und lesenden Zugriff auf alle SIMI-Entitäten (Tabellen) im **SecurityScope "REST"**.

## Setzen des Loglevels

Der Loglevel kann bequem mittels der vom Framework zur Verfügung gestellten Admin-Masken zur Laufzeit geändert werden.

Falls eine Fragestellung vor der Verfügbarkeit der Admin-Masken analysiert werden muss, kann der Loglevel in der
Datei **\[repo root\]/webapp/docker/image/uber-jar-logback.xml** wie folgend beschrieben angepasst werden (Bedingt neuen Build des Image):

Anpassung des levels, welcher überhaupt auf die Konsole geschrieben wird:

    <root level="debug">
        <appender-ref ref="Console"/>
    </root>

Anpassung des log-levels für das Cuba-Framework:

    <logger name="com.haulmont.cuba" level="DEBUG"/>
    
Anpassung des log-levels für Simi:

    <logger name="ch.so.agi.simi" level="DEBUG"/>

## Erzeugen / Aktualisieren des Datenbankschemas

Siehe Ordner [schema](./schema)

## Entwicklungs-Dokumentation

Beschreibung der Inhalte der Unterordner von /webapp sowie des Custom-Codes siehe [Entwicklungs-Dokumentation](doc/development.md)

## Benutzer-Dokumentation

Diese ist im Dok-Repo zuhause: [Benutzer-Dokumentation](https://github.com/sogis/dok/blob/dok/dok_div_anleitungen/Documents/simi/simi_anleitung.md).