# Theme

Bildet die Themen und Themenbereitstellungen ab (Datenbezug)

![Theme](resources/theme/theme.png)

## Bemerkungen 

### Unter-Package "SubArea"

Die Geometrien werden mittels GRETL-Job in die SIMI-DB kopiert.

Dokumentation SubArea siehe [hier](theme_subarea.md)

## Klasse Theme

Aufgrund der fachlichen (nicht technischen) Auseinandersetzung definiertes Thema, für welches Geodaten für den Bezug bereitgestellt werden. Meistens wird ein Thema in einem einzigen Nachführungsablauf nachgeführt. Pro Theme gibt es ein bis mehrere zum Download bereitgestellte [Themenbereitstellungen (ThemePublication)](#klasse-themepublication)

### Attributbeschreibung

|Name|Typ|Z|Beschreibung|
|---|---|---|---|
|identifier|String(100)|j|Eindeutiger Identifier des Themas (ch.so.\[Amt\].\[Thema\]).|
|title|String(200)|j|Angezeigter Titel des Themas.|
|description|String(1000)|j|Kurze fachliche Bescheibung des Themas. Ziel: < 500 Zeichen Text. Kann HTML-Markup enthalten (\<br\/\>, \<a ...\>\<\/a\>)|
|furtherInfoUrl|String(500)|n|Verweis auf eine Seite mit spezifischen weiteren Informationen zum Thema (Häufig auf Kantons-Typo3).|
|synonymsArr|String(800)|n|Synonyme für das Thema. Als Json-Array formatiert.|
|keywordsArr|String(800)|n|Stichworte für das Thema. Als Json-Array formatiert.|
|remarks|String|n|Interne Bemerkungen.|

### Konstraints

UK auf identifier.

## Klasse ThemePublication

Konkrete Bereitstellung(en) eines Themas. Im gängigen Fall der Bearbeitung im Edit-Modell mit Bereitstellung im Pub-Modell besteht eine "überall" sichtbare Bereitstellung gemäss Pub-Modell und eine nur auf [files.geo.so.ch](https://files.geo.so.ch) sichtbare Bereitstellung gemäss Edit-Modell.

Eine Themenbereitstellung enthält bei Vektordaten 1-n Tabellen. Bei nicht vektoriellen Daten ist per Konvention pro Themenbereitstellung nur eine Datendatei enthalten (z.B. ein GeoTiff). Die Beziehung der Themenbereitstellung zur Datendatei ist in diesem Fall nicht explizit ausmodelliert. Das korrekte Funktionieren der Verknüpfungen erfolgt über die übereinstimmenden Kennungen (Bsp: ch.so.agi.walkerkarte), Dateitypen und manuell korrekt gesetzten Ordner- und Dateinamen auf der Dateiablage (Bsp: ch.so.agi.walkerkarte/aktuell/ch.so.agi.walkerkarte.tif).

### Attributbeschreibung

|Name|Typ|Z|Beschreibung|
|---|---|---|---|
|publicModelName|String|(n)|Name des Publikationsmodelles. Zwingend für tabellarische Daten. Der GRETL Publisher ermittelt dynamisch das Publikationsmodell und meldet dieses mit dem Publikations-Zeitpunkt an SIMI. Bei Differenzen bricht der GRETL-Job mit Fehler ab.|
|dataClass|Enum|j|Typ der Publikation: tableSimple, tableRelational, nonTabular. Deprecated. Siehe Bemerkungen|
|classSuffixOverride|String(50)|(n)|Suffix auf den Themen-Identifier zur Unterscheidung bei mehreren Bereitstellungen pro Thema (Null bei nur einer Bereitstellung). Bsp. **kommunal** für kommunale Nutzungsplanung. Resultierender Identifier: ch.so.arp.nutzungsplanung.kommunal. Todo: Rename auf identSuffix. Deprecated.|
|titleOverride|String(200)|n|Überschreibung des Themen-Titels.|
|descriptionOverride|String(1000)|n|Überschreibung der Themen-Beschreibung.|
|coverageIdent|String(100)|j|Identifier der Datenabdeckung (DataCoverage) dieser Themenbereitstellung. Default "ktso" für ganzer Kanton.|
|remarks|String|n|Interne Bemerkungen.|
|modelUpdateTs|String|n|Deprecated|
|modelUpdatedBy|String|n|Deprecated|

Bemerkungen:

* dataClass: Inzwischen unglücklicher Mix von Bedeutungen / Konsequenzen
  * Falls =tableRelational wird identifier auf *.relational ausgefüllt
  * Falls =tableRelational in data.geo.so.ch nicht sichtbar
  * Falls =tableSimple und keine Dateitypen zugewiesen: Default-Zuweisung in View
  * Fazit: Ersetzen mit Boolean "Nur auf files.geo.so.ch"

### Konstraints

UK über _typeSuffix, "FK auf Thema".

## Klasse CustomFileType

Enthält die Dateitypen des Datenbezugs. Zip wird nicht erfasst, da dieses nur als Ordner dient und nicht Aussagekräftig ist.

Deprecated: Umbenennen von CustomFileType auf FileType

### Attributbeschreibung

|Name|Typ|Z|Beschreibung|
|---|---|---|---|
|mimeType|String(255)|j|Mime-Type des Dateityps.|
|name|String(100)|j|Sprechender Name des Dateityps.|
|kuerzel|String(50)|j|Suffix für die Dateibenennung. Bei gezippter Bereitstellung zweiteilig. Beispiele: xtf.zip, gpkg.zip, tif, laz. Wird in data.geo.so.ch, Geocat-Export als Key für weitere Eigenschaften verwendet (Sortierung, ...)|

### Konstraints

Separate UK auf mimeType, name, kuerzel.

## Klasse DataSetView

Siehe [DataSetView](data.md#klasse-datasetview-dsv) in Teilmodell "Data".

### Konstraints

* Keine Zuweisung auf TableViews mit "Rowfilter-View"
* Innerhalb der gleichen Themen-Bereitstellung:
  * TableViews müssen aus dem gleichen Schema stammen
  * Kein Gemisch von TableView und RasterView

## Klasse OrgUnit

Organisationseinheit, welche zu den Themen-Bereitstellungen fachlich oder technisch Auskunft geben kann.

Da die technische Auskunft immer durch das AGI erfolgt, wird nur die Beziehung für die fachliche Auskunft modelliert und geführt.

### Attributbeschreibung

|Name|Typ|Z|Beschreibung|
|---|---|---|---|
|name|String(100)|j|Name (Bezeichnung) der Organisationseinheit. Bsp: "Amt für Geoinformation", SubOrg: "Abteilung Boden (AfU)", SubSubOrg: "Fachbereich Altlasten (AfU Abteilung Boden)"|

Todo: Name unique setzen. Deprecated

## Klasse Agency

Informationen zum Amt (AfU, ARP, AGI, ...). Hinweis: Punktuell ist der Klassenüberbegriff "Amt" nicht ganz zutreffend (Beispielsweise bei Daten der Solothurnischen Gebäudeversicherung).

### Attributbeschreibung

|Name|Typ|Z|Beschreibung|
|---|---|---|---|
|abbreviation|String(10)|j|Kürzel für das Amt (AfU, ARP, ...). Kürzel "AGI" dient als Schlüssel für die "automatische" Zuweisung des techn. Kontaktes zu allen Themen.|
|url|String(255)|j|URL der Homepage des Amts.|
|phone|String(20)|j|Kennung und Vorwahl innerhalb CH: 032 212 66 77|
|email|String(50)|j|Email-Adresse des Amtes|

### Konstraints

UK auf abbreviation.

## Klasse SubOrg

Unterorganisation innerhalb eines Amts. Der Einfachheit halber wird nur eine Hierarchiestufe ausmodelliert (Es gibt keine Klasse SubSubOrg).

### Attributbeschreibung

|Name|Typ|Z|Beschreibung|
|---|---|---|---|
|url|String(255)|n|URL der Homepage des Amts.|
|phone|String(20)|n|Kennung und Vorwahl innerhalb CH: 032 212 66 77.|
|email|String(50)|n|Email-Adresse der Untereinheit.|

Alle Attribute sind optionale Überschreibungen den Angaben auf der Agency.

### Konstraints

UK auf name und FK zum Amt (Wird nur bei kleinem Aufwand umgesetzt).
