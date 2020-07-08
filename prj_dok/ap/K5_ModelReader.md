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

## Signatur des Service

### Request-Parameter

* db: Exakter Name der Datenbank.
* query: Abfragestring, mit welchem anhand der Informationen zu Schema- und Tabellenname die zutreffenden Tabellen gefunden werden.

### Response (Json)

limitedTo






## Umsetzungsentscheid

Der Modelreader kann ausschliesslich mittels SQL-Abfragen auf Katalog und Datenbank umgesetzt werden.

## Queries

### Tabelle und View

```sql
with 

table_view_base as  (
	select
		ns.oid as schema_oid,
		ns.nspname as schema_name,
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

table_view_all as (
	select 
		table_view_base.*,
		table_pk.pkattr_name
	from
		table_view_base
	left join
		table_pk
			on table_view_base.tv_oid = table_pk.table_oid
)
```