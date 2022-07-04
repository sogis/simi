create table SIMIPRODUCT_THERM_GROUP (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    NAME varchar(255) not null,
    KEYWORDS varchar(800),
    SYNONYMS varchar(800),
    --
    primary key (ID)
);