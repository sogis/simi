# Transformation (Trafo) sql2json

## Anforderungen

|ID|Anforderung|
|---|---|
|1|Es können mehrere mittels SQL-Abfrage ausgelesene Json-Fragmente in ein Json-Gerüstdokument integriert werden.|
|1.1|Das Gerüstdokument ist selbst ein valides Json-Dokument.|
|2|Im Gerüstdokument sind die Stellen, an denen ein Json-Fragment eingesetzt werden soll, mit einem entsprechenden "Json-Tag" gekennzeichnet.|
|2.1|Die folgenden "Json-Tags" sind vorhanden:<br>- SQL: Setzt das Resultat einer SQL-Abfrage als Json-Fragment in das Gerüstdokument ein.<br>- Globals: Setzt die entsprechende Globalvariable als Json-Fragment ein.|
|3|Implementation in Java|
|4|Packetierung als executable "fat-jar". Das jar umfasst sämtliche Abhängigkeiten auf Dritt-Bibliotheken|
|5|Es werden nur Standard-Bibliotheken oder "De-Fakto-Standard" Bibliotheken eingesetzt, da diese sehr zuverlässig und API-Stabil sind.|
|6|Das Generieren eines Json-Dokuments von 2 mb muss in unter einer Sekunde abgeschlossen sein. Ausgeklammert ist die benötigte Zeit für die Ausführung der 1-n Queries.|

## Spezifikation

Der sql2json Transformator arbeitet pro Programmaufruf ein Json-Template mit n im Template enthaltenen Trafo-Tags ab.
Für jedes Trafo-Tag setzt der Trafo ein Sql-Statement auf die Metadatenbank ab und ersetzt das Trafo-Tag mit dem Ergebnis
des SQL-Queries.

### Konfiguration des Trafo

Die "Globalkonfiguration" des Trafo erfolgt mittels Kommandozeilenparameter und/oder Umgebungsvariable:

|Bezeichnung|Parameter|Umgebungsvariable|Bemerkung|
|---|---|---|---|
|Template-Pfad|-t|SqlTrafo_Templatepath|Absoluter Dateipfad zum zu verarbeitenden Template. Bsp: opt/user/trafo/wms/template.json|
|Pfad zu Output|-o|SqlTrafo_Outputpath|Absoluter pfad und Dateiname des output config.json. Bsp: opt/user/trafo/wms/config.json|
|DB-Connection|-c|SqlTrafo_DbConnection|JDBC Connection-URL zur abzufragenden DB. Aufbau: jdbc:postgresql://host:port/database|
|DB-User|-u|SqlTrafo_DbUser|Benutzername für die DB-Verbindung|
|DB-Password|-p|SqlTrafo_DbPassword|Passwort für die DB-Verbindung|
|Log-Level|-l|SqlTrafo_LogLevel|Logging-Level: Silent, Info, Warn(ing), Debug. Geloggt wird auf den System-Errorstream|

### Trafo-Tag

Das Trafo-Tag ist ein Json-Objekt, dessen Name mit "$trafo:" beginnt: `{"$trafo:elem": "object.sql"}`

**Subtypen:**
* **"$trafo:elem":** Rendert ein einzelnes Json-Element in das Output-Json.
* **"$trafo:list":** Rendert eine Liste von Json-Elementen in das Output-Json.
* **"$trafo:set":** Rendert ein Set von Json-Objekten in das Output-Json.

"Element" ist der Sammelbegriff für Objekt, und "Einzelwert" wie z.B: string, number, boolean, null

**Datei mit SQL-Query**

Das für das Tag auszuführende Query wird im Trafo-Tag als String-Pfad übergeben. Der Pfad wird relativ
zum Template-Pfad aufgelöst, damit die Sql-Dateien auch in einem Unterverzeichnissen geordnet werden können.

Beispiel:
* Template befindet sich in opt/user/trafo/wms/template.json
* Sql befindet sich in opt/user/trafo/wms/sql/object.sql

--> Angabe der SQL-Datei im Trafo-Tag mittels "sql/object.sql"  

### Tag-Konfiguration für die Rückgabe eines Einzelnen Json-Elementes

**Template-Ausschnitt**

```json
{
	"fuu": "...",
	"layer": {"$trafo:elem": "object.sql"},
	"bar": "..."
}
```

**Query-Rückgabe**

|buz|
|---|
|"title": "Bezirksgrenzen", "visible": false|

**Json-Ausgabe**

```json
{
	"fuu": "...",
	"layer": {
		"title": "Bezirksgrenzen",
		"visible": false
	},
	"bar": "..."
}
```

Neben Objekten können auch einfache Strings zurückgegeben werden. Json-Ausgabe für Query-Resultat "ch.so.agi.gemeindegrenzen":

```json
{
	"fuu": "...",
	"layer": "ch.so.agi.gemeindegrenzen",
	"bar": "..."
}
```

### Tag-Konfiguration für die Rückgabe einer Objektliste (= Json-Array)

**Template-Ausschnitt**

```json
{
	"fuu": "...",
	"layers": {"$trafo:list": "objectlist.sql"},
	"bar": "..."
}
```

**Query-Rückgabe**

|buz|
|---|
|{"id": "ch.so.agi.gemeindegrenzen", "title": "Gemeindegrenzen", "visible": true }|
|{"id": "ch.so.agi.bezirksgrenzen", "title": "Bezirksgrenzen", "visible": false }|

**Json-Ausgabe**

```json
{
	"fuu": "...",
	"layers": [{
			"id": "ch.so.agi.gemeindegrenzen",
			"title": "Gemeindegrenzen",
			"visible": true
		},
		{
			"id": "ch.so.agi.bezirksgrenzen",
			"title": "Bezirksgrenzen",
			"visible": false
		}
	],
	"bar": "..."
}
```

### Tag-Konfiguration für die Rückgabe eines "Objekt-Sets"

**Template-Ausschnitt**

```json
{
	"fuu": "...",
	"layers": {"$trafo:set": "objectset.sql"},
	"bar": "..."
}
```

**Query-Rückgabe**

|buz|
|---|
|"ch.so.agi.gemeindegrenzen": {"title": "Gemeindegrenzen", "visible": true }|
|"ch.so.agi.bezirksgrenzen": {"title": "Bezirksgrenzen", "visible": false }|

**Json-Ausgabe**

```json
{
	"fuu": "...",
	"layers": {
		"ch.so.agi.gemeindegrenzen": {
			"title": "Gemeindegrenzen",
			"visible": true
		},
		"ch.so.agi.bezirksgrenzen": {
			"title": "Bezirksgrenzen",
			"visible": false
		}
	},
	"bar": "..."
}
```

### Fehlerbehandlung

Beim Auftreten eines Fehlers wird das Json-Tag mit dem Fehlertext "erweitert" und in das output-json geschrieben.
Zusätzlich wird ein umfangreicher Fehleroutput auf den Error-Stream geschrieben.

Bei einem Fehler bei der Verarbeitung eines Trafo-Tag wird die Verarbeitung der weiteren Tag's **nicht** abgebrochen.
Die auftretenden Fehler werden sequentiell ausgegeben. Bei einem Fehler gibt der gestartete Java-Prozess <> 0 
als exit value zurück.