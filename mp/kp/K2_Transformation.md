# K2 - Transformation 

## Umsetzungsfragen

Erstellung eines config.json mittels SQL-CTE
* Wahl des geeigneten Teilmodelles
    * Muss genügend komplex sein.
    * Modellierung soll zwischen AGDI und SIMI nicht zu stark
abweichen.
* Erstellung des CTE, basierend auf der heutigen Config-DB.

Erarbeitung der Umsetzungsentscheide für das Transformations-Modul
* Entscheid, ob die Transformation mittels SQL-CTE erfolgen soll
* Entscheid, wie aufgrund der SQL-Informationen das QML erstellt
wird.
* Entscheid, wie zukünftig die dateibasierten Konfigurationen
gehalten werden (Legendenbilder, …)

## Entscheide

|Thema|Entscheid|Bemerkungen|
|---|---|---|
|Transformation mittels SQL-CTE?|Ja. Pilotierung mit Benutzer und Gruppen der Config-DB hat gezeigt, dass dies ein guter Weg ist.|Fraglich ist, ob es SQL-Fragmente gibt, welche X-Mal verwendet werden --> Ist während Erstellung der SQL CTE's zu beobachten.|
|Ablauf / Abhängigkeit Trafo-SQL's von Trafo-Modul|1. Erstellen Modul V 1.0<br<2. Erstellen der SQL's<br>3. Erstellen Modul V 1.1|Benötigt "agiles" Vorgehen. Erst beim Schreiben der SQL's wird klar, wo es SQL-Codeverdoppelungen gibt und wie diese am Besten "ausgemerzt" werden.|
|Dateibasierte Konfigurationen: Originaldatenhaltung|Die Originale werden als Blobs in der Simi-DB gehalten.|Vorteil: Aufgeräumt. Kopieren braucht nur den Dump. Es geht nur um kleine Dateien - kein Problem für die DB.|
|Dateibasierte Konfigurationen: Export|Es braucht ein Trafo-Element, welches die Blobs wiederum als Datei exportiert (vs. Kopieren von nach).|Bezüglich QGIS-Server die Möglichkeit der Zip-Erstellung beachten (*.qgz)|
 
