# Simi Umsetzungsfragen

## Übersicht der Fragestellungen

Klärung der maskenübergreifend «auftretenden» Fragestellungen:
* In welchem «Vererbungsbaum» wird wieso welche Vererbung
angewendet?
* Best Practice für die Bereitstellung eines Service (→ Kenne dein
GRETL)
* Best Practice für das Konsumieren eines Service (ModelReader)
* Validierung: Was gilt es für die Validierung auf dem Client zu
beachten? (Lazy-Loading, …)
* Wie können zwecks Entkoppelung der SIMI- von der Json-Export
Entwicklung einfach «extended Properties» vorgesehen werden?
* GUI-Hilfsfunktionen: Wie wird das Kopieren einer Layergruppe in
eine Map im SIMI-GUI umgesetzt?

Deployment:
* Best-Practice bzgl. Docker-Image
    * Erstellung (Base-Image, …)
    * Umgebungsvariablen
* DB-Setup
* Notwendige Ressourcen für stabile und performante Ausführung

## Vererbungstrategien pro Baum

Fragestellung: In welchem «Vererbungsbaum» wird wieso welche Vererbung
angewendet?

Teilmodell|Root-Klasse|Strategie|Bemerkungen|
|---|---|---|---|
|Contact|Contact|Joined|Wegen Beziehung zwischen den Kindern|
|Data|DataSetView|Joined|Wegen Beziehung zwischen den Kindern|
|Data|DataSet|"Konzeptionell"|Physische Abbildung weder sinnvoll noch notwendig|
|Data|TableDS|SingleTable|Ist einfacher...|
|Data|TableDS|SingleTable|Ist einfacher...|
|IAM|Identity|Joined|Wegen Beziehung zwischen den Kindern|
|Product|Dataproduct|Strukt|Wegen Vererbungstiefe nicht "joined"|
|Product|SingleActor|Joined|Wenn es nicht performt, Umbau auf SingleTable|
|Product|ProductList|SingleTable|Ist einfacher...|

## Weitere Detailfragen

|Betreff|Frage / Antwort|Status|
|---|---|---|
|DataProduct.pubScope|Wie die enum mit Klassenabhängig zutreffenden Werten umsetzen?|Offen|
| |ANTWORT| |
| | | |
|DataSetView.styleServer|Wie kann der Text-Inhalt einer Datei in ein String-Feld geschrieben werden?|Offen|
| |ANTWORT| |
| | | |
|BETREFF|FRAGE|Offen|
| |ANTWORT| |




