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

### REST Request-URL

[BasisURL]/[Datenbankname]/[Schemaname]/[Tabellenname]

Beispiel: https://geo.so.ch/simi/tinfo/pub/afu_igel_pub/stall

### Response (Json)

```json
{
	"model": "AFU_IGEL_PUB_20200106",
	"schema": "afu_igel_pub",
	"tableOrView": "igel_stall",
    "tvDescription": null,
	"pkColumn": "t_id",
	"columns": {
		"name": {
			"name": "name",
			"mandatory": true,
			"type": "varchar",
			"length": 255,
			"description": "Transfer-ID",
			"geoColType": null,
			"geoColSrOrg": null,
			"geoColSrID": null
		},
		"geometry": {
			"name": "geometry",
			"mandatory": true,
			"type": "geometry",
			"length": null,
			"description": "Koordinaten des Stalls",
			"geoColType": "POINT",
			"geoColSrOrg": "EPSG",
			"geoColSrID": 2056
		},
		"nr": {
			"...": "..."
		}
	}
}
```


## Bemerkung zu den Services

Wenn aufgrund der Antwortzeit möglich, wird nur der Service "TableSearch" umgesetzt, mit der Response von TableInfo
(volle Informationstiefe). Die Abfrage der Modellnamen benötigt ein Query pro zurückgegebener Tabelle, erzeugt also
bei Treffer auf 20 Tabellen 20 Queries.

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
/*
    and
        tv.relname like '%sta%'
    and 
        ns.nspname like '%igel%'
*/
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
		objoid AS tv_oid,
		description AS tv_desc
	FROM 
		pg_catalog.pg_description 
	where
		objsubid = 0
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

select * from table_view_all 
--where tv_schema_name = 'afu_igel_pub' and tv_name = 'igel_stall'
```

#### Attribute einer Tabelle

```sql
WITH 

att_base AS (
	SELECT 
		nspname AS schema_name,
		relname AS tv_name,
		attrelid AS tv_oid,
		attnum AS att_num,
		attname AS att_name,
		attnotnull AS att_mandatory,
		typname AS att_typ,
		CASE 
			WHEN typname = 'varchar' THEN atttypmod - 4
			ELSE NULL
		END AS att_length
	FROM	
		pg_namespace
	JOIN 
		pg_class 
			ON  pg_namespace.oid = pg_class.relnamespace
	JOIN 
		pg_attribute
			ON pg_class.oid = pg_attribute.attrelid
	JOIN 
		pg_type 
			ON pg_attribute.atttypid = pg_type.oid
	WHERE
		attnum > 0 --exclude system columns
),

geo_att AS (
	SELECT 
		f_table_schema AS geo_schema_name,
		f_table_name AS geo_table_name,
		f_geometry_column AS geo_att_name,
		"type" AS geo_col_type,
		srid AS geo_col_srid   
	FROM 
		geometry_columns gc 
),

att_desc AS ( 
	SELECT 
		objoid AS tv_oid, 
		objsubid AS att_num, 
		description AS att_desc 
	FROM 
		pg_description 
)

SELECT
	schema_name,
	tv_name,
	att_name,
	att_mandatory,
	att_typ,
	att_length,
	att_desc,
	geo_col_type,
	geo_col_srid   
FROM 
	att_base
LEFT JOIN 
	att_desc
		ON att_base.tv_oid = att_desc.tv_oid 
		AND att_base.att_num = att_desc.att_num
LEFT JOIN 
	geo_att
		ON att_base.schema_name = geo_att.geo_schema_name
		AND att_base.tv_name = geo_att.geo_table_name
		AND att_base.att_name = geo_att_name
--WHERE schema_name = 'afu_igel_pub' AND tv_name NOT LIKE 't_ili2db%'
```

#### "Hauptmodell" des Schemas

Wird ermittelt, falls:
* die ili2db Metatabelle "t_ili2db_model" im entsprechenden Schema existiert
* in "t_ili2db_model" genau ein Modell mit Anfangsnamen "SO_" existiert 

```sql
SELECT 
	split_part(modelname, '{', 1) AS model_name
FROM 
	arp_npl_pub.t_ili2db_model
WHERE 
	modelname LIKE 'SO_%'
```

# Entscheide

|Thema|Entscheid|Bemerkungen|
|---|---|---|
|Auslesen der Informationen aus Geo-DB's / Ili-Repo|Das Auslesen erfolgt ausschliesslich mittels SQL auf die Geo-DB's|Voraussichtlich wird für den Datenbezug lediglich der Modellname benötigt. Dieser kann ebenfalls mittels SQL aus t_ili2db_model ausgleesen werden.|
|Scope Tabelle oder Schema|Voraussichtlich wird das API den Scope Tabelle haben.|Definitiver Entscheid erfolgt in K3.|
|Umgang mit nicht vorhandenen Informationen|Diese werden explixit als json:null in der Response zurückgegeben.|API-Klarheit ist (hier) wichtiger als die Transfergrösse.|
|Benennung der Komponente|Neu: SchemaReader|Die Komponente liest grossmehrheitlich Informationen aus einem Schema von PostgreSQL aus und heisst darum folgerichtig **Schemareader**.| 