create table SIMITHEME_THEME (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    IDENTIFIER varchar(100) not null,
    DATA_OWNER_ID uuid not null,
    TITLE varchar(200) not null,
    DESCRIPTION varchar(1000) not null,
    COVERAGE_IDENT varchar(100) not null,
    REMARKS text,
    --
    primary key (ID)
);