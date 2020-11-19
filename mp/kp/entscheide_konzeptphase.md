
# Konzeptionelle Entscheide

## K1 Einsatz / Konfiguration der cuba-platform

* **Vererbungsarten:** Es wird pro Vererbungsbaum festgelegt, welche Vererbungsstrategie umgeseetzt wird. 
Wenn die Kinder Beziehungen besitzen, wird die Vererbungsart "joined" verwendet, damit klar ersichtlich ist, 
welche Kinder in welcher Beziehung teilnehmen.
* **Einsatz von View's:** Für die Darstellung zusammengezogener Informationen (etwa: Know your GDI - "Report") werden
DB-Views verwendet. Beobachten, ob dies im Zusammenhang mit Schemaänderungen mühsam wird (Nein gemäss Haulmont). 
* **"Dateien":** Diese werden direkt in ihrem "nativen" Format als Spalte in der entsprechenden Tabelle gehalten.
--> QML als String, Legende als byte[]. Die Benutzer merken davon nicht's - sie laden Dateien hoch und runter.
Verortung in der Tabelle ist bei den Dateigrössen und -Mengen von Simi absolut problemlos und bringt den Vorteil, 
dass man sich nicht noch um irgendwelche externe Referenzen kümmern muss.
* **Rollen und Screens:** Die technischen Konfigurationen auf von den GIS-Koordinatoren genutzten Masken sind readonly zu gestalten.
Es wird die einfachste Lösung verfolgt: Eine Maske für AGI und GIS-Koordinatoren, die technischen Felder sind für die GIS-Koordinatoren "readonly".
Die GIS-Koordinatoren können nicht mit QGIS 2.18 Darstellungen erstellen. Entsprechend wird der Zugang und die Permission-Konfiguration für die GIS-Koordinatoren vertagt (out of project scope).
* **Softdelete:** Dieses wird als Standardverhalten der Plattform übernommen. Vorteil: "Geschenkte" Informationen bezüglich Zeitpunkt und Nutzer der letzten Änderung
(Insert, update UND delete). Nachteil: Die "soft-deleted" Records müssen periodisch gelöscht werden --> GRETL Job.

## K2 Transformation sql2json und json2qgs

|Thema|Entscheid|Bemerkungen|
|---|---|---|
|Transformation mittels SQL-CTE?|Ja. Pilotierung mit Benutzer und Gruppen der Config-DB hat gezeigt, dass dies ein guter Weg ist.|Fraglich ist, ob es SQL-Fragmente gibt, welche X-Mal verwendet werden --> Ist während Erstellung der SQL CTE's zu beobachten.|
|Ablauf / Abhängigkeit Trafo-SQL's von Trafo-Modul|1. Erstellen der SQL's<br>2. Erstellen Modul 3. Ausführen der SQL's mittels Modul.|Agiles Vorgehen. Erst beim Schreiben der SQL's wird klar, wo es SQL-Codeverdoppelungen gibt und wie diese am Besten "ausgemerzt" werden.|
|Binärdateien: Datenhaltung in SIMI|Die Originale werden als Blobs in der Simi-DB gehalten.|Vorteil: Aufgeräumt. Kopieren braucht nur den Dump. Es geht nur um kleine Dateien - kein Problem für die DB.|
|Binärdateien: Export|Als Transfer-Codierung für binäre Informationen wird base64 verwendet und in die *.json als einfache Strings integriert.|Falls QGIS Server 2.18 wird das das QML mit zugehörigen Assets als "String of Json" in der Datenbank gespeichert, um einfachen Export sicherzustellen.|
|XML-Dateien: Datenhaltung und Export|XML-Dateien werden in SIMI als String(unbounded) gehalten und analog den Binärdateien als base64 codierte Strings in die *.json integriert.|
|Globals|Globale Informationen, welche in mehreren *.json genutzt werden: Diese werden in SIMI im Modul "globals" geführt, damit die Informationen einfach für sql2json zur Verfügung stehen.|

Todo: Fileupload QML+Assets für 2.18 überlegen
* Transparente Datenhaltung in SIMI
* Einfacher Export mittels sql (für sql2json)
* Einfach Bedienung von Simi bei mehreren Dateien (kein zippen....?)

Upload und Download von immer einer Datei
*.qml, falls nur ein qml hochgeladen wurde
*.zip, falls zum qml assets vorhanden sind

Tabellenfelder:
qml
qml_assets

Funktionen des Beans
* Tuple<String, Optional<String>> styleFileToStorage(File uploadedFile, String qmlVersion)
* File storageToStyleFile(String qml, String assets)
* String styleStorageInfo(String qml, String assets)

Tests:
* Falsche qmlVersion --> Exception
* qmlOnly
* Flaches zip
* Hierarchisches zip
* zip upload ohne assets --> Exception
* zip upload ohne qml --> Exception
* download ohne assets --> xy.qml
* download mit assets --> valides zip
* duplicate asset name --> exception
* grosses qml mit vielenAssets --> OK

```json
{
	"assets": [{
			"blubb.png": "asdfölasdfjk"
		},
		{
			"blubb.png": "asdfölasdfjk"
		}
	]
}
```


## K3 Bedienungserleichterungen SIMI
  
|Thema|Entscheid|Bemerkungen|
|---|---|---|   
|Mutation|Nicht umsetzen|Trotz mehrerer Anläufe konnte keine Variante gefunden werden, bei welcher der Ertrag der Mutationsunterstützung den Implementationsaufwand rechtfertigt. Der Aufwand ist insbesondere aufgrund der vielen zu berücksichtigenden Beziehungen gross.|
|Kopieren|Soll umgesetzt werden|Mit der Möglichkeit, Layergruppen, Facadelayer und DataSetViews zu kopieren wird der Bearbeitungsaufwand deutlich reduziert (Geschützte Ebenen, ...).|
|Kartennachführung|Erfahrungen in der Nutzung abwarten|Bezüglich Kartennachführung müssen zuerst Erfahrungen mit den neuen Masken vorliegen, bevor eine effektive Unterstützung definiert werden kann.|

## K4 Config-Pipeline

Todo:
* Integration SIMI - Jenkins
* Äusserste Schale als Config --> Triggert reboot der betroffenen Container, ohne Zeitaufwand für Image build.
* Nicht aus SIMI generierte *.json werden direkt als Datei gepflegt und liegen im Pipeline-Repo vor.

## K5 ModelReader

|Thema|Entscheid|Bemerkungen|
|---|---|---|
|Auslesen der Informationen aus Geo-DB's / Ili-Repo|Das Auslesen erfolgt ausschliesslich mittels SQL auf die Geo-DB's|Voraussichtlich wird für den Datenbezug lediglich der Modellname benötigt. Dieser kann ebenfalls mittels SQL aus t_ili2db_model ausgleesen werden.|
|Umgang mit nicht vorhandenen Informationen|Diese werden explixit als json:null in der Response zurückgegeben.|API-Klarheit ist (hier) wichtiger als die Transfergrösse.|
|Benennung der Komponente|Neu: SchemaReader|Die Komponente liest grossmehrheitlich Informationen aus einem Schema von PostgreSQL aus und heisst darum folgerichtig **Schemareader**.| 
|Quarkus oder Boot?|Spring Boot|Obwohl Umfang und Anforderungen sehr gut auf ein "Microservice-Framework" passen - "Frameworkitis" vermeiden --> Spring Boot|
|Endpunkte|Der Schemareader wird zwei Endpunke umfassen. Erster Endpunkt zum Suchen von Tabellen, zweiter zur Anzeige der Detailinformationen zu einem Endpunkt.|

## K6 GRETL-Endpoint

Siehe Ergebnis morgen

## K7 - DataProduct-Service

Reduktion auf zwingnde Informationen
Beibehaltung der Signatur

  

 





