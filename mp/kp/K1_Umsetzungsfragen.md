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
|DataSetView.styleServer|Wie kann der Text-Inhalt einer Datei in ein String-Feld geschrieben werden?|Offen|
| |ANTWORT| |
| | | |
|Login einmal wöchentlich|Gibt es einen einfachen Weg, nur einmal in der Woche nach den Credentials zur Fragen (etwa mittels langlebigem auth cookie)? Zusammenhang mit cuba.web.rememberMeEnabled?|Erledigt|
| |Setting [rememberMeExpirationTimeoutSec](https://doc.cuba-platform.com/manual-latest/app_properties_reference.html#cuba.rememberMeExpirationTimeoutSec)| |
| | | |
|Entity Zusatzinformationen|Best practice für das Auslesen von zusätzlichen Informationen aus verknüpften Entities. Zum Beispiel ob eine DataSetView "public" ist. JPQL Query / Native SQL auf DB-View / ...?|Offen|
| |ANTWORT| |
| | | |
|Custom Query: Abdeckung von Änderungen|Feedback zur Idee, DB-Views für die Zusammenzüge zu verwenden. Behindert dies die automatisch generierten update Skripte? Zu verhindern mit "do last"?|Offen|
| |ANTWORT| |
| | | |
|Updated Info|Is the information on timestamp and user of last update available in the existing admin interface?|Pending|
| |Answer| |
| | | |
|Updated Info|If not available in admin interface: How can it be displayed without code duplication on every edit screen? Screen Fragment or Mixin or other?|Pending|
| |Answer| |
| | | |
|Abstract|Question|Pending|
| |Answer| |

Entscheide:
* Vererbungsarten
* Einsatz von View's
* Binäre Assets in DB als einfache Strings (base64)
* Ein Screen für Rollen AGI-MA und GIS-Koord
* Know your GDI als Screen (Daten aus View)






