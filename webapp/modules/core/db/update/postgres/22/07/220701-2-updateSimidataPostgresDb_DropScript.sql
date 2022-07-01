--jo
update SIMIDATA_POSTGRES_DB set identifier = DB_NAME__U44525, title = DB_NAME__U44525 ^
--end jo

alter table SIMIDATA_POSTGRES_DB drop column DB_NAME__U44525 cascade ;
