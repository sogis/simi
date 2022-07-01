create table SIMIDATA_DB_SCHEMA (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    SCHEMA_NAME varchar(100) not null,
    POSTGRES_DB_ID uuid not null,
    --
    primary key (ID)
);