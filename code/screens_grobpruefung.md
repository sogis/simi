# Screens - Grobprüfung

## Data

### OK PostgresDB

### OK DataTheme

### OK PostgresTable

### OK RasterDS

### OK Raster View

### OK Table View

### OK Layergroup

### OK Fassade

### Map

Grobprüfung der übergreifenden Screen-Eigenschaften:
1. Bezeichnung in Menu und Reiter (Titel) ident und verständlich?
1. Entfernen von Verknüpfungseinträgen ohne Bestätigungsdialog
1. Layout der Tabellen (Spaltenbreiten) ok?
   * Bei Default-Grösse?
   * Beim weiteren Aufziehen?  
1. Gibt es weitere Findings (--> Screenshots)?


### TEMPLATE

Grobprüfung der übergreifenden Screen-Eigenschaften:
1. Bezeichnung in Menu und Reiter (Titel) ident und verständlich?
1. Entfernen von Verknüpfungseinträgen ohne Bestätigungsdialog
1. Layout der Tabellen (Spaltenbreiten) ok?
   * Bei Default-Grösse?
   * Beim weiteren Aufziehen?  
1. Gibt es weitere Findings (--> Screenshots)?

### Löschen...

                <actions>
                    <action id="unlink" type="remove" caption="Verknüpfung löschen">
                        <properties>
                            <property name="confirmation" value="false"/>
                        </properties>
                    </action>
                </actions>
                
### Todos:
* Childlayerproperties: Transparenz entfernen
* Map: Edit-Formular aufräumen....
* Spaltenbreiten durchkonfigurieren
                
                
# Spaltenweiten:
* DataTheme - Edit
  * Letzte Synchronisierung: 200px
* PostgresTable
  * Browse
    * Letzte Synchronisierung: 200px
  * Edit
    * Synchr, Verwendungen: 180px
* role-fragment
  * Berechtigung: 150px
* TableView - Edit
  * Sortierung: 170px
* LayerGroup - Edit
  * Sortierung und Sichtbar: 170px