# Flow

![Flow](../puml/rendered/simi_flow_en.png)

Describes the Relationships of a GRETL-Job to source and target PostgresTable.

### Class Job

GRETL-Job that moves data from sorce to destination tables. 

#### Attribute description

|Name|Type|M|Description|
|---|---|---|---|
|repoPath|String(200)|j|Path of the Job relative to the Job repository root. Sample: "afu_baugrundklassen_pub".|
|schedule|String(100)|j|Information on the configured "timetable" for the job to run (daily, weekly, on demand, ...)|
|lastSuccessfulRun|DateTime|j|Timestamp of the last succesful execution of the job|

#### Constraints

"repoPath" is unique.

### Klasse LayerUsage

Link table with attributes. 

#### Attribute description

|Name|Type|M|Description|
|---|---|---|---|
|accessType|enum|j|Values of the Enumeration: "read" (default) and "write".|

#### Constraints

Unique key over:
* Foreign key to Job
* Foreign key to PostgresTable
* accessType enumeration




 
  
 





