alter table SIMIDATA_POSTGRES_TABLE add constraint FK_SIMIDATA_POSTGRES_TABLE_ON_DB_SCHEMA foreign key (DB_SCHEMA_ID) references SIMIDATA_DB_SCHEMA(ID) ^
create index IDX_SIMIDATA_POSTGRES_TABLE_ON_DB_SCHEMA on SIMIDATA_POSTGRES_TABLE (DB_SCHEMA_ID) ;
