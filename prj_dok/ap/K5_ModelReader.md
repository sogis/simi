# ModelReader

## Zurückzugebende Informationen für eine Tabelle

* Schemainformationen
    * schemaName (ModelSchema)
    * modelName (ModelSchema)
* Tabelleninformationen
    * tableName (TableDS)
    * descriptionInDb (PostgresTable). Mit Schreiben von "DataSet.description", sofern dieses NULL ist.
    * idFieldName (PostgresTable)
    * geoFieldName (PostgresTable)
    * geoType (PostgresTable)
    * geoEpsgCode (PostgresTable)
* Attributinformationen
    * name (TableField)
    * typeName (TableField)
    * strLength (TableField)
    * catSynced (TableField)
    * descriptionInDb (TableField)
    
Bemerkung: In K3 wird definiert, wo und wie der ModelReader im GUI aufgerufen wird. Wahrscheinlich wird der Aufruf
"pro Tabelle" am Besten sein, da nicht alle Tabellen eines ili2pg Schemas ausgelesen werden.

## Services des ModelReader

Es werden zwei Services benötigt:
1. TableSearch: Gibt aufgrund einer Sucheingabe des Benutzers die treffenden Tabellen (mit jeweiligem Schema) zurück.
1. TableInfo: Gibt detaillierte Informationen zu einer Tabelle zurück.

Die Services werden im REST-"Jargon" implementiert, primär weil damit die Verwendung des TableInfo-Service für uns
Menschen einfacher ist.

## Signatur von TableSearch

### Request
* URL: [BasisURL]/[Datenbankname]
* Parameter
    * "schema": (Teil-)Name des Schemas der gesuchten Tabelle
    * "table": (Teil-)Name der gesuchten Tabelle
    
Beispiel: https://geo.so.ch/simi/tsearch/edit/?schema=igel&table=sta
  
### Response (Json)

```json
{
	"table_view_list": [
        {
			"table_view": "stall",
			"schema": "afu_igel_pub"
		},
		{
			"table_view": "standort",
			"schema": "afu_igel_pub"
		}
	],
	"truncatedTo": 2
}
```

"table_view_list" ist nach "table_view", "schema" sortiert.

"truncatedTo":
* NULL, falls alle treffenden Resultate zurückgegeben wurden
* Anzahl der zurückgegebenen Tabellen

## Signatur von TableInfo


## Umsetzungsentscheid

Der Modelreader kann ausschliesslich mittels SQL-Abfragen auf Katalog und Datenbank umgesetzt werden.

## Queries

### TableSearch

```sql
select
    ns.nspname as schema_name,
    tv.relname as tv_name,
    tv.relkind as tv_typ
from 
    pg_class tv 
join
    pg_namespace ns
        on tv.relnamespace = ns.oid
where 
        tv.relkind in ('r','v')
    and
        tv.relname like '%sta%'
    and 
        ns.nspname like '%igel%'
```

### TableInfo

#### Table

```sql
with

table_view_base as  (
	select
		ns.oid as tv_schema_oid,
		ns.nspname as tv_schema_name,
		tv.oid as tv_oid,
		tv.relname as tv_name,
		tv.relkind as tv_typ
	from 
		pg_class tv 
	join
		pg_namespace ns
			on tv.relnamespace = ns.oid
	where 
		relkind in ('r','v')
),

table_pk as ( -- pk attribut einer tabelle, sofern pk genau ein attribut umfasst
	select
		con.conrelid as table_oid,
		att.attname as pkattr_name
	from	
		pg_constraint con
	join
		pg_attribute att
			on con.conrelid = att.attrelid and con.conkey[1] = att.attnum
	where
			con.contype = 'p'
		and
			array_length(con.conkey, 1) = 1
),

table_view_desc as (
	SELECT 
		classoid AS tv_oid,
		description AS tv_desc
	FROM 
		pg_catalog.pg_description 
),

table_view_all as (
	select 
		tv_schema_name,
		tv_name,
		tv_typ,
		tv_desc,
		pkattr_name as table_pkattr_name
	from
		table_view_base
	left join
		table_pk
			on table_view_base.tv_oid = table_pk.table_oid
	left join
		table_view_desc 
			on table_view_base.tv_oid = table_view_desc.tv_oid
)

select * from table_view_all where tv_schema_name = 'afu_igel_pub' and tv_name = 'igel_stall'
```

#### Attribute einer Tabelle

#### "Hauptmodell" des Schemas

-- Informationen zu einem Attribut (Name, datentyp, zwingend, zugehörige Tabelle)

SELECT 
	attrelid AS tv_oid,
	attname AS att_name,
	typname AS att_typ,
	attlen AS att_typ_length,
	attnotnull AS att_mandatory
FROM 
	pg_attribute
JOIN	
	pg_catalog.pg_type 
		ON atttypid = pg_type.oid
		
		

WITH 

att_desc AS (
SELECT 
	objoid AS tv_oid,
	objsubid AS att_num,
	description AS att_desc
FROM 
	pg_catalog.pg_description 
)

SELECT * FROM relation_desc