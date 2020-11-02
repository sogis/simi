# Betreff joining m:n entities

Im Datenmodell von SIMI gibt es viele m:n Verbindungen, bei welchen auf der 
Verknüpfungstabelle noch wenige Zusatzinformationen vorhanden sind.

Beispiel für Zusatzinformationen: Sortierindex einer DataSetView innerhalb 
eines Facadelayer.

Es gibt drei "einfache" Möglichkeiten, wie die Pflege der Verknüpfungen erfolgen kann:
* Standardfunktionalität
  * Öffnet ein Edit-Screen der Zeile der Verknüpfungstabelle
* Listenauswahl in neuem Fenster: 
  * Öffnet den Browse-Screen der ":n" Entity (Im Beispiel der DataSetViews)
  * Die Zusatzinformationen der Verknüpfkungstabelle werden in der Kind-Ansicht 
  des Edit-Screens der "m:" Entity gepflegt (Im Beispiel der Facadelayer)
  * Bedingt custom code zur Erstellung der Zeile der Verknüpfungstabelle
  * How to siehe Tutorial [Many-to-Many Association](https://www.cuba-platform.com/guides/data-modelling-many-to-many-association)
* Auswahl in Combobox: 
  * Zuweisung erfolgt in Kind-Ansicht des Edit-Screens der "m:" Entity.
  * In der Tabellenansicht der Verknüpfungstabelle kann mittels Combobox die 
  "n:" Entity verknüpft werden.
  
## "Auswahl in Combobox": Konfiguration am Beispiel der Rollen

**Controller-XML:** Datencontainer

```xml
<data>
    <instance id="dataProductDc">
    </instance>
    <collection id="rolesDc" class="ch.so.agi.simi.entity.iam.Role" view="_minimal">
        <loader id="rolesDl">
            <query>
                <![CDATA[select e from simiIAM_Role e]]>
            </query>
        </loader>
    </collection>
</data>  
```

**Controller-XML:** Konfiguration der Combobox-Spalte in der Tabelle

```xml
<groupBox id="permissionsBox" caption="msg://ch.so.agi.simi.entity.product/DataSetView.permissions">
    <table id="permissionsTable" dataContainer="permissionsDc" width="100%" height="200px" editable="true">
        <columns>
            <column id="role" editable="true" optionsContainer="rolesDc" sort="ASCENDING"/>
        </columns>
        <buttonsPanel>
            <button id="addPermissionBtn" icon="ADD_ACTION" caption="Erstellen" primary="true"/>
        </buttonsPanel>
    </table>
</groupBox>
```

**Controller-Java:** Eventhandler für Click auf "addPermissionBtn"

Erstellt eine Permission als neue Zeile der Verknüpfungstabelle und gibt
der Spalte role darin den Fokus.

```java
@Subscribe("addPermissionBtn")
public void onAddPermissionBtnClick(Button.ClickEvent event) {
    Permission permission = dataContext.create(Permission.class);
    permission.setDataSetView(this.getEditedEntity());

    permissionsDc.getMutableItems().add(permission);

    permissionsTable.requestFocus(permission, "role");
}
```