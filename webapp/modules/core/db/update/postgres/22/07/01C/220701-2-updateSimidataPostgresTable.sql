alter table SIMIDATA_POSTGRES_TABLE drop constraint if exists FK_SIMIDATA_POSTGRES_TABLE_ON_DATA_THEME ;
drop index if exists IDX_SIMI_POSTGRES_TABLE_UNQ ;
drop index if exists IDX_SIMIDATA_POSTGRES_TABLE_ON_DATA_THEME ;

alter table SIMIDATA_POSTGRES_TABLE rename column data_theme_id to data_theme_id__u59087 ;
alter table SIMIDATA_POSTGRES_TABLE alter column data_theme_id__u59087 drop not null ;
-- alter table SIMIDATA_POSTGRES_TABLE add column DB_SCHEMA_ID uuid ^
-- update SIMIDATA_POSTGRES_TABLE set DB_SCHEMA_ID = <default_value> ;
-- alter table SIMIDATA_POSTGRES_TABLE alter column DB_SCHEMA_ID set not null ;
alter table SIMIDATA_POSTGRES_TABLE add column DB_SCHEMA_ID uuid ; --jeol mod
