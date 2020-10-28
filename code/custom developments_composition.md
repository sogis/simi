# Betreff joining entities

# Facadelayer

## Childtabelle in Screen

"scharfer" Code
```xml
        <groupBox id="dataSetViewsBox" caption="msg://ch.so.agi.simi.entity.product/FacadeLayer.dataSetViews">
            <table id="dataSetViewsTable" dataContainer="dataSetViewsDc" width="100%" editable="true">
                <actions>
                    <action id="addDataSetView" caption="Hinzufügen" icon="ADD_ACTION"/>
                    <action id="exclude" type="remove" caption="Ausschliessen"/>
                    <action id="sortAction" caption="Sortieren" icon="SORT"/>
                </actions>
                <columns>
                    <column id="dataSetView.identifier"/>
                    <column id="dataSetView.title"/>
                    <column id="transparency" editable="true"/>
                    <column id="sort" editable="true"/>
                </columns>
                <buttonsPanel>
                    <button id="btnAddDataSetView" action="dataSetViewsTable.addDataSetView"/>
                    <button id="btnExclude" action="dataSetViewsTable.exclude"/>
                    <button id="btnSortAction" action="dataSetViewsTable.sortAction"/>
                </buttonsPanel>
            </table>
        </groupBox>
```

Prototyp:
```xml
        <groupBox id="dataSetListPropertiesBox" caption="Liste der DataSets">
            <table id="dataSetListPropertiesTable" dataContainer="dataSetListPropertiesDc" width="100%" height="200px"
                   editable="true">
                <actions>
                    <action id="create" type="create"/>
                    <action id="edit" type="edit"/>
                    <action id="remove" type="remove"/>
                </actions>
                <columns>
                    <column id="dataset" caption="Dataset" width="600"/>
                    <column id="sort" editable="true" width="80" caption="Sort."/>
                    <column id="visible" editable="true" caption="Sicht." width="80" sortable="false"/>
                    <column id="transparency" caption="Trans. [%]" editable="true" width="100" sortable="false"/>
                </columns>
                <buttonsPanel>
                    <linkButton id="btnSortTable" caption="Neu sortieren"/>
                    <linkButton action="dataSetListPropertiesTable.create" caption="Listeneintrag erstellen"/>
                    <linkButton action="dataSetListPropertiesTable.edit" caption="Listeneintrag editieren"/>
                    <linkButton action="dataSetListPropertiesTable.remove" caption="Listeneintrag löschen"/>
                </buttonsPanel>
            </table>
        </groupBox>
```


```java
    @Subscribe("dataSetViewsTable.addDataSetView")
    public void onSingleActorsTableAddSingleActor(Action.ActionPerformedEvent event) {
        screenBuilders.lookup(DataSetView.class, this)
                .withLaunchMode(OpenMode.DIALOG)
                .withSelectHandler(dataSetViews -> {
                    dataSetViews.stream()
                            .map(this::createPropertiesInFacadeFromDataSetView)
                            .forEach(this::addToPropertiesInFacade);
                })
                .build()
                .show();
    }
```      
Entities
```java
// Facadelayer - Scharf
@Composition
@OnDelete(DeletePolicy.CASCADE)
@OneToMany(mappedBy = "facadeLayer")
private List<PropertiesInFacade> dataSetViews;

// Facadelayer - Proto
@OrderBy("sort")
@Composition
@OnDelete(DeletePolicy.CASCADE)
@OneToMany(mappedBy = "facadelayer")
protected List<DatasetListProperties> dataSetListProperties;

// Props in List - Scharf
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "FACADE_LAYER_ID")
@NotNull
private FacadeLayer facadeLayer;

@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "DATA_SET_VIEW_ID")
@NotNull
private DataSetView dataSetView;

// Props in List - Proto
@NotNull
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "FACADELAYER_ID")
protected FacadeLayer facadelayer;

@NotNull
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "DATASET_ID")
protected PostgresDS dataset;

// DataSetView - Scharf
@Composition
@OnDelete(DeletePolicy.CASCADE)
@OneToMany(mappedBy = "dataSetView")
private List<PropertiesInFacade> facadeLayers;

// PostgresDS - Proto (Entspricht DataSetView der scharfen Implementation)

// Hat kein Verweis auf einen Facadelayer. Ist ok, solange PostgresDS nicht gelöscht wird 
```    
