# Packages (Teilmodelle)

![Übersicht der Teilmodelle](../puml/rendered/simi_overview.png)

* **Product [[Link]](product.md):** Enthält die Klassen für die Konfiguration der aus Data abgeleiteten Produkte. 
* **Data [[Link]](data.md):** Enthält die Klassen für die Beschreibung der von der GDI genutzten 
Datenquellen (GIS-Tabellen, Raster, ...).
* **IAM [[Link]](iam.md):** Klassen zur Konfiguration des Identity und 
Access Management (Benutzer, Gruppen, Rollen, ...).
* **Contact [[Link]](contact.md):** Teilmodell zur Definition der Kontaktinformationen 
bezüglich der genutzten Daten.
* **Flow [[Link]](flow.md):** Dokumentiert die Datenflüsse der GRETL-Jobs.
* **CCC [[Link]](ccc.md):** Modelliert die einzelnen CCC-Integrationen (CCC-Client).
* **Bouncer [[Link]](bouncer.md):** Klassen zur Konfiguration des Bouncer-Proxy, welcher den Zugriff auf eine Ressource einschränkt.
* **Print [[Link]](print.md):** Modelliert die Metainformationen für das Erzeugen von Karten-PDF's.
* **Dependency [[Link]](dependency.md):** Doku der Komponenten, welche von Datenstrukturen der GDI abhängig sind.
* **Featureinfo [[Link]](featureinfo.md):** Konfig von Featureinfo. Dieses erweitert das QGIS-Server Featureinfo mit templated HTML-Output und Datenquellen.
* **Globals [[Link]](globals.md):** Konfig von GDI-weiten Konstanten.

## Konstraints

Für alle \*:\* Beziehungen in den Teilmodellen ist ein UK über die FK's zu erstellen.

## Erläuterungen zur Kapitelstruktur innerhalb der Teilmodelle

Strukturierung der *.md der Teilmodelle:
 
```
# [Name des Teilmodelles] --Erläuterungen zum ganzen Teilmodell
## Modell-Konstraints -- Falls zutreffend
## Klasse [Klassenname] -- Erläuterungen zu einer Klasse des Teilmodelles
### Attributbeschreibung -- Tabellarische Beschreibung der Attribute
### Konstraints -- Falls zutreffend
```

### Spalten der Attributbeschreibung

* **Name:** Attributname
* **Typ:** Java-Datentyp des Attributes. String wird zusätzlich mit der Länge qualifiziert.
* **Z:** Ist das Attribut **z**wingend erforderlich? (j,n)
* **Beschreibung:** Erläuterungen zum Attribut

Die Attributtabellen sind sortiert gemäss:
1. Zwingende alphabetisch
2. Optionale alphabetisch

## Steuerung der Verfügbarkeit der Daten und Kartenebenen

Die Steuerung betrifft die folgenden Applikationen und Dienste und bezieht sich auf Produkte (= Zusammensetzungen)
und Rohdaten. Das Datenthema = Klassen im Modell wird ebenfalls betrachtet.

|Datenklasse \><br>Apps und Dienste|SingleLayer|DataTheme|FacadeLayer|Singleactor|LayerList|
|---|---|---|---|---|---|
|Featureservice (WFS u. DataService)|x|-|-|-|-|
|Bulkservice|x (1.)|-|-|-|-|
|WMS (2.)|-|-|-|x|x|

Todo: 
* Ergänzen mit Spalte Map
* Ergänzem mit Zeile WGC und SO-Locator

Bemerkungen:
1. Die Berechtigung für den Bulk-Service leitet sich aus der Summe der Berechtigungen der enthaltenen SingleLayer ab.   
Ein xtf kann auch abgegeben werden, sofern eine geschützte Spalte nullable ist --> wird in xtf auf null gesetzt.   
Umsetzung davon muss in Konzeptphase zu Datenbezug gelöst werden. 
2. Was ist das gewünschte Verhalten bei der Kombination der Flags für SingleActor und LayerList?

#### Snipplet für Tabellenerstellung

```markdown
|Name|Typ|Z|Beschreibung|
|---|---|---|---|
```

#### Stringlängen anschaulich

|Länge|Beispiel|
|---|---|
|String(100)|Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut l|
|String(200)|Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores |
|String(1000)|Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu f|

# Beziehung zu configXY.json

Mittels Simi werden auch die Konfigurationsdateien für API und WGC erzeugt. Sofern in der Metadatenbank keine Informationen
zu den zu konfigurierenden Metaobjekten enthalten sind, kann die Konfiguration komplett ausserhalb SIMI, oder über das
Teilmodell globals erfolgen. Für die betroffenen configXY.json ist in der Spalte Teilmodell jeweils "globals" eingetragen.

|Konfig-Datei|Simi-Teilmodell|Bemerkungen|
|---|---|---|
|agdiConfig.json|globals.gdi.*|Viele der Eigenschaften werden in die Pipeline wandern.|
|cccConfig.json|ccc|
|dataConfig|data||
|documentConfig.json|globals|Wo sind die Referenzen auf die datasets abgebildet? Diese sollten sich mittels bouncer abbilden lassen||
|elevationConfig.json|globals.elevation.*||
|featureInfoConfig.json|FeatureInfo||
|landregConfig.json|globals.landreg.*|Nachteil bei Lösung über globals: Know your GDI ist schwieriger zu lösen. Ist dies nicht sowieso ein eigenes qml? $td|
|mapViewerConfig.json|product||
|permission.json|?|$td|
|printConfig.json|globals.wgcPrint.*||








#










