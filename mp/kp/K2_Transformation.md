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
|Ablauf / Abhängigkeit Trafo-SQL's von Trafo-Modul|1. Erstellen der SQL's<br>2. Erstellen Modul 3. Ausführen der SQL's mittels Modul.|Agiles Vorgehen. Erst beim Schreiben der SQL's wird klar, wo es SQL-Codeverdoppelungen gibt und wie diese am Besten "ausgemerzt" werden.|
|Dateibasierte Konfigurationen: Originaldatenhaltung|Die Originale werden als Blobs in der Simi-DB gehalten.|Vorteil: Aufgeräumt. Kopieren braucht nur den Dump. Es geht nur um kleine Dateien - kein Problem für die DB.|
|Dateibasierte Konfigurationen: Export|Als Transfer-Codierung für binäre Informationen wird base64 verwendet und in die *.json als einfache Strings integriert.|Falls QGIS Server 2.18 wird das das QML mit zugehörigen Assets als "String of Json" in der Datenbank gespeichert, um einfachen Export sicherzustellen.|
|Globals|Globale Informationen, welche in mehreren *.json genutzt werden: Diese werden in SIMI im Modul "globals" geführt, damit die Informationen einfach für sql2json zur Verfügung stehen.|

 
