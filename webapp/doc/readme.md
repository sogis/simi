# SIMI

Webapplikation zur Pflege der Metainformationen der GDI-SO (**S**patial **I**nfrastructure **M**etadata **I**nterface)

Die Doku des Metamodells ist hier zuhause: [Doku des Metamodelles](metamodel.md)

## Schema für SIMI und Cuba anlegen

Dies erfolgt mittels vier Datenbankskripten:
* Mit [platform_schema/create-db.sql](platform_schema/create-db.sql) werden die Tabellen des Cuba Frameworks angelegt.
* Mit den *.create-db.sql im Ordner <https://github.com/simi-so/simi/tree/master/code/modules/core/db/init/postgres> 
  die SIMI-Tabellen, Indexes, ...
  
ZU BEACHTEN: Das Framework verwendet als Abschlusszeichen eines SQL Befehls "^" und nicht ";".

Damit man sich nicht darum kümmern kann auch wie hier beschrieben vorgegangen werden:
<https://doc.cuba-platform.com/manual-latest/db_update_in_prod_cmdline.html>

## Setzen des Loglevels

Der Loglevel kann bequem mittels der vom Framework zur Verfügung gestellten Admin-Masken zur Laufzeit geändert werden.

Falls eine Fragestellung vor der Verfügbarkeit der Admin-Masken analysiert werden muss, kann der Loglevel in der
Datei **\[repo root\]/webapp/docker/image/uber-jar-logback.xml** wie folgend beschrieben angepasst werden (Bedingt neuen Build des Image):

Anpassung des levels, welcher überhaupt auf die Konsole geschrieben wird:

    <root level="debug">
        <appender-ref ref="Console"/>
    </root>

Anpassung des log-levels für das Cuba-Framework:

    <logger name="com.haulmont.cuba" level="DEBUG"/>
    
Anpassung des log-levels für Simi:

    <logger name="ch.so.agi.simi" level="DEBUG"/>
  
## Kopieren von Data-Products

Die im GUI harmlos erscheinende Kopierfunktion ist aufgrund der vielen zu berücksichtigenden Beziehungen
ziemlich komplex.

Die Klassen innerhalb des Kopierkontextes werden kopiert (dupliziert), die ausserhalb werden referenziert. 
Nach dem Kopiervorgang zeigt also sowohl das Original wie auch die Kopie auf das gleiche ausserhalb des
Kopierkontextes liegende Objekt.

Hinweis: Die Vererbungen sind in den Diagrammen nicht abgebildet.

### Tableview

![Tableview Copy](simi_resources/copy-tableview.png)

### Rasterview

![Rasterview Copy](simi_resources/copy-rasterview.png)

### FacadeLayer

![Facadelayer Copy](simi_resources/copy-facadelayer.png)

### Layergroup

![Layergroup Copy](simi_resources/copy-layergroup.png)

### Map

![Map Copy](simi_resources/copy-map.png)








