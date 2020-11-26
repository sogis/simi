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

* Beans in entsprechende subpackages
  Hilfsklassen und -Interfaces in das Package des Beans
  
## Nach Vorliegen der migrierten Daten

* Browse-Zwischenscreens ersetzen mit direkter Auswahl in Edit-Form

* Bei attributierten m:n Beziehungen: Direktes Auswählen in Kind-Tabelle des Edit-Form mittels Dropdown
  * Rollenzuweisung
  * Attributauswahl in DataSetView
  * Zuweisen DSV zu Facade
  * Zuweisen SA zu Layergruppe oder Karte

* Ablauf mit Schemaauslesen prüfen.

* Identifier für die verschiedenen Klassen setzen (Karte, Gruppe, Facade, Vector, Raster, Tabelle)

# Neu

## Middleware-Service "Depencencies"

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

    