# Featureinfo

![Featureinfo](../puml/rendered/simi_featureinfo.png) 

## Klasse Featureinfo

Enthält die Informationen bezüglich der verwendeten Quelle für die Layerabfrage 
und das Rendering-Template für die Ausgabe.

### Attributbeschreibung

|Name|Typ|Z|Beschreibung|
|---|---|---|---|
|displayTemplate|String|j|Jinja-Template, welches das Layout der Featureinfo-Ausgabe bestimmt.|
|sqlQuery|String|n|Query, welches gegenüber der konfigurierten Postgres-DB abgesetzt wird.|
|pyModuleName|String(100)|n|Name des Python-Modules, welches für die Informationsabfrage genutzt wird.|
|remarks|String|n|Interne Bemerkungen zur Konfig.|

### Konstraints

* Jedes Featureinfo muss genau einen "is_for_layer" Eintrag in der Klasse LayerRelation haben.
* Entweder sqlQuery oder pyModuleName muss Null sein.

## Klasse LayerRelation

Bildet die Beziehungen des Featureinfo zu einer DataSetView ab:
* **"is_for_layer":** Ziel-Layer, für welchen die Featureinfo erstellt wurde.
* **"queries":** Aus der Ebene wird via SQL oder Modul Informationen für dieses Featureinfo bezogen.

Mittels der "queries"-Verknüpfungen werden die Permissions geprüft und "Know your GDI" sichergestellt.
Bei einem sqlQuery wird über die erste queries-Verknüpfung die Datenbank ermittelt, auf die das Query abgesetzt wird.

### Attributbeschreibung

|Name|Typ|Z|Beschreibung|
|---|---|---|---|
|relation|enum|j|Beziehungstyp zwischen Featureinfo und DSV (is_for_layer, queries).|

### Konstraints
UK auf FK's und "relation".

## Mapping auf den Inhalt von featureInfoConfig.json

|cccConfig.json|simi|Bemerkungen|
|---|---|---|
|$schema|globals.featureinfo.schemaURI||
|service|globals.featureinfo.serviceName||
|config.default_qgis_server_url|globals.featureinfo.wmsFeatureInfoServerURL||
|config.#default_info_template|globals.featureinfo.defaultJinyaTemplate||
|resources.wms_services.name|globals.featureinfo.*||
|resources.wms_services.root_layer.name|globals.featureinfo.*||
|resources.wms_services.root_layer.type|globals.featureinfo.*||
|resources.wms_services.root_layer.title|globals.featureinfo.*||
|---|---|---|
|**Abschnitt für DataSetView**|-|-|
|resources.wms_services.root_layer.layers.name|DataSetView.identifier||
|resources.wms_services.root_layer.layers.title|DataSetView.title||
|resources.wms_services.root_layer.layers.type|Wert: "layer"||
|resources.wms_services.root_layer.layers.attributes.name|DataSetView -> ViewField -> TableField.Name||
|resources.wms_services.root_layer.layers.attributes.alias|DataSetView -> ViewField.alias||
|---|---|---|
|**Abschnitt für FacadeLayer**|-|-|
|resources.wms_services.root_layer.layers.name|FacadeLayer.identifier||
|resources.wms_services.root_layer.layers.type|Wert: "layergroup"||
|resources.wms_services.root_layer.layers.title|FacadeLayer.title||
|resources.wms_services.root_layer.layers.layers.name|FacadeLayer -> PropertiesInFacade -> DataSetView.identifier||
|resources.wms_services.root_layer.layers.layers.title|FacadeLayer -> PropertiesInFacade -> DataSetView.title||
|resources.wms_services.root_layer.layers.layers.type|Wert: "layer"||
|resources.wms_services.root_layer.layers.layers.attributes.name|FacadeLayer -> PropertiesInFacade -> DataSetView -> ViewField -> TableField.Name||
|resources.wms_services.root_layer.layers.layers.attributes.alias|FacadeLayer -> PropertiesInFacade -> DataSetView -> ViewField.alias||

$td Wieso sind in den Config-Beispielen auch LayerListen enthalten?
