-- begin PRODUCT_DATA_SET_VIEW
create table PRODUCT_DATA_SET_VIEW (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    IDENTIFIER varchar(255) not null,
    FEATURE_INFO_ID uuid,
    --
    primary key (ID)
)^
-- end PRODUCT_DATA_SET_VIEW
-- begin FI_LAYER_RELATION
create table FI_LAYER_RELATION (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    REL_TYPE varchar(50) not null,
    DATA_SET_VIEW_ID uuid not null,
    FEATURE_INFO_ID uuid not null,
    --
    primary key (ID)
)^
-- end FI_LAYER_RELATION
-- begin FI_FEATURE_INFO
create table FI_FEATURE_INFO (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DISPLAY_TEMPLATE text not null,
    SQL_QUERY text,
    PY_MODULE_NAME varchar(100),
    REMARKS text,
    --
    primary key (ID)
)^
-- end FI_FEATURE_INFO
