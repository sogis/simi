-- update SIMIDATA_POSTGRES_DB set TITLE = <default_value> where TITLE is null ;
alter table SIMIDATA_POSTGRES_DB alter column TITLE set not null ;
-- update SIMIDATA_POSTGRES_DB set IDENTIFIER = <default_value> where IDENTIFIER is null ;
alter table SIMIDATA_POSTGRES_DB alter column IDENTIFIER set not null ;
