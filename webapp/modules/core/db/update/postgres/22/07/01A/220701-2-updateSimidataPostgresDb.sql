alter table SIMIDATA_POSTGRES_DB rename column db_name to db_name__u44525 ;
alter table SIMIDATA_POSTGRES_DB alter column db_name__u44525 drop not null ;
-- alter table SIMIDATA_POSTGRES_DB add column TITLE varchar(100) ^
-- update SIMIDATA_POSTGRES_DB set TITLE = <default_value> ;
-- alter table SIMIDATA_POSTGRES_DB alter column TITLE set not null ;
alter table SIMIDATA_POSTGRES_DB add column TITLE varchar(100) ;
-- alter table SIMIDATA_POSTGRES_DB add column IDENTIFIER varchar(100) ^
-- update SIMIDATA_POSTGRES_DB set IDENTIFIER = <default_value> ;
-- alter table SIMIDATA_POSTGRES_DB alter column IDENTIFIER set not null ;
alter table SIMIDATA_POSTGRES_DB add column IDENTIFIER varchar(100) ;
