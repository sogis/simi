create table SIMITHEME_FILE_TYPE (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    MIME_TYPE varchar(255) not null,
    NAME varchar(100) not null,
    KUERZEL varchar(50) not null,
    --
    primary key (ID)
);