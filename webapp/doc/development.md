# Entwicklungs-Dokumentation

## Abhängigkeiten

Abhängig von der Anpassung bestehen die folgenden Abhängigkeiten:

* Auf eine Datenbank mit korrektem Schema. Diese Abhängigkeit besteht immer, falls die Tabellen für SIMI fehlen,    
  werden diese automatisch beim Start des Cuba-Applikationsserver angelegt (Falls cuba.automaticDatabaseUpdate = true)
* Auf eine Datenbank mit "realistischen" Daten. Diese als Dump von der Integration/Produktion beziehen.
* Auf den Schemareader, welcher die Tabelleninformationen aus den Geodatenbanken ausliest.
  * Zu Entwicklungszwecken kann als Datenbank ein offizielles Postgis-Iamge verwendet werden. Dieses enthält im   
    Schema **"tiger"** Test-Geodaten. Das [docker-compose.yml](../devenv/docker-compose.yml) der Entwicklungsumgebung   
    startet einen Schemareader, welcher für den Datenbank-Identifier **"geodb"** auf die Postgis-Datenbank zeigt.
    
(Geo-)Tabellen im Schema "tiger" (für Postgis-Image 11-2.5-alpine):

* addr
* addrfeat
* bg
* county
* county_lookup
* countysub_lookup
* cousub
* direction_lookup
* edges
* faces
* featnames
* geocode_settings
* geocode_settings_default
* loader_lookuptables
* loader_platform
* loader_variables
* pagc_gaz
* pagc_lex
* pagc_rules
* place
* place_lookup
* secondary_unit_lookup
* state
* state_lookup
* street_type_lookup
* tabblock
* tract
* zcta5
* zip_lookup
* zip_lookup_all
* zip_lookup_base
* zip_state
* zip_state_loc 

## Entwicklungsablauf

Folgend in kurzen Sätzen ein typischer Entwicklungsablauf für eine funktionale Anpassung mit kleinem Impact (Es wird nur die Revision inkrementiert, von Beispielsweise V 1.1.34 auf V 1.1.35)

* Lokale Entwicklungs-Db starten (devenv/db_run.sh)
* Funktionale Anpassung umsetzen (In Cuba Studio)
  * Funktionalität erstellen / ändern
  * Auto. Test erstellen / ändern
* Gezielt Testen (automatisch und oder manuell)
* gradlew build (Wird in der github action in jedem Fall ausgeführt)
* commit / push
* Nach push wird automatisch
  * Gradle build ausgeführt (compile und unit tests)
  * Fatjar basiertes docker image erstellt
  * Integrationstests auf docker image ausgeführt
  * Neues image auf hub.docker.com gepushed
  * Von openshift das image auf test, int, prod deployt
* Nach ca. 30 Minuten verifizieren, dass neue Revision ausgerollt ist (z.B. mittels geo-i.so.ch/simi --> Menu SIMI Admin --> About --> Version)

### Dump aus Integration restoren

Periodisch macht es Sinn, die aktuellen Daten aus der Integration zu übernehmen.

Vorgehen:

* Dump lokal nehmen (Siehe dazu dok "Datenbanken")
* Dump restoren, ohne Übernahme von Ownern, Rollen, ... 
  * Schema:   
  ```pg_restore -d postgresql://postgres:postgres@localhost/simi -x -O --schema-only gitignored/simi_t_v1.2.dmp```
  * Daten:   
  ```pg_restore -d postgresql://postgres:postgres@localhost/simi -x -O --data-only --disable-triggers gitignored/simi_t_v1.2.dmp```  
* Anmelden mit dem Admin-Passwort "aus Dump" (Siehe Passwort-Manager)
  * Admin-Passwort überschreiben
  
## Abdeckung mit automatisierten Tests

Der Umfang der automatisierten Tests ist angepasst auf den Typ der entwickelten Funktionaliät. Standardfunktionalitäten des Frameworks wie Browse und Edit-Screens sind mit wenig Tests abgedeckt, während Beispielsweise bei Implementationen von SIMI-spezifischen Funktionen (Package ch.so.agi.simi.core.*) viel in Tests investiert wird. 
Die Integrationstests verwenden zur Laufzeit generierte spezifische Testdaten, welche nach Testabschluss wieder gelöscht werden.

Für das Laden und Speichern der Daten mittels Screens und Entities ist das Konzept der [View im Cuba-Framework](https://doc.cuba-platform.com/manual-latest/views.html) sehr zentral. Mittels der Testklasse webapp/modules/web/test/ch/so/agi/simi/web/beans/datatheme/SchemaReaderMock.java werden für alle Views die DB-Queries durchgeführt. Damit können Datenladefehler und indirekt Datenschreibfehler auf einfache Art und Weise minimiert werden.

Der Zugriff auf ein nicht geladenes Attribut (Spalte) einer Entity ist ebenfalls eine relativ häufige Fehlerquelle. Diese Fehlerquelle ist nicht mit Tests abgedeckt, da keine schlanke Testmethodik dafür gefunden werden konnte. 

## Package-Inhalte

Das Cuba Framework kann in verschiedensten Deployment-Architekturen betrieben werden. Der Sourcecode wird darum in den Modulen core, global und web deployt, siehe https://doc.cuba-platform.com/manual-7.2/app_modules.html (Spielt für uns untegeordnete Rolle, da alles in einem Image).

Die wichtigsten Packages von SIMI innerhalb Core Module:

* **ch.so.agi.simi.core**: Enthält in Unterpackages die Implementationen von funktionalen Interfaces (sogenannte Services). Beispielsweise die Ermittlung der abhängigen Konfigurationen und GRETL-Jobs einer Dataset-View oder Tabelle im Package ch.so.agi.simi.core.dependency

Innerhalb Global Module: 

* **ch.so.agi.simi.entity**: Enthält die Entities. Gegliedert nach den Teilmodellen (data, product, ...) des Metamodelles


Innerhalb Web Module:

* **ch.so.agi.simi.web.beans**: Enthält "clientseitige", in Beans gekapselte Funktionalitäten. Beispielsweise das Laden der Tabelleninformationen mittels Schemareader (Package ch.so.agi.simi.web.beans.dbSchema)
* **ch.so.agi.simi.web.screens**: Enthält die Definitionen der Browse- und Editscreens (GUI). Gegliedert nach den Teilmodellen (data, product, ...) des Metamodelles.

## Kopieren von Data-Products (Package ch.so.agi.simi.core.copy)

Die im GUI harmlos erscheinende Kopierfunktion ist aufgrund der vielen zu berücksichtigenden Beziehungen
ziemlich komplex.

Die Klassen innerhalb des Kopierkontextes werden kopiert (dupliziert), die ausserhalb werden referenziert. 
Nach dem Kopiervorgang zeigt also sowohl das Original wie auch die Kopie auf das gleiche ausserhalb des
Kopierkontextes liegende Objekt.

Hinweis: Die Vererbungen sind in den Diagrammen nicht abgebildet.

### Tableview

![Tableview Copy](res/copy-tableview.png)

### Rasterview

![Rasterview Copy](res/copy-rasterview.png)

### FacadeLayer

![Facadelayer Copy](res/copy-facadelayer.png)

### Layergroup

![Layergroup Copy](res/copy-layergroup.png)

### Map

![Map Copy](res/copy-map.png)

## Cuba API Javadoc

Diese ist nicht ganz einfach zu finden, darum Link hier: https://files.cuba-platform.com/javadoc/cuba/7.2/
