
# Konzeptionelle Entscheide

## K1 Einsatz / Konfiguration der cuba-platform

* **Vererbungsarten:** Es wird pro Vererbungsbaum festgelegt, welche Vererbungsstrategie umgesetzt wird. 
Wenn die Kinder Beziehungen besitzen, wird die Vererbungsart "joined" verwendet, damit klar ersichtlich ist, 
welche Kinder in welcher Beziehung teilnehmen.
* **Einsatz von View's:** Für die Darstellung zusammengezogener Informationen (etwa: Know your GDI - "Report") werden
DB-Views verwendet. Beobachten, ob dies im Zusammenhang mit Schemaänderungen mühsam wird (Nein gemäss Haulmont). 
* **"Dateien":** Diese werden direkt in ihrem "nativen" Format als Spalte in der entsprechenden Tabelle gehalten.
--> QML als String, Legende als byte[]. Die Benutzer merken davon nicht's - sie laden Dateien hoch und runter.
Verortung in der Tabelle ist bei den Dateigrössen und -Mengen von Simi absolut problemlos und bringt den Vorteil, 
dass man sich nicht noch um irgendwelche externen Referenzen kümmern muss.
* **Rollen und Screens:** Die GIS-Koordinatoren können nicht mit QGIS 2.18 Darstellungen erstellen. Entsprechend wird der Zugang und die Permission-Konfiguration für die GIS-Koordinatoren vertagt (out of project scope).
* **Softdelete:** Dieses wird als Standardverhalten der Plattform übernommen. Vorteil: "Geschenkte" Informationen bezüglich Zeitpunkt und Nutzer der letzten Änderung
(Insert, update UND delete). Nachteil: Die "soft-deleted" Records müssen periodisch gelöscht werden --> GRETL Job.
* **IAM:** Zwecks Prüfung der Identität der Benutzer wird SIMI an das LDAP angebunden. Ein Single-Sign-On / SES-Integration wird nicht umgesetzt.

## K2 Transformation sql2json und json2qgs

|Thema|Entscheid|Bemerkungen|
|---|---|---|
|Transformation mittels SQL-CTE?|Ja. Pilotierung mit Benutzer und Gruppen der Config-DB hat gezeigt, dass dies ein guter Weg ist.|Fraglich ist, ob es SQL-Fragmente gibt, welche X-Mal verwendet werden --> Ist während Erstellung der SQL CTE's zu beobachten.|
|Erstellungsabhänggikeit der Trafo-SQL's und des Tools "sql2json"|1. Erstellen der SQL's für Permissions und ogc-Service<br>2. Entscheid "sql2json" mit Templating Ja / Nein<br>3. u. 4. Erstellen der restlichen SQL's. Erstellung des Tools "sql2json"|Agiles Vorgehen. Erst beim Schreiben der SQL's wird klar, wo es SQL-Codeverdoppelungen gibt und wie diese am Besten "ausgemerzt" werden.|
|Binärdateien: Export|Als Transfer-Codierung für binäre Informationen wird base64 verwendet und in die *.json als einfache Strings integriert.|Da weiterhin QGIS Server 2.18 werden die qml und die zum QML gehörenden png's, svg's in den *.json in einer Json-Struktur zusammengefasst. Die Stuktur wird in der Spezifikationsphase des Tools "json2qgs" bestimmt.|
|XML-Dateien: Datenhaltung und Export|Werden wie Binärdateien (*.png, ...) als bytea gehalten|Ausnahme: Das QML wird als String(unbounded) gehalten. Grund: Transparenz / Einfache Einsehbarkeit bei QML-bezogenen Fragen im Betrieb.|
|Globals|Globale Informationen, welche in mehreren *.json genutzt werden: Diese werden als basis-view bereitgestellt.|Falls nicht anders machbar kann nachträglich in SIMI ein Modul "Globals" erstellt werden. In diesem können Metainformationen für etwa den WMS getcapabilities request gepflegt werden.|

## K3 Bedienung SIMI
  
|Thema|Entscheid|Bemerkungen|
|---|---|---|   
|Mutation|Nicht umsetzen|Trotz mehrerer Anläufe konnte keine Variante gefunden werden, bei welcher der Ertrag der Mutationsunterstützung den Implementationsaufwand rechtfertigt. Der Aufwand ist insbesondere aufgrund der vielen zu berücksichtigenden Beziehungen gross.|
|Kopieren|Soll umgesetzt werden|Mit der Möglichkeit, Layergruppen, Facadelayer und DataSetViews zu kopieren wird der Bearbeitungsaufwand deutlich reduziert (Geschützte Ebenen, ...).|
|Kartennachführung|Erfahrungen in der Nutzung abwarten|Bezüglich Kartennachführung müssen zuerst Erfahrungen mit den neuen Masken vorliegen, bevor eine effektive Unterstützung definiert werden kann.|
|Kontakte|Werden im Projektrahmen nicht in SIMI integriert.|Grund: Die heute im AGDI zugeordneten Kontakte lassen sich zu 95% aus dem zuständigen Amt automatisch herleiten. Die Kontaktinformationen werden erst mit der Umsetzung des Projektes "Datenbezug" überhaupt irgendwo dargestellt --> Im Scope des Projektes "Datenbezug" umsetzen.|

## K4 Config-Pipeline

* **Integration SIMI - Jenkins:**
  * Zwecks maximaler Nutzung der Standardfunktionalität von Jenkins wird wie folgt integriert:
  * Simi stösst die Config-Pipeline mittels Jenkins-API-Aufruf mit Security-Token an.
  * Simi öffnet gleich anschliessend die Benutzeroberfläche des entsprechenden Jobs in Jenkins mittels URL-Aufruf. 
  Im Jenkins-GUI kann der Benutzer den Fortschritt der Pipeline sehen / überwachen. Dazu wird anonymer lesender Zugriff für "Jedermann" auf das GUI des "Pipeline-Jenkins" aktiviert..
* Falls die "Image-Brennzeit" nicht reduziert werden kann, werden die *.json als Ressorcen gemountet. 
* Nicht aus SIMI generierte *.json werden direkt als Datei gepflegt. Die Originale liegen im Pipeline-Repo.

## K5 ModelReader (SchemaReader)

|Thema|Entscheid|Bemerkungen|
|---|---|---|
|Auslesen der Informationen aus Geo-DB's / Ili-Repo|Das Auslesen erfolgt ausschliesslich mittels SQL auf die Geo-DB's|Voraussichtlich wird für den Datenbezug lediglich der Modellname benötigt. Dieser kann ebenfalls mittels SQL aus t_ili2db_model oder t_ili2db_trafo ausgleesen werden.|
|Umgang mit nicht vorhandenen Informationen|Diese werden explixit als json:null in der Response zurückgegeben.|API-Klarheit ist (hier) wichtiger als die Transfergrösse.|
|Benennung der Komponente|Neu: SchemaReader|Die Komponente liest grossmehrheitlich Informationen aus einem Schema von PostgreSQL aus und heisst darum folgerichtig **Schemareader**.| 
|Quarkus oder Boot?|Spring Boot|Obwohl Umfang und Anforderungen sehr gut auf ein "Microservice-Framework" passen - "Frameworkitis" vermeiden --> Spring Boot|
|Endpunkte|Der Schemareader wird zwei Endpunke umfassen. Erster Endpunkt zum Suchen von Tabellen, zweiter zur Anzeige der Detailinformationen zu einem Endpunkt.|

## K6 GRETL-Endpoint

Der Informationsgehalt der Schnittstelle zwischen GRETL und SIMI wird reduziert. Der Aspekt "Know Your GDI" wird neu
mittels Such-API von Github abgedeckt (etwas weniger Benutzerfreundlich, aber deutlich einfacher). Damit gibt es im Projektrahmen
"Metadatenpflege" keine Schnittstelle zwischen GRETL und SIMI.

**Ausblick auf Projekt "Datenbezug":**

Im Projektrahmen Datenbezug muss mittels Schnittstelle zwischen GRETL und SIMI der Zeitpunkt des letzten Ausführens eines Publikationsjobs
SIMI mitgeteilt werden.

Varianten:
* Favoritisiert: GRETL sendet nach erfolgreichem Abschluss eines Build mittels HTTPS die notwendigen Informationen an SIMI (SIMI ist Server).
  Im GRETL wird die Datenbank des aktualisierten Schemas und optional der Name des Schemas aus dem job.properties gelesen.
  Liste aller an SIMI übermittelten Informationen:
  * Name des aktualisierten Schemas
  * Datenbank des Schemas (Edit, Pub, ...)
  * Timestamp der Aktualisierung (Kann auch Startzeit des Build sein - so genau kommt es nicht darauf an)
  * Optional: CRON-Einstellungen
* Fallback-Varianten:
  * GRETL stösst SIMI lediglich mit dem Jobnamen an (nach erfolgreichem Build). Als zweiter Schritt frägt SIMI mittels
  Jenkins-API die Informationen von Jenkins ab (Also zwei Requests: GRETL --> SIMI, dann SIMI --> GRETL)
  * SIMI frägt jeweils am Morgen um 5 Uhr mittels Jenkins-API alle Jobs ab, und überträgt die Zeitpunkte des letzten
  erfolgreichen build ins SIMI. Potentiell müsste dafür SIMI einen Request pro Job machen, da die Informationen nicht 
  anders zu holen sind.

## K7 - DataProduct-Service

Der DataProduct-Service wird von SO-Locator und Web GIS Client für unterschiedliche Zwecke verwendet:
* Web GIS Client:    
  * Abfragen der Metainformationen - "(i) - Knopf"
  * Laden einer neuen Ebene
  
* SO-Locator
  * Laden einer neuen Ebene
  
Er umfasst viele zum heutigen Zeitpunkt von keinem Client verwendete Informationen:
* Kontaktangaben zu einem Datensatz
* Keywords, Synonyme
* ...

Der weder von SO-Locator noch Web GIS Client benötigten Informationen werden im entsprechenden
config.json mit Dummy-Werten befüllt. Damit ist für allfällige spätere Anpassungen klar,
welche Teile des Service effektiv genutzt werden.

Im Projektrahmen wird der DataProduct-Service funktional **nicht** verändert.

  

 





