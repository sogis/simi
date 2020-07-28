# K3 - SIMI Hauptanwendungsfälle

## Umsetzungsfragen

Erarbeitung, welches mittelfristig (Nach «Migration Sogis-DB) die
Hauptanwendungsfälle der Arbeit mit SIMI sind.
Mittels AGDI wurden vor allem Ebenen neu erfasst. Mit SIMI ist zu erwarten,
dass mehrheitlich bestehende Ebenen mutiert werden.

## Resultate des WS vom 27.07

Hauptanwendungsfälle:
* Modell nach kleiner Anpassung wieder publizieren (Änderung weniger Attribute)
  * Beinhaltet die Anpassung von mutierten Kommentaren.
* Thema komplett überarbeiten ("Neues Modell")
  * $td: A -> B Mutation entwerfen
* Aus bestehender Konfiguration eine fast identische geschützte erstellen.
  * LayerListe
  * FacadeLayer
  * SingleLayer
* Punktuelle Änderung im Feedbackprozess
  * QML anpassen
  * Beschriftungen anpassen
  * ...
  
### Detailanforderungen

|Anforderung|Hintergrund|Fragen|
|---|---|---|
|Der Zeitpunkt des letzten QML-Hochladens soll ersichtlich sein., damit in der Bearbeitung erkannt werden kann, ob schon hochgeladen wurde oder nicht.|Bestätigung in der oft komplexen Bearbeitung, dass QML hochgeladen wurde.||
|Der Alias soll aufgrund des DB-Attributnamen abgeleitet werden und in das DS eingefüllt werden. Mit Überschreibmöglichkeit im DSV|Arbeitserleichterung|Kann dies für gelegentliche Benutzer verständlich gemacht werden? (GIS-Koordinatoren)|
|Anzeige für ein DSV, ob und welchem SL es zugeordnet ist.|Zwecks Übersicht, beispielsweise bei Komplettüberarbeitung eines Themas|Passt das so? Aussage war: Für DS anzeigen, in welchem PS...|
