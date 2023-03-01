# SubArea (Theme)

Dokumentiert die Klassen des Package Theme.SubArea (SIMI) und die Zusammenhänge zum Modell "SO_AGI_Meta_Datenabdeckung_YYYYMMDD". Das Modell ist im Schema agi_data_coverage auf der Edit-DB implementiert.

![DataCoverage](resources/theme/subarea.png)

### Bemerkungen zu den Beziehungen

* PublishedSubArea: Der FK auf SubArea ist konzeptionell zwingend. Physisch optional umgesetzt, damit der GRETL-Job die SubAreas löschen und neu einfügen kann.

## Klasse PublishedSubArea

Mit dem "published" Timestamp wird in dieser Klasse gespeichert, wann ein Teilgebiet einer Themenpublikation das letzte Mal publiziert wurde. "published" wird für Vektordaten via GRETL-Publisher geschrieben.

### Attributbeschreibung

|Name|Typ|Z|Beschreibung|
|---|---|---|---|
|published|DateTime|j|Letzte (aktuellste) Publikation dieses Teils der ThemenBereitstellung.|
|prevPublished|DateTime|j|Vorletzte Publikation dieses Teils der ThemenBereitstellung.|
|subAreaIdent|String(100)|n|Hilfsfeld für die Zuweisung auf die entsprechende SubArea bei Aktualisierung aus der Edit-DB (via GRETL-Job).|

### Konstraints

UK über die FK.

## Klasse SubArea

Fläche eines Teilgebietes des Kanton Solothurn. Alle 1-n Teilgebiete der gleichen Datenabdeckung bilden zusammen ein AREA-Datensatz, welcher 
die Datenabdeckung innerhalb des Kt. Solothurn dokumentiert.

Wird mittels GRETL-Job aus dem Schema "agi_data_coverage" (Modell SO_AGI_Meta_Datenabdeckung_YYYMMMDD) gepflegt (Bulk InsUpdDel). Trägt darum auch nicht die gewohnten Meta-Attribute zu Zeitpunkt und Benutzer der Erstellung eines Objektes (Vererbt nicht von SimiEntity sondern von BaseUuidEntity).

### Attributbeschreibung

|Name|Typ|Z|Beschreibung|
|---|---|---|---|
|identifier|String(100)|j|Eindeutige Kennung des Teilgebiets.|
|coverageIdent|String(100)|j|Kennung der Datenabdeckung des importierten Teilgebiets (Sprechender FK auf die Datenabdeckung in der Edit-DB). |
|geomWKB|byte[]|j|Polygon-Geometrie des Teilgebietes als WKB.|
|title|String(255)|n|Sprechender Titel des Teilgebiets. Todo: Auf Mandatory setzen oder |

### Konstraints

UK über identifier, coverageIdent, updated

## Klasse PublishedSubAreaHelper

Hilfsklasse, mit welcher der Rollout-GRETL Job die PublishedSubArea der Pub-DB auf die ThemePublication der Int-DB zuweist (Zwecks Erhalt der produktiven Publikationsdaten).

Ablauf siehe readme.md im GRETL-Job "agi_layer_rollout"