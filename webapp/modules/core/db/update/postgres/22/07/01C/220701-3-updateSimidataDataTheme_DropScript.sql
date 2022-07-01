--je
insert into SIMIDATA_DB_SCHEMA select * from SIMIDATA_DATA_THEME ^

alter table SIMIDATA_POSTGRES_TABLE alter column DB_SCHEMA_ID set not null ^

drop table SIMIDATA_DATA_THEME ;
--end je
