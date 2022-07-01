alter table SIMIDATA_POSTGRES_TABLE add column DESCRIPTION_OVERRIDE text ;
alter table SIMIDATA_POSTGRES_TABLE add column TITLE varchar(255) ^
update SIMIDATA_POSTGRES_TABLE set TITLE = '' where TITLE is null ;
alter table SIMIDATA_POSTGRES_TABLE alter column TITLE set not null ;
alter table SIMIDATA_POSTGRES_TABLE add column TABLE_IS_VIEW boolean ^
update SIMIDATA_POSTGRES_TABLE set TABLE_IS_VIEW = false where TABLE_IS_VIEW is null ;
alter table SIMIDATA_POSTGRES_TABLE alter column TABLE_IS_VIEW set not null ;

--je
update SIMIDATA_POSTGRES_TABLE set TITLE = TABLE_NAME ;

update  SIMIDATA_POSTGRES_TABLE set TABLE_IS_VIEW = true where table_name LIKE '%_v';
-- end je
