# K6 - Service Endpoint for GRETL-Client

## Business Case

GRETL (**G**radle **E**xtract **T**ransform **L**oad) is the central "dataset mover" in our geodata infrastructure.

When the structure of source or destination tables of the dataset's change their schema, we need to know for every GRETL-Job, what
data it moves from source to destination tables.   
GRETL invokes this endpoint after each successful run of a job to register a jobs run and its dependencies on source and destination tables.

## Preconditions

The service does only register dependencies to already existing PostgresTables. If a corresponding PostgresTable entity
is missing, the service does not link and returns `false` in the linkInfo for the corresponding table.

See the following matrix to clarify how the data is manged:

|Entity|Insert|Update|Delete|
|---|---|---|---|
|PostgresTable|manual|manual|manual|
|Job|endpoint|endpoint|manual|
|LayerUsage|endpoint|endpoint|endpoint|

* manual: Edited by an end user through the generic UI
* endpoint: Edited by the endpoint "code"

DataModel for the Endpoint: ![Flow](../../simi/doc/model/flow_en.md)

The Class PostgresTable is contained in the "sibling" model "Data". For simplicity, please mock it as shown in
the diagram.

## Principal execution paths

The principal execution paths are returned as information to the calling client GRETL. They are:
* Success paths:
    * JobCreated: No job with name "jobFullPath" was found. Job and its dependencies are registered.
    * JobUpdated: The dependencies of the already registered job are not changed due to the request. Only the jobs attributes are updated.
    * TablesUpdated: Some (or all) dependencies to PostgresTable entities of the already registered job are changed due to the request.
* Failure paths:
    * Unauthorized: Authentication missing or incorrect.
    * Malformed: Request was malformed.
    * TablesMissing: At least one source and one destination table must be registered. If no source and / or destination table could be registered, the TablesMissing exception is returned.
    * Error: Any unhandled server side exception.

## Request URL

`http://geo.so.ch/simi/update/gretljob

## Request POST Content Json

```json
{
	"jobFullPath": "agi_mopublic_pub",
	"schedule": "H H(1-3) * * 7",
	"lastSuccessfulRun": "2020-03-18T15:46:38Z",
	"sourceTables": ["dbName.schemaname1.tablenameA", "dbName.schemaname1.tablenameB", "dbName.schemaname2.tablenameC"],
	"targetTables": ["dbName.schemaname3.tablenameD", "dbName.schemaname4.tablenameE", "dbName.schemaname4.tablenameF"]
}
```

The arrays are used to keep the signature simple. The clients of the endpoint do not need to fulfill any kind of ordering in
the arrays.

## Response

### Execution paths and response signature

|path|status code|response.code|response.message|response.linkInfo null?|
|---|---|---|---|---|
|JobCreated|HTTP 200|JobCreated|"Job and dependencies created."|false|
|JobUpdated|HTTP 200|JobUpdated|"Job updated (Dependencies untouched)."|false|
|TablesUpdated|HTTP 200|TablesUpdated|"Updated table dependencies and job properties."|false|
|Unauthorized|HTTP 401|Unauthorized|"Authentication missing or incorrect"|true|
|Malformed|HTTP 400|Malformed|"Request signature is malformed. See server log for details. Root error message: {Exception message}"|true|
|TablesMissing|HTTP 520|TablesMissing|"Could not create or update job, no matching source and or destination tables found"|false|
|Error|HTTP 500|Error|"An unhandled error occurred while processing the request. See server log for details. Root error message: {Exception message}"|true|

### Samples for the response "payload"

### TablesUpdated

```json
{
  "code": "TablesUpdated",
  "message": "Updated table dependencies and job properties.",
  "linkInfo": [
    {"edit.agi_dm01avso24.t_ili2db_basket": false},
    {"edit.agi_dm01avso24.t_ili2db_import": false},
    {"pub.agi_dm01avso24.bodenbedeckung": true},
    {"pub.agi_dm01avso24.gemeinde": true}
  ]
}
```

### Malformed

```json
{
  "code": "Malformed",
  "message": "Request signature is malformed. See server log for details. Root error message: Parse error after jobFullPath",
  "linkInfo": null
}
```

## Non functional requirements

### Logging

We expect that the "outer" log message is configured cuba-platform-wide with general information (timestamp, module, ...)
The request processing must be logged depending on the log level:
* Level "info": One log entry per request. The "inner" message is
    * for successful invokes: The response message, prefixed with "{jobFullPath}: " 
    * for errors and exceptions: Same as successful invokes, plus trailing stacktrace.
* One level above "info": 5-10 log outputs for the main steps of the processing, plus trailing the "info" level output.
    * Examples for the main steps: authentication validation, request parsing, entity loading, entity updating, entity saving, response building.
    
### Configuration

The url path ("/update/gretljob" in "http://geo.so.ch/simi/update/gretljob") must be configurable.
The basic auth user and password, if not configurable "elsewhere" in the platform.

If the log level is a platform wide configuration, no additional config is needed concerning the logging.

### Module dependencies

As we value lean dependencies, the endpoint should be built without depending on 
* The web portal block
* The Rest-API add on
* Or any components that are not used by the generic UI

Please declare in the quote if and why you would like to depend on additional components. A sample for a good reason is a significant reduction in development time.

### Integration test

All of the above mentioned execution paths must be covered by automatic repeatable integration tests, including database test data and database modification.

### Dependencies to other work

We value the ability to work without inter-dependencies more that getting a fully integrated solution of all work done.

Therefore, we would like you to work completely independent of the team that creates the entities and generic UI for connected parts of the whole datamodel.

The Sequence of work for the endpoint will be:
* Contract for the endpoint 
    * Quote (Your team)
    * Clarifications of this specification (Our Team)
    * Contract (together)
* Implementation for the endpoint (Your team)
    * Includes creating a mock PostgresTable entity
* Confirmation of the completed implementation (Our team)
    * This ends the contract.
* Integration of the implementation into the "whole datamodel" solution (Our team)

### Deliverables

A github repo with the endpoint code for:
* entities
* database create and update scripts for the entities
* endpoint
* integration tests (with code to initialize with test data for each test case)
* The generic user interface is not part of this work package. If helpful for development, a simple generic user interface can be created.   

and a readme.md stating how to:
* Start the server (start the cuba-platform)
* Invoke the endpoint with curl (or similar)
* Start the integration tests

The code must be "open source".

Note: If you think this will result in a good example on how to build a endpoint, feel free to use it as tutorial,
part of the manual, ...