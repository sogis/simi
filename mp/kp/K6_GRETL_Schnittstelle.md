# K6 - Service Endpoint for GRETL-Client

## HTTP Service

### Request URL

`http://geo.so.ch/simi/update/gretljob

### Request POST Content Json

```json
{
	"jobName": "agi_mopublic_pub",
	"schedule": "H H(1-3) * * 7",
	"lastSuccessfulRun": "2020-03-18T15:46:38Z",
	"sourceTables": ["schemaname1.tablenameA", "schemaname1.tablenameB", "schemaname2.tablenameC"],
	"targetTables": ["schemaname3.tablenameD", "schemaname4.tablenameE", "schemaname4.tablenameF"]
}
```

### Response

#### Successes

When no server side exception occured, and at least one job execution could be registered:
* HTTP 200
* a code denoting the action performed by the server (mandatory).
* a short message describing the action (mandatory)
* a list of ignored tables (optional - null if no tables where ignored)

```json
{
  "code": "PartialUpdate",
  "message": "Table dependencies updated; some of the submitted tables were ignored",
  "ignoredTables": [
    "edit.agi_dm01avso24.t_ili2db_import",
    "edit.agi_dm01avso24.t_ili2db_basket",
    "edit.agi_dm01avso24.t_ili2db_import",
    "edit.agi_dm01avso24.t_ili2db_basket",
    "edit.agi_dm01avso24.t_ili2db_import",
    "edit.agi_dm01avso24.t_ili2db_basket"
  ]
}
```

```json
{
  "code": "FullUpdate",
  "message": "All table dependencies updated",
  "ignoredTables": null
}
```

#### Errors

HTTP Code 400: Bad Request

```json
{
  "code": "InvalidJson",
  "message": "Invalid JSON content submitted",
  "ignoredTables": null
}
```

HTTP Code 404: Not Found

```json
{
  "code": "TablesNotFound",
  "message": "None of the submitted tables found, request ignored",
  "ignoredTables": [
    "edit.agi_dm01avso24.t_ili2db_import",
    "edit.agi_dm01avso24.t_ili2db_basket",
    "edit.agi_dm01avso24.t_ili2db_import",
    "edit.agi_dm01avso24.t_ili2db_basket",
    "edit.agi_dm01avso24.t_ili2db_import",
    "edit.agi_dm01avso24.t_ili2db_basket"
  ]
}
```

HTTP Code 500: Internal Server Error

```json
{
  "code": "ServerError",
  "message": "Error while processing request received at time 19:34 - see server log",
  "ignoredTables": null
}
```

Todo ausformulieren:
* Weitere Fehlerfälle...?
* Weitere Successfälle:
    * Registered Job - Job hat es noch nicht gegeben und wurde registriert.
* Weitere Details:
    * linkedTables - Tabellen wurden neu mit Job verlinkt.
    * unlinkedTabkes - Tabellen wurden aus Job entfernt. 
* Verweisen auf das Teilmodell (simi_dependency.md)



/* Umsetzungsfragen

Spezifikation, wie die GRETL-Schnittstelle mittels cuba-platform die
entsprechenden Entitäten pflegt / aktualisiert.
*/
