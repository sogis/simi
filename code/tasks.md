# Version 0.9

* Done Fertigstellung der Abhängigkeitsanzeige
* Done Integrationstest: Laden aller Daten gemäss der jeweiligen XML-View
* Done Anpassung Versionierung auf Major.Minor.Revision
  * Major: Breaking Schema change
  * Minor: Breaking Config change
  * Revision: Fortlaufende Buildnummer
* Done Jenkins-Integration erstellen (für Start der Config-Pipeline) 


# Version 1.0

* Schema
  * DONE Entfernen Publikationsfelder (releasedAt, releasedThrough)
  * DONE Verschieben der Suchfelder von DSV in TableView
  * DONE Verschieben Attributalias, wmsFiInfo, displayProps4Json von ViewField nach TableField
  * Externe WMS / WMTS erfassen
  * Ergänzung mit den fehlenden Feldern, gemäss Erkenntnissen aus Datenmigration und Erstellung der
  Konfigurations-Jsons.
  * DONE In der Tabelle simidata_postgres_db muss noch die service-URL der DB-Connection ergänzt werden (neues Attribut)
  * DONE Aufräumen DataSetView-Klassen (TableView, ViewField, RasterView, DataSetView)
    * Db-Präfix simidata. 
    * Java-Package data.*
  * Entfernen der ext-Felder der Datenmigration    
  * UK über FK's und UK über FK, Attribut?
  * Bei Anlegen Datatheme und im gleichen Schritt Auslesen der DB --> Exception
  * Nach Testing
      * Screens Wegräumen
        * _layer-group-choose.xml
        * _data-set-view-browse.xml
        * _data-product_pub-scope-browse.xml
        * _data-product_pub-scope-edit.xml
        * _map-browse.xml
        * _single-actor-browse.xml
        
        
# Version 1.x
  * Entity-Loading tests entity-Zentrisch machen (eine Testmethode pro entity mit helper testViews(String[] viewnames))
        
        
# Schemareading Tests

* Schema 
  * DS anlegen. Erstellt alle noch nicht vorhandenen DS inklusive Attribute und weist ihnen default - Werte zu
  * ModelSchema anlegen
  * ModelSchema aktualisieren
* PostgresTable
  * Aktualisieren (Mit Kindern)
* 

Methode: DataTheme actualizeWithDbCat(String dbName, String schemaName, String[] tableNames)

Bei bestehenden Änderungen: showUnsavedChangesDialog() oder so.  https://doc.cuba-platform.com/manual-7.2/screen_validation.html
   
# Version 1.x
- Exception Messages: 
  - Entweder hardcodiert englisch oder in *.properties deutsch
  - Ersatz der Konkatenierungen durch Verwendung von MessageFormat.format(). (Für Nachricht-Strings mit Parametern)
  - In ganzer codebasis suchen nach "Exception(", damit sollten die meisten Fälle gefunden werden.

# Refactor

## Jederzeit

* Grooming der Screens
  * Tabellen
    * Settings disable
    * Spaltenweiten
    * Row-Header für edit-Rows (falls verfügbar)
  * Publikations-Datum, ... raus
  
## Nach Vorliegen der migrierten Daten

* Ablauf mit Schemaauslesen prüfen.


# Neu

## Middleware-Service "Dependencies"

Signatur:
* Input:
  * UUID des postgrestable
  * (Abhängigkeitstypen)
    * Alle
    * Nur Daten
    * Nur Produkte
* Output
  * Abhängigkeitstyp (Produkt, Datenfluss, Komponente, Weitere (CCC, ...))
  * Typ-Sort (Sortindex des Typs)
  * Informationen zum abhängigen Objekt
    * Name - Voll qualifiziert
    * Bezug zu Tabelle - "Tabelle in dieser Datei des GRETL-Jobs gefunden"
    
# Aenderungen gegenüber Migrationsversion
* bytea für Customlegenden in SingleActor
* Neue Entity StyleAsset als Kind der DataSetViews
* Login muss gesetzt werden (Siehe dev runtime settings)
* Beziehung FeatureInfo - DSV als Attributierte n:M Beziehung. 
  * In diesem Zusammenhang überlegen, wie angezeigt wird, dass eine spezielle Featureinfo vorliegt.
  Am einfachsten indem der Alias nicht gesetzt ist.
* DataSetView_SearchTypeEnum geändert von int auf string

# Findings zu Erstversion der Datenmigration
* Karten haben keine Kinder
* Publikationstyp muss noch aus "In WMS verfügbar" abgeleitet werden
* Rohdaten publiziert aus "In WFS verfügbar" ableiten
* Suchtyp muss abgefüllt werden
* Mit den QML scheint etwas nicht zu stimmen. Nach Runterladen, Hochladen kommt beim Speichern die Fehlermeldung "PSQLException: ERROR: invalid byte sequence for encoding "UTF8": 0x00". Fehler tritt beim Hochladen und Speichern von "neuen" QML nicht auf.

## Skript für Anpassung SearchType

```sql
alter table SIMIPRODUCT_DATA_SET_VIEW rename column search_type to search_type__u46724 ;
alter table SIMIPRODUCT_DATA_SET_VIEW alter column search_type__u46724 drop not null ;
alter table SIMIPRODUCT_DATA_SET_VIEW add column SEARCH_TYPE_INT integer ^
update SIMIPRODUCT_DATA_SET_VIEW set SEARCH_TYPE_INT = 10 where SEARCH_TYPE_INT is null ;
alter table SIMIPRODUCT_DATA_SET_VIEW alter column SEARCH_TYPE_INT set not null ;
alter table SIMIPRODUCT_DATA_SET_VIEW add column SEARCH_TYPE varchar(50) ^
update SIMIPRODUCT_DATA_SET_VIEW set SEARCH_TYPE = '1_no_search' where SEARCH_TYPE is null ;
alter table SIMIPRODUCT_DATA_SET_VIEW alter column SEARCH_TYPE set not null ;
```

# Issues

## State of code-generated entities

### Entities are not displayed in form
    