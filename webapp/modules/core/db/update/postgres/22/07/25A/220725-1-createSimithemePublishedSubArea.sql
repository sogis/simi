create table SIMITHEME_PUBLISHED_SUB_AREA (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    PUBLISHED timestamp not null,
    PREV_PUBLISHED timestamp not null,
    SUB_AREA_ID uuid not null,
    THEME_PUBLICATION_ID uuid not null,
    --
    primary key (ID)
);