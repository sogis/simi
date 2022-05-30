# SubArea (Theme)

Dokumentiert die Klassen des Package Theme.SubArea (SIMI) und die Zusammenhänge zum Modell "SO_AGI_Meta_Datenabdeckung_YYYYMMDD" im Schema agi_data_coverage auf der Edit-DB.

![DataCoverage](resources/theme/subarea.png)

## Klasse PublishedSubArea

Mit dem "LastPublished date" wird in dieser Klasse gespeichert, wann ein Teilgebiet einer Themenpublikation das letzte Mal publiziert wurde. LastPublished wird für Vektordaten via GRETL-Publisher geschrieben.

### Attributbeschreibung

|Name|Typ|Z|Beschreibung|
|---|---|---|---|
|lastPublished|DateTime|j|Letzte (aktuellste) Publikation dieses Teils der ThemenBereitstellung.|

### Konstraints

UK über die FK

## Klasse SubArea

Fläche eines Teilgebietes des Kanton Solothurn. Alle 1-n Teilgebiete der gleichen Datenabdeckung bilden zusammen ein AREA-Datensatz, welcher 
die Datenabdeckung innerhalb des Kt. Solothurn dokumentiert.

Wird mittels GRETL-Job aus dem Schema "agi_data_coverage" (Modell SO_AGI_Meta_Datenabdeckung_YYYMMMDD) gepflegt (Bulk InsUpdDel).

### Attributbeschreibung

|Name|Typ|Z|Beschreibung|
|---|---|---|---|
|identifier|String(100)|j|Eindeutige Kennung des Teilgebiets.|
|coverageIdent|String(100)|j|Kennung der Datenabdeckung des Teilgebiets.|
|geomWkb|byte[]|j|Polygon-Geometrie des Teilgebietes als WKB.|
|title|String(255)|n|Sprechender Titel des Teilgebiets.|
|version|DateTime|n|Timestamp der Version. Notwendig für den GRETL Aktualisierungs-Job|

### Konstraints

UK über identifier, coverageIdent, version

# Ablauf der Datensynchronisation

* db2db mit version = null
* Update der bestehende Beziehungen von PublishedSubArea auf die neu importierten SubArea
* Alle Subareas löschen: `where version is not null`
* Version für neu importierte setzen: `version = now()`

# Ablauf bei PUT-Aufruf durch Publisher

## Publisher sendet Aktualisierung

    {
      "dataIdent": "ch.so.afu.gewaesserschutz",
      "published": "2021-12-23T14:54:49.050062",
      "partIdentifiers": ["224", "225"]
    }

## Aktualisierung PublishedSubArea

* Falls PublishedSubArea bereits vorhanden:
  * Dieses Aktualisieren (Timestamp)
* Sonst:
  * Neues PublishedSubArea erstellen