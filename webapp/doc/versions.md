# Versionen der Webapplikation "SIMI"

# V 1

Umfasst in Metamodell und Funktionalität den für die Ablösung des AGDI notwendigen Umfang.

## V 1.1

Erste Version von SIMI, mit welcher produktiv gearbeitet wird.  
Letzte Modelländerungen:

* Alias, ... wird einmal für jede Tabelle, und nicht für jede TableView modelliert (Auflösung Redundanz)
* IAM User: Name und Vorname sind für 1.x überflüssig --> Wurden in Modell auf optional gesetzt
  
### Mögliche Verbesserungen
  * Konsequent Anzeige von "\[NULL\]" oder ähnlich bei Attributen, die null sind.

## V 1.2

### Mögliche Verbesserungen

#### Zwingende Schemaanpassungen
* IAM User: Entfernen von Name und Vorname. Diese wurden mit 1.1.x von mandatory auf optional gestellt.
  * Rahmenbedingung: Beibehalten der Vererbungsstruktur Identity, Group, User wegen der Beziehungen. 

#### Externe WMS / WMTS

* Gruppierungs-Limitationen aus Frühlingsrelease auflösen
* Notwendige Eigenschaften in Metamodell vollständig abbilden
* Prüfen, ob die extern verfügbaren Eigenschaften (Titel, Beschreibung, ...) im Rahmen der Pipeline zur Laufzeit abgeholt werden sollen

#### Print

* Konzeptionellen Entscheid für heutige Lösung nachvollziehen
* Der Print-WMS umfasst ca. 5% mehr Ebenen wie der publizierte (=105%). Macht das so Sinn, oder sollte der Print nur die 5 zusätzlichen Prozent umfassen?
* Zusammenhang mit Last und Antwortzeiten beachten

#### Transparenz

* Überschreibung der Transparenz der Einzelebenen eines Facadelayers? (In PropertiesInFacade)