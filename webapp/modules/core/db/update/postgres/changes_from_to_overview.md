# Simi Version 2 Datenbank-Änderungen

In den folgenden Tabellen sind die gemachten Änderungen an der Meta-DB notiert sowie der Bezug zu den vom Cuba-Framework
automatisch durchnummerierten Änderungs-Dateien (Angabe jeweils ohne Suffix .sql).

Bemerkung: Das Framework macht 

## 1. Juli 2022

|Änderung|Ordner|
|---|---|
|Split DbName in Identifier und Titel|01A|
|Erstellen DbSchema|01B|
|Migration DbSchema nach DbSchema|01C|
|PostgresTable: Attributanpassung für V2|01D|
|DSV und Attribute: Anpassungen V2|01E|
|User: Vor- und Nachname entfernt|01F|

## 4. Juli 2022

|Änderung|Ordner|
|---|---|
|Product: Stichworte und Synonyme in ThermGroup|02B|
|Theme: Organisation (Contact)|02D|

## 5. Juli 2022

|Änderung|Ordner|
|---|---|
|Theme: Alle weiteren Klassen ausser Package SubArea|05A|

## 6. Juli 2022

|Änderung|Ordner|
|---|---|
|DataProduct: Anpassung Identifier|06B|

## 25. Juli 2022

|Änderung|Ordner|
|---|---|
|Theme.SubArea Klassen|25A|
|ThemePublication: Title Override, Remarks|25B|

## Nach den Sommerferien

Package Thema abschliessen mit den Entities zur Datenabdeckung.   
Überlegen, welche Informationen direkt aus dem GRETL-Job folgen, und für welche es in SIMI Masken braucht.
