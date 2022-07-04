create table SIMITHEME_ORG_UNIT (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    DTYPE varchar(31),
    --
    NAME varchar(100) not null,
    --
    primary key (ID)
);