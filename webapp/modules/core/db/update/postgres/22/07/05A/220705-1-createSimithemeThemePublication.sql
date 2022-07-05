create table SIMITHEME_THEME_PUBLICATION (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    THEME_ID uuid not null,
    DATA_CLASS varchar(50) not null,
    CLASS_SUFFIX_OVERRIDE varchar(50),
    SIMI_CLASS_SUFFIX varchar(255),
    --
    primary key (ID)
);