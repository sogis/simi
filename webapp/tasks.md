# Version 0.9

* Done Fertigstellung der Abhängigkeitsanzeige
* Done Integrationstest: Laden aller Daten gemäss der jeweiligen XML-View
* Done Anpassung Versionierung auf Major.Minor.Revision
  * Major: Breaking Schema change
  * Minor: Breaking Config change
  * Revision: Fortlaufende Buildnummer
* Done Jenkins-Integration erstellen (für Start der Config-Pipeline) 


# Version 1.1.x

* Nach Pilotierung:
  * Screens Wegräumen
    * _layer-group-choose.xml
    * _data-set-view-browse.xml
    * _data-product_pub-scope-browse.xml
    * _data-product_pub-scope-edit.xml
    * _map-browse.xml
    * _single-actor-browse.xml
    
# Aenderungen gegenüber Migrationsversion
* bytea für Customlegenden in SingleActor
* Neue Entity StyleAsset als Kind der DataSetViews
* Login muss gesetzt werden (Siehe dev runtime settings)
* Beziehung FeatureInfo - DSV als Attributierte n:M Beziehung. 
  * In diesem Zusammenhang überlegen, wie angezeigt wird, dass eine spezielle Featureinfo vorliegt.
  Am einfachsten indem der Alias nicht gesetzt ist.
* DataSetView_SearchTypeEnum geändert von int auf string

# Schema-Änderungen auf V 1.1.x
* Aufhebung "Externe Tabelle und diesbezügliche Vererbungsstruktur" TableDS <- ExternalTable und TableDS <- PostgresTable
  * Attribute von TableDS in PostgresTable gezügelt
* geo* Felder aus TableView gelöscht (Sind Teil von PostgresTable)
* boolean wgcEdit aus TableView gelöscht
* Transparenz aus PropertiesInList und PropertiesInFacade entfernt
* Map.wgcUrlValue entfernt
* ExternalLayers: Umbenannt nach ExternlLayer, nur noch eine tech. Ebene kann referenziert werden. 

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

TableDependencyList
* GUI-Check
* Git-Abfrage: Autotest's



    
