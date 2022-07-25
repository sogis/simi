create table SIMITHEME_SUB_AREA (
    ID uuid,
    --
    IDENTIFIER varchar(100) not null,
    GEOM_WKB bytea not null,
    TITLE varchar(255),
    UPDATED timestamp,
    TMP_COVERAGE_IDENT varchar(100),
    --
    primary key (ID)
);