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

## 2. August 2022

|Änderung|Ordner|
|---|---|
|Theme grooming|02A|
|Create Org-Themes and upd DProds|02B|

## 3. August 2022

|Änderung|Ordner|
|---|---|
|Remove ThermGroup and refs|03A|
|Keywords and Synonyms|03B|

## 9. August 2022

|Änderung|Ordner|
|---|---|
|TableField: IliEnum|09A|

# Todo

Schema:
- Spez Keywords and Synonyms beschreiben (SPEZ)
  - Keywords komplett bottom up?
  - Synonyme eher top down? -> braucht es auf dem Thema

- Synonyme auf Dataproduct
- Synonyme (überschreibung) auf thema
- boolean isThemeRepresenter auf Datenprodukt vergeben?
- furtherInformation-Attribut auf Thema


Verstehen, wie enum(fuu, bar) als Datentyp im SIMI landet

Requested db identifier {0} is not configured. Schemareader service has 1 dbs configured in the service


    
Kopier und Morph-Funktionen testen

# Therm-Verkettung Thema und DataProduct (Keywords, Synonyms)

```sql
/*
DROP TABLE IF EXISTS layer;

CREATE TABLE layer AS                                     
WITH t (title, therms, theme_ident) AS (
 VALUES
      ('this:x parent:-'::varchar, jsonb_build_array('a', 'b', 'c'), 'nulltherm'::varchar)
      ,('this:x parent:1'::varchar, jsonb_build_array('a', 'b', 'c'), 'onetherm'::varchar)
      ,('this:1 parent:x'::varchar, jsonb_build_array('a'), 'xtherm'::varchar)
      ,('this:x parent:x'::varchar, jsonb_build_array('a', 'b', 'c'), 'xtherm'::varchar)
      ,('this:0 parent:x'::varchar, jsonb_build_array(), 'xtherm'::varchar)
)
SELECT * FROM t;

DROP TABLE IF EXISTS theme;

CREATE TABLE theme AS                                     
WITH t (ident, therms) AS (
 VALUES
      ('nulltherm'::varchar, NULL)
      ,('onetherm'::varchar, jsonb_build_array('1'))
      ,('xtherm'::varchar, jsonb_build_array('b','1'))
)
SELECT * FROM t;
*/


WITH 

lay_agg_duplicates AS (
  SELECT 
    l.title,
    l.therms lt,
    t.therms tt,
    COALESCE(l.therms, jsonb_build_array()) || COALESCE(t.therms, jsonb_build_array()) AS agg
  FROM 
    layer l 
  LEFT JOIN 
    theme t ON l.theme_ident = t.ident
)

,lay_therms AS (
  SELECT DISTINCT
    title,
    exploded.value AS therm
  FROM
    lay_agg_duplicates
  CROSS JOIN LATERAL 
    jsonb_array_elements_text(agg) exploded
)

,lay_agg AS (
  SELECT 
    title,
    jsonb_agg(therm ORDER BY therm) AS therms
  FROM
    lay_therms
  GROUP BY 
    title
)

SELECT * FROM lay_agg;
```

