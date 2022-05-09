# Theme

Bildet die Themen und Themenbereitstellungen ab (Datenbezug)

![Theme](resources/theme/theme.png)

## Bemerkung zu den Attributbeschreibungen

Ggf. werden die Beschreibungen mit den Tags $meta $port versehen, damit nach vollzogen werden kann, zu welchen Zwecken ein Attribut geführt wird. Falls weder $meta noch $port angegeben --> Attribut dient "nur" der internen Dokumentation.

## Klasse Theme

Aufgrund der fachlichen (nicht technischen) Auseinandersetzung definiertes Thema, für welches Geodaten für den Bezug bereitgestellt werden. Bei Vektordaten gilt meist: Umfang des Publikations-Modelles = Datenumfang eines Themas.

Im Datenschatz von Solothurn bestehen zwei Haupt-Typen von Themen:

* **Typ Einzelthema:** Thematisch eng umrissenes fachlich begründetes Thema. Beispiel: Bienenstandorte
* **Typ Themengruppe:** "Superthema", welches mehrere fachlich eng umrissene Einzelthemen zusammenfasst. Meist mit dem Hintergrund eines gemeinsamen Nachführungs- / Bewilligungsablaufes. Beispiele: AV, Nutzungsplanung

Für die Steuerung der GDI macht es keinen Unterschied, ob ein Thema ein Einzelthema oder eine Themengruppe ist, darum sind Einzelthema, Themengruppe nicht ausmodelliert.

## Klasse ThemePublication

Konkrete Bereitstellung(en) eines Themas für einen Nutzungszweck. Beispiel Thema Verkehrszählung: Dessen Daten können lesend eingebunden werden -> Publikation ch.so.avt.verkehrszaelstellen.
Oder als Quelle von komplexen Weiterverarbeitungen genutzt werden, in welchen die relationale Gliederung von Vorteil ist --> Publikation ch.so.avt.verkehrszaelstellen.edit.

## Klasse DataSetView

Siehe [DataSetView](data.md#klasse-datasetview-dsv) in Teilmodell "Data".

## Klasse OrgUnit

Organisationseinheit, welche zu den Themen-Bereitstellungen fachlich oder technisch auskunft geben können.

Da die technische Auskunft immer durch das AGI erfolgt, wird nur die Beziehung für die fachliche Auskunft ausmodelliert.

## Klasse Agency

Informationen zum Amt (AfU, ARP, AGI, ...). Punktuell ist der Klassenüberbegriff "Amt" nicht ganz zutreffend (Beispielsweise bei Daten der Solothurnischen Gebäudeversicherung).

* Bezeichnung default "Amt" - Zwecks Präzisierung, falls Suborg erfasst, jedoch Amtstelefon angezeigt wird.

## Klasse SubOrg

Unterorganisation innerhalb eines Amts. Der Einfachheit halber wird nur eine Hierarchiestufe ausmodelliert (Es gibt keine SubSubOrg). Beispiel für die Erfassung eines Fachbereiches als SubOrg: "Fachbereich Altlasten (Abteilung Boden)"

## Klasse DataCoverage

Datenabdeckungs-Ebene für 1-n Themen. Aus der Ebene gehen zwei Informationen hervor:
* Gibt es Gebiete im Kanton ohne Daten (Datenlücken)?
* Sind die Daten für den Bezug im mehrere Stücke aufgeteilt? Und wenn ja: In welche Teilflächen (Polygone)?

## Klasse PublishedExtent

Mit dem "LastPublished date" wird in dieser Klasse gespeichert, wann eine Themenpublikation das letzte mal publiziert wurde. LastPublished wird für Vektordaten via GRETL-Publisher geschrieben.

## Klasse Extent

Fläche eines Teilgebietes des Kanton Solothurn.

### Beispiel Attributbeschreibung

|Name|Typ|Z|Beschreibung|
|---|---|---|---|
|identifier|String(100)|j|Eindeutiger hierarchischer Identifier des DataProduct (ch.so.fuu.bar).|
|pubScope|enum|j|Gibt an, in welchen Diensten und Applikationen das DP publiziert ist. Details siehe [hier](../metamodel.md#ebenenpublikation-in-dataproduct).|
|keywords|String(200)|n|Stichworte für das DataProduct. Können auch thematische Überbegriffe sein.|
|remarks|String|n|Interne Bemerkungen.|
|synonyms|String(200)|n|Synonyme für das DataProduct.|
|title|String(200)|n|Angezeigter Titel (Bezeichnung) des Dataproduct. Falls null in Erstellungsphase wird identifier verwendet.|
|description|String|n|Beschreibung|

### Beispiel Konstraints

Feld "identifier" ist GDI-weit eindeutig.