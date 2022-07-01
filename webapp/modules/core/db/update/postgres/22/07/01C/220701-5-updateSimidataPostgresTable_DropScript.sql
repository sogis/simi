--je
update SIMIDATA_POSTGRES_TABLE set DB_SCHEMA_ID = DATA_THEME_ID__U59087 ^

alter table SIMIDATA_POSTGRES_TABLE alter column DB_SCHEMA_ID set not null ^
--end je

alter table SIMIDATA_POSTGRES_TABLE drop column DATA_THEME_ID__U59087 cascade ;

