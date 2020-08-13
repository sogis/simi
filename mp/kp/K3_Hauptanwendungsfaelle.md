# K3 - SIMI Hauptanwendungsfälle

## Umsetzungsfragen

Erarbeitung, welches mittelfristig (Nach «Migration Sogis-DB) die
Hauptanwendungsfälle der Arbeit mit SIMI sind.
Mittels AGDI wurden vor allem Ebenen neu erfasst. Mit SIMI ist zu erwarten,
dass mehrheitlich bestehende Ebenen mutiert werden.

## Resultate des WS vom 27.07

Hauptanwendungsfälle:
* Modell nach kleiner Anpassung wieder publizieren (Änderung weniger Attribute, Klassen (=DataSet's) bleiben gleich)
  * Beinhaltet die Anpassung von mutierten Kommentaren.
* Thema komplett überarbeiten ("Neues Modell")
  * --> Teilmodell ["Mutation"](../../doc/model/mutation.md)
* Aus bestehender Konfiguration eine fast identische geschützte erstellen.
  * LayerListe
  * FacadeLayer
  * DataSetView
* Punktuelle Änderung im Feedbackprozess
  * QML anpassen
  * Beschriftungen anpassen
  * ...
  
Ca. jede zehnte Ebene hat auch Konfiguration bezüglich:
* Spez. Layerinfo
* Suche
* Report
  
### Detailanforderungen

|Anforderung|Hintergrund|Fragen / Bemerkungen|
|---|---|---|
|Der Zeitpunkt des letzten QML-Hochladens soll ersichtlich sein., damit in der Bearbeitung erkannt werden kann, ob schon hochgeladen wurde oder nicht.|Bestätigung in der oft komplexen Bearbeitung, dass QML hochgeladen wurde.||
|Der Alias soll aufgrund des DB-Attributnamen abgeleitet werden und in das DS eingefüllt werden. Mit Überschreibmöglichkeit im DSV|Arbeitserleichterung|Kann dies für gelegentliche Benutzer verständlich gemacht werden? (GIS-Koordinatoren)|
|Anzeige für ein DSV, ob und welcher DSV es zugeordnet ist.|Zwecks Übersicht, beispielsweise bei Komplettüberarbeitung eines Themas|Passt das so? Aussage war: Für DS anzeigen, in welchem PS...|
|Einfaches Sortieren von Attributen / Ebenen|Effizientes Arbeiten|Wie viele male müssen mehrere Ebenen, Attribute "en bloc" einsortiert werden?|
|Python-Formattierung mittels Combobox verständlicher machen. Combobox muss freie Stringeingabe ebenfalls unterstützen|Formattierung ist krypisch||
|Bei Mutation einer Layerliste auf die Karten hinweisen, welche dieselben SA enthalten|Arbeitsunterstützung, damit Karten nicht vergessen gehen|Aufwandabhängig wird zusätzlich die automatische "Nachmutation" der Karten angeboten.|
|Automatische Ableitung von ch.so. ... aus den bestehenden Informationen|Homogenität|Muster: ch.so.[Amt].[Thema].(edit).[Ebenenname]|

## Entscheide

|Thema|Entscheid|Bemerkungen|
|---|---|---|
|Teilmodell Mutation|Entscheid dazu, nachdem 2/3 des GUI's der anderen Teilmodelle implementiert sind.|Das konzipierte Teilmodell ist mit vernünftigem Aufwand zu erstellen - Trotzdem muss Gewissheit zum Nutzen bestehen.|
