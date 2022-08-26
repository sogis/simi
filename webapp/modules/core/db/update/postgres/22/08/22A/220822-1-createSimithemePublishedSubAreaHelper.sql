create table SIMITHEME_PUBLISHED_SUB_AREA_HELPER (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    PUBLISHED timestamp not null,
    THEME_PUBLICATION_ID uuid not null,
    PREV_PUBLISHED timestamp not null,
    SUB_AREA_IDENT varchar(100) not null,
    THEME_PUB_DATA_CLASS_OVERRIDE varchar(50),
    THEME_IDENTIFIER varchar(100) not null,
    THEME_PUB_DATA_CLASS varchar(50) not null,
    SUB_AREA_ID uuid not null,
    --
    primary key (ID)
);