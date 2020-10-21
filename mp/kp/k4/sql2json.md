# Transformation sql2json

## Anforderungen

|ID|Anforderung|
|---|---|
|9|Es können mehrere mittels SQL-Abfrage ausgelesene Json-Fragmente in ein Json-Gerüstdokument integriert werden.|
|9.1|Das Gerüstdokument ist selbst ein valides Json-Dokument.|
|10|Im Gerüstdokument sind die Stellen, an denen ein Json-Fragment eingesetzt werden soll, mit einem entsprechenden "Json-Tag" gekennzeichnet.|
|10.1|Die folgenden "Json-Tags" sind vorhanden:<br>- SQL: Setzt das Resultat einer SQL-Abfrage als Json-Fragment in das Gerüstdokument ein.<br>- Globals: Setzt die entsprechende Globalvariable als Json-Fragment ein.|
|99|Implementation in Java|
|12|Packetierung als executable jar. Das jar umfasst sämtliche Abhängigkeiten auf dritt|
|12.1|Nach Möglichkeit neben dem postgres-Treiber keine weiteren externen Abhängigkeiten nutzen (Standartbibliothek einsetzen).|
|99|Dem sql2json.jar werden wie folgenden Parameter übergeben:<br>- Pfad zum Json-Gerüstdokument<br>- Pfad zum "Resultat-Json"<br>- Verbindungsparameter zur Metadatenbank<br>- Pfad zum Ordner mit den *.sql Dateien
|11|Das Generieren eines Json-Dokuments von 2 mb muss in unter einer Sekunde abgeschlossen sein. Ausgeklammert ist die benötigte Zeit für die Ausführung der 1-n Queries.|

## Ideen

* Tags werden als Json-Objekte codiert. A la:
    * SQL-Array: `{"$sql:array": "permission.sql"}` permission.json ist der Dateiname der sql-Abfrage
    * SQL-Value:`{"$sql:value": "permission.sql"}` permission.json ist der Dateiname der sql-Abfrage
    * Globals-Tag: `{"$sql:globals": "wgc.scales"}` wgc.scales ist der Key der abzufragenden Global-Variable
* Zwecks Unterscheidung von "normalen" Json-Objekten wird der Pseudo-Namensraum "$sql:" für die Tags gesetzt. 

Erweiterung SQL-Array:

```json
{
	"$sql:array": {
		"query": "permission.sql",
		"rowtemplate": {
			"permissions": {
				"wms_services": [{
					"name": "somap",
					"layers": [{
							"name": "somap"
						},
						{
							"$sql:field": "wms_layer_with_attributes"
						}
					]
				}],
				"wfs_services": [{
					"name": "somap",
					"layers": [{
						"$sql:field": "wfs_layer_with_attributes"
					}]
				}]
			}
		}
	}
}
```
    
### Beispiel

#### Gerüstdokument:

```json
{
  "trafos": {"$sql:value": "trafos.sql"},
  "roles": {"$sql:array": "roles.sql"},
  "screenres": {"$sql:globals": "wgc.screenres"},
  "browsers": ["firefox", 
    {"$sql:value": "chromium_version.sql"}
  ]
}
```

#### Resultat Json nach Transformation

```json
{
  "trafos": {
    "sql2json": "slick",
    "json2qml": "cool"
  },
  "roles": [
    "public",
    "agdi_admin"
  ],
  "screenres": 92,
  "browsers": [
    "firefox", 
    "chromium v52"
  ]
}
```

#### Zurückgelieferte Werte des Beispiels

|Tag|Wert(e)|
|---|---|
|`{"$sql:value": "trafos.sql"}`|Resultset mit zwei Records mit einer Spalte:<br>"sql2json": "slick"<br>"json2qml": "cool"|
|`{"$sql:array": "roles.sql"}`|Resultset mit zwei Records mit einer Spalte:<br>"public"<br>"agdi_admin"|
|`{"$sql:globals": "wgc.screenres"}`|92|
|`{"$sql:value": "chromium_version.sql"}`|Resultset mit einem Record und einer Spalte:<br>chromium v52|

`{"$sql:value": "..."}` Umschliesst also die Rückgabe mit {}, falls mehr wie ein Record zurückgegeben wird.














