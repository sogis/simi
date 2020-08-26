-- begin SIMI_ROLE
create table SIMI_ROLE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(100) not null,
    REMARKS text,
    --
    primary key (ID)
)^
-- end SIMI_ROLE
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
-- begin SIMI_DATA_PRODUCT
create table SIMI_DATA_PRODUCT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    DTYPE varchar(31),
    --
    IDENTIFIER varchar(100) not null,
    PUB_SCOPE_ID uuid not null,
    KEYWORDS varchar(200),
    REMARKS text,
    SYNONYMS varchar(200),
    TITLE varchar(200),
    RELEASED_AT timestamp,
    RELEASED_THROUGH varchar(100),
    --
    primary key (ID)
)^
-- end SIMI_DATA_PRODUCT
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
-- begin SIMI_IDENTITY
create table SIMI_IDENTITY (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    DTYPE varchar(31),
    --
    IDENTIFIER varchar(100) not null,
    REMARKS text,
    --
    primary key (ID)
)^
-- end SIMI_IDENTITY
-- begin SIMI_DATA_PRODUCT_PUB_SCOPE
create table SIMI_DATA_PRODUCT_PUB_SCOPE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DISPLAY_TEXT varchar(100) not null,
    OVERALL_STATE integer not null,
    DEFAULT_VALUE boolean not null,
    FOR_DSV boolean not null,
    FOR_GROUP boolean not null,
    FOR_MAP boolean not null,
    PUB_TO_WMS boolean not null,
    PUB_TO_WGC boolean not null,
    PUB_TO_LOCATOR boolean not null,
    SORT integer not null,
    --
    primary key (ID)
)^
-- end SIMI_DATA_PRODUCT_PUB_SCOPE
-- begin SIMI_PROPERTIES_IN_LIST
create table SIMI_PROPERTIES_IN_LIST (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    SORT integer not null,
    TRANSPARENCY integer not null,
    --
    VISIBLE boolean not null,
    PRODUCT_LIST_ID uuid,
    SINGLE_ACTOR_ID uuid,
    --
    primary key (ID)
)^
-- end SIMI_PROPERTIES_IN_LIST
-- begin SIMI_SINGLE_ACTOR
create table SIMI_SINGLE_ACTOR (
    ID uuid,
    --
    primary key (ID)
)^
-- end SIMI_SINGLE_ACTOR
-- begin SIMI_PRODUCT_LIST
create table SIMI_PRODUCT_LIST (
    ID uuid,
    --
    primary key (ID)
)^
-- end SIMI_PRODUCT_LIST
-- begin SIMI_USER
create table SIMI_USER (
    ID uuid,
    --
    FIRSTNAME varchar(100) not null,
    LASTNAME varchar(100) not null,
    --
    primary key (ID)
)^
-- end SIMI_USER
-- begin SIMI_GROUP
create table SIMI_GROUP (
    ID uuid,
    --
    PUB_AS_USER boolean not null,
    --
    primary key (ID)
)^
-- end SIMI_GROUP
-- begin SIMI_LAYER_LIST
create table SIMI_LAYER_LIST (
    ID uuid,
    --
    primary key (ID)
)^
-- end SIMI_LAYER_LIST
-- begin SIMI_MAP
create table SIMI_MAP (
    ID uuid,
    --
    BACKGROUND boolean not null,
    WGC_URL_VALUE varchar(50) not null,
    --
    primary key (ID)
)^
-- end SIMI_MAP
-- begin SIMI_GROUP_USER_LINK
create table SIMI_GROUP_USER_LINK (
    GROUP_ID uuid,
    USER_ID uuid,
    primary key (GROUP_ID, USER_ID)
)^
-- end SIMI_GROUP_USER_LINK
-- begin SIMI_ROLE_GROUP_LINK
create table SIMI_ROLE_GROUP_LINK (
    GROUP_ID uuid,
    ROLE_ID uuid,
    primary key (GROUP_ID, ROLE_ID)
)^
-- end SIMI_ROLE_GROUP_LINK
-- begin SIMI_ROLE_USER_LINK
create table SIMI_ROLE_USER_LINK (
    USER_ID uuid,
    ROLE_ID uuid,
    primary key (USER_ID, ROLE_ID)
)^
-- end SIMI_ROLE_USER_LINK
