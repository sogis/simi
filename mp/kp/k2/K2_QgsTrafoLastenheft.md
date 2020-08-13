# Lastenheft QgsTrafo

## Überblick und Einbettung in das Gesamtsystem

![Komponenten](../../puml/rendered/components.png)

Relevant für das Verständnis des Auftrages ist das package "Transformers":

Die Komponenten der GDI werden mittels json-Dateien konfiguriert. Die Json's werden mittels des Transformers "JsonTrafo" erzeugt. 
Der JsonTrafo integriert mittels templating SQL-Abfrageresultate in ein Json-Gerüst und gibt das resultierende Json aus.
Für den QGIS-Server wird ein XML-basiertes *.qgs (*.qgz) benötigt, in welchem Datenquelle, Whereclause, Darstellung, Gruppierung, ... 
der Ebenen abgebildet ist. Das *.qgs wird aus dem im Rahmen dieses Auftrages zu spezifizierenden "qgsContent.json" erzeugt.

```
Aufgrund Anforderung 1 sollte der Input Json sein. 
```

Die bestehenden Json-Konfigurationen werden im Rahmen dieses Auftrages einzig bezüglich der Assets angepasst. 
Diese sollen base64-Codiert direkt innerhalb der Json-Dateien an die Komponenten übergeben werden können.

Des Weiteren wird im Rahmen des Aufrages geklärt, welche Informationen aus dem DataProduct-Service vom Web GIS Client
wozu genutzt werden.

Der JsonTrafo wird vom AGI erstellt und ist nicht Teil des Auftrags.

## Anforderungen an QgsTrafo
|ID|Anforderung|
|---|---|
|1|Die vom QgsTrafo eingelesene Datei muss json sein, damit in den vorgelagerten SQL-Abfragen die mächtigen Json-Funktionen von postgreSQL genutzt werden können.|
|2|Annahme zu den Assets zu einem *.qml: Diese sind in den zu verarbeitenden *.qml bereits base64-Codiert enthalten.|
|3|Die grundsätzliche Funktionsweise des bestehenden "Magicbutton" wird beibehalten (Jinja-Template, Default-QGS, ...).|
|4|Als Erweiterung wird für Vektorebenen eine optionale Whereclause in das qgsContent.json integriert. Diese muss an den korrekten Stellen jeweils im *.qgs eingefügt werden.|
|5|Konfiguration: Der QgsTrafo wird mittels Kommandozeilenaufruf gestartet. Im Aufruf werden die folgenden Parameter übergeben:<br>- Pfad zur QGS-Template Datei<br>- Pfad zu "qgsContent.json"<br>- Pfad zu den Default-Qml<br>- Pfad, unter welchem das erzeugte QGS abgelegt wird<br>- Der Loglevel. Sofern nicht angegeben kommt der Default-Loglevel "Info" zum Einsatz.|
|6|Logging: Bei Loglevel "Info" wird für jede Verarbeitung eines Jinja-Templates 1-2 Logs ausgegeben. Bei Debug zusätzlich 1-2 Logs für die Verarbeitung jedes Layers (Sowohl für die Gruppen, wie auch die Einzellayer).|
|7|Performance: Ein qgsContent.json mit 300 "real World" Ebenen muss in unter 5 Sekunden in das resultierende *.qgs generiert werden.|
|8|Ablage des Sourcecode: OpenSource auf Github.|
|9|Dokumentation: als *.md im Repo. Umfasst Kapitel zur Installation, Programmausführung, und die Schrittweise Anleitung bei Migration auf eine höhere QGIS Server Version.| 
|10|Bereitstellung: Shell oder Python-Script, welches das Kommandozeilenprogramm inklusive seiner Abhängigkeiten in einer pyvenv installiert (mittels requirements.txt).|

## Vorgehen und Termine

Vor der Implementation wird die Struktur des qmlContent.json in zwei Schritten festgelegt:
* Entwurf: Die Struktur des entstehenden Json richtet sich einzig nach dem zu transportierenden Informationsgehalt. 
    * Er berücksichtigt keinerlei "Besonderheiten" der produzierenden und konsumierenden Seite (JsonTrafo, QgsTrafo)
    * Optionale, aber für einen Layer nicht vorhandene Attribute, werden explizit mit `null` deklariert. Beispiel: `"myOptionalValue" : null` 
* Im Rahmen einer Feedbackrunde (Halbtägiger Workshop) wird besprochen, ob und wie in Einzelfällen die Struktur    
nach den Bedürfnissen von QgsTrafo und/oder JsonTrafo angepasst werden kann.

### Abgabetermin

Wunschtermin Ende Oktober. Allerspätestens Ende November 2020.

## Offene Fragen (Sind bis zur Finalisierung des Lastenheftes zu klären)
* Welche Json-Konfigurationen haben eine Abhängigkeit zu welchen Assets? Wie ist dies zu lösen?
* Sind die Abhängigen Assets als Blobs in der Config-DB enthalten, oder in einen verwalteten Ordnerbaum enthalten?
* Da innerhalb des qmlContent.json definiert, ist das qml auch so etwas wie ein Asset. Wie ist dies zu lösen (xml in json)? base64?
    * Wir hatten glaub's einen ähnlichen Fall beim Featureinfo. Oder war es dort Json in XML?
* Tabellen ohne Geometrie müssen / dürfen nicht in das QGS - korrekt?

Fragen intern: Wie machen wir jeweils das Anheben der qml? Dies ist nicht Teil des Projektes "Metadatenpflege"

## Idee für Fallback, sofern die Assets nicht im jeweiligen QML enthalten sind:

Universell mit vollem relativem Pfad gemäss vom Benutzer hochgeladenem zip:

```json
{
    "..." : "...",
	"assets": [{
		"fullPath": "fillpattern/myCrossPattern.svg",
		"base64": "as0987sdf"
	}, {
		"fullPath": "symb/grosseSchuettung.png",
		"base64": "mnxbcv709870"
	}]
}
```
