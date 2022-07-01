alter table SIMIDATA_DB_SCHEMA add constraint FK_SIMIDATA_DB_SCHEMA_ON_POSTGRES_DB foreign key (POSTGRES_DB_ID) references SIMIDATA_POSTGRES_DB(ID);
create unique index IDX_SIMIDATA_DB_SCHEMA_UNQ on SIMIDATA_DB_SCHEMA (SCHEMA_NAME, POSTGRES_DB_ID);
create index IDX_SIMIDATA_DB_SCHEMA_ON_POSTGRES_DB on SIMIDATA_DB_SCHEMA (POSTGRES_DB_ID);
