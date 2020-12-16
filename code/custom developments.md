# Custom Developments

- CopyDataProductBean
   - deep copy of some DataProducts
   - **List<Function<E, List<? extends StandardEntity>>>**
- StyleUploadDownloadBean
   - upload and check version of .qml
   - **void handleFileUploadSucceed(FileUploadField uploadField, Consumer<String> assignResult)** Kann das auch "String handleFileUploadSucceed(FileUploadField uploadField)" sein?
- PostgresTableEdit
   - read json in SchemaReaderBean
   - generate TableFields from read data and update catSynced
- PubScopeFragment
   - set default value in onInitEntity
   - programmatically add JpqlCondition
- FilterFragment
   - simple filter functionality for browse screens
- DataProductBrowse
   - create PropertiesInList / PropertiesInFacade joining entities when edit screen closes. **Verstehe / finde ich nicht...**
   - use *remove* action to properly remove joining entities. **Verstehe / finde ich nicht...**
- LayerGroupEdit / FacadeLayerEdit
   - convert FL to LG
- Sort on table in FacadeLayerEdit, LayerGroupEdit, MapEdit and TableViewEdit
  - **In Bean extrahieren.** Zum Beispiels mittels Interface SortFieldEntity und Interfacemethode getSortField() oder ähnlich.
- RasterViewEdit / TableViewEdit
   - create new Permission entity and add it directly to the table within the current edit screen
- PostgresDBBrowse
   - check if DB is empty before deleting it
- PostgresTableBrowse
   - initializer and afterCloseListener for TableViewEdit Screen
   - generated columns to show related TableViews
- Custom validator
   - TableViewEdit for searchFilterWordField
   - FeatureInfoEdit for pyModuleNameField and sqlQueryField
   
   
**Allgemein**
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
    