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








# Modellierungsfragen

|Betrifft|Frage|Wer|
|---|---|---|
|data.PostgresSchema|Wann sind die verwendeten Parameter der Schemaerstellung mittels ili2pg Teil des Model-Repos?|Stefan|
|-|Ist nicht nötig. Die Parameter werden in die ili2db Metatabellen im Schema selbst geschrieben.|-|
|data.PostgresDB|Verwendung der Service-Definition verstehen. Host und Port als Attribute ok?|Michael|
|-|Nein, bis auf weiteres der Servicename, da keine Automatisierungen für jdbc bestehen. jdbc kann nicht via Servicename connecten.|-|
|data und core|Transparenz-Regeln verstehen.|Michael|
|-|Facadelayer: Wenn in FL gesetzt: FL 0.5 * FL-Child 0.5 = 0.25 resultierend für FL-Child <br/> ProductList: Wenn gesetzt, wird der Wert des PL-Child überschrieben |-|
|data|Externe WMS-Ebene wie einbinden? Vergleichen mit aktualisiertern Config-DB|Oliver|
|-|Antwort...|-|
|data|Wie die Raster in die GDI einbinden? Es gibt zu viele Möglichkeiten...|Stefan|
|-|Antwort...|-|
|Dependencies|Erfassen wir auch reine Identifier-Abhängigkeiten? Wie die Abhängigkeit auf den Namen einer WMS-Ebene?|Michael und Stefan|
|-|Antwort...|-|
|Konf Backgroundlayer|Soll das weiterhin erfasst werden? Eine flexiblere Konfigurationsvariante ist die Bereitstellung einer Rumpf-Konfiguration des config.json des Web GIS Client. In diese werden die dynamischeren Eigenschaften von beispielsweise den Vordergrundebenen hineingeneriert.|Michael und Stefan|
|-|Antwort...|-|
|GM03|Modellierung der Klassen und Beziehungen für GM03|Peter|
|-|Antwort...|-|
|GM03|Verifikation, dass Contacts-Modellierung GM03-Kompatibel ist|Peter|
|-|Antwort...|-|
|core.DataProduct.identifier|Namenskonvention einführen? ch.so.xy.[Thema].(edit.)[Layername](_data). Thema entspricht Modellname ohne Version.|Stefan|
|-|Antwort...|-|
|core.DataProduct.inWGC|Wie sind geschützte Edit-Ebenen im WMS enthalten? Diese sind aus Sicht public überflüssig und störend.|Oliver|
|-|Antwort...|-|
|core|Automatische Ableitung des TOC-Namen für SingleActors in Gruppen. TOC-Name = [SingleActor.Title] ([ProductList.Title])|Andrea|
|-|Antwort...|-|
|core.Map|Hintergrundkarten bestehen heute doppelt im AGDI (für SO-Locator) und als "Feeder-Projekt" für den WMTS. Kann dies einfach verhindert werden?|Andi|
|-|Antwort...|-|
|SL - DSV|In Ruhe reflektieren, ob ein Teil der Module besser auf DSV / DS wie auf SL verknüpft werden sollen (u.a: Versionierung)|Oliver|
|-|Antwort...|-|
|data DataTheme|Braucht es eine Klasse DataTheme? Diese ist nötig, wenn DataSets wie Beispielsweise die LIDAR-Produkte gruppiert als ein Paket abgegeben werden sollen (Kachel.zip mit DOM, DTM, Höhenlinien, ...).|Oliver|
|-|Antwort...|-|
|data bulk / object service|Steuerung vieleicht trennen. Datenbezug wird Modellweise freigeschaltet, Service-Booleans müssen auf TableView|Oliver|
|-|Antwort...|-|
|data taster|Wie soll die Bandselektion erfolgen? QML?|Stefan|
|-|Antwort...|-|










