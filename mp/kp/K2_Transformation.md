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

## XML in JSON

### Resultat ohne base64

```sql
WITH 

xmlnote AS (
SELECT '<note type="small">
<to>Tove</to>
<from>Jani</from>
<heading>Reminder</heading>
<body>Dont forget me this weekend!</body>
</note>' AS note
)

SELECT 
	to_json(note) AS encoded, 
	json_build_object('note', note) AS obj, 
	json_each_text(json_build_object('note', note)) AS encoded_decoded  
FROM xmlnote
 ```

```
Name           |Value                                                                                                                                                |
---------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
encoded        |"<note type=\"small\">\n<to>Tove</to>\n<from>Jani</from>\n<heading>Reminder</heading>\n<body>Dont forget me this weekend!</body>\n</note>"           |
obj            |{"note" : "<note type=\"small\">\n<to>Tove</to>\n<from>Jani</from>\n<heading>Reminder</heading>\n<body>Dont forget me this weekend!</body>\n</note>"}|
encoded_decoded|(note,"<note type=""small"">¶<to>Tove</to>¶<from>Jani</from>¶<heading>Reminder</heading>¶<body>Dont forget me this weekend!</body>¶</note>")         |
```


## Offene Fragen (Sind bis zur Finalisierung des Lastenheftes "QGS-Trafo" zu klären)
* Welche Json-Konfigurationen haben eine Abhängigkeit zu welchen Assets? Wie ist dies zu lösen?
  * Die Assets werden im Rahmen des Auftrages als base64 in das entsprechende *.json codiert und von der Komponente aus dem *.json ausgelesen.
* Sind die Abhängigen Assets als Blobs in der Config-DB enthalten, oder in einen verwalteten Ordnerbaum enthalten?
* Da innerhalb des qmlContent.json definiert, ist das qml auch so etwas wie ein Asset. Wie ist dies zu lösen (xml in json)? base64?
    * Wir hatten glaub's einen ähnlichen Fall beim Featureinfo. Oder war es dort Json in XML?
* Tabellen ohne Geometrie müssen / dürfen nicht in das QGS - korrekt?
  *

## Entscheide

|Thema|Entscheid|Bemerkungen|
|---|---|---|
|Transformation mittels SQL-CTE?|Ja. Pilotierung mit Benutzer und Gruppen der Config-DB hat gezeigt, dass dies ein guter Weg ist.|Fraglich ist, ob es SQL-Fragmente gibt, welche X-Mal verwendet werden --> Ist während Erstellung der SQL CTE's zu beobachten.|
|Ablauf / Abhängigkeit Trafo-SQL's von Trafo-Modul|1. Erstellen der SQL's<br>2. Erstellen Modul 3. Ausführen der SQL's mittels Modul.|Agiles Vorgehen. Erst beim Schreiben der SQL's wird klar, wo es SQL-Codeverdoppelungen gibt und wie diese am Besten "ausgemerzt" werden.|
|Dateibasierte Konfigurationen: Originaldatenhaltung|Die Originale werden als Blobs in der Simi-DB gehalten.|Vorteil: Aufgeräumt. Kopieren braucht nur den Dump. Es geht nur um kleine Dateien - kein Problem für die DB.|
|Dateibasierte Konfigurationen: Export|Als Transfer-Codierung für binäre Informationen wird base64 verwendet und in die *.json als einfache Strings integriert.|Falls QGIS Server 2.18 wird das das QML mit zugehörigen Assets als "String of Json" in der Datenbank gespeichert, um einfachen Export sicherzustellen.|
|Globals|Globale Informationen, welche in mehreren *.json genutzt werden: Diese werden in SIMI im Modul "globals" geführt, damit die Informationen einfach für sql2json zur Verfügung stehen.|

 
