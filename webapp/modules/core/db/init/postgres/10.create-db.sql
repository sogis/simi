-- begin SIMIIAM_ROLE
create table SIMIIAM_ROLE (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    NAME varchar(100) not null,
    REMARKS text,
    --
    primary key (ID)
)^
-- end SIMIIAM_ROLE
-- begin SIMIIAM_PERMISSION
create table SIMIIAM_PERMISSION (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    LEVEL_ varchar(50) not null,
    DATA_SET_VIEW_ID uuid not null,
    ROLE_ID uuid not null,
    --
    primary key (ID)
)^
-- end SIMIIAM_PERMISSION
-- begin SIMIEXTENDED_RELATION
create table SIMIEXTENDED_RELATION (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    RELATION_TYPE varchar(50) not null,
    DEPENDENCY_ID uuid not null,
    DATA_SET_VIEW_ID uuid not null,
    --
    primary key (ID)
)^
-- end SIMIEXTENDED_RELATION
-- begin SIMIPRODUCT_DATA_PRODUCT_PUB_SCOPE
create table SIMIPRODUCT_DATA_PRODUCT_PUB_SCOPE (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
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
-- end SIMIPRODUCT_DATA_PRODUCT_PUB_SCOPE
-- begin SIMIDATA_VIEW_FIELD
create table SIMIDATA_VIEW_FIELD (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    SORT integer not null,
    WGC_EXPOSED boolean not null,
    TABLE_FIELD_ID uuid not null,
    TABLE_VIEW_ID uuid not null,
    --
    primary key (ID)
)^
-- end SIMIDATA_VIEW_FIELD
-- begin SIMIDATA_STYLEASSET
create table SIMIDATA_STYLEASSET (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    DATASET_SET_VIEW_ID uuid not null,
    IS_FOR_SERVER boolean not null,
    FILE_NAME varchar(255) not null,
    FILE_CONTENT bytea not null,
    --
    primary key (ID)
)^
-- end SIMIDATA_STYLEASSET
-- begin SIMIDATA_POSTGRES_TABLE
create table SIMIDATA_POSTGRES_TABLE (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    ID_FIELD_NAME varchar(100) not null,
    TABLE_IS_VIEW boolean not null,
    TITLE varchar(255) not null,
    DESCRIPTION_OVERRIDE text,
    DB_SCHEMA_ID uuid not null,
    DESCRIPTION_MODEL text,
    CAT_SYNC_STAMP timestamp not null,
    GEO_FIELD_NAME varchar(100),
    GEO_TYPE varchar(100),
    GEO_EPSG_CODE integer,
    TABLE_NAME varchar(100) not null,
    REMARKS text,
    --
    primary key (ID)
)^
-- end SIMIDATA_POSTGRES_TABLE
-- begin SIMIDATA_POSTGRES_DB
create table SIMIDATA_POSTGRES_DB (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    IDENTIFIER varchar(100) not null,
    TITLE varchar(100) not null,
    DB_SERVICE_URL varchar(255) not null,
    DEFAULT_VALUE boolean not null,
    --
    primary key (ID)
)^
-- end SIMIDATA_POSTGRES_DB
-- begin SIMIDATA_TABLE_FIELD
create table SIMIDATA_TABLE_FIELD (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    NAME varchar(100) not null,
    DESCRIPTION_MODEL text,
    DESCRIPTION_OVERRIDE text,
    TYPE_NAME varchar(100) not null,
    MANDATORY boolean not null,
    STR_LENGTH integer,
    CAT_SYNCED boolean not null,
    ILI_ENUM boolean not null,
    POSTGRES_TABLE_ID uuid not null,
    ALIAS varchar(100),
    WMS_FI_FORMAT varchar(100),
    DISPLAY_PROPS4_JSON text,
    --
    primary key (ID)
)^
-- end SIMIDATA_TABLE_FIELD
-- begin SIMIIAM_IDENTITY
create table SIMIIAM_IDENTITY (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    DTYPE varchar(31),
    --
    IDENTIFIER varchar(100) not null,
    REMARKS text,
    --
    primary key (ID)
)^
-- end SIMIIAM_IDENTITY
-- begin SIMIDATA_RASTER_DS
create table SIMIDATA_RASTER_DS (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    PATH varchar(200) not null,
    REMARKS text,
    DESCRIPTION text,
    --
    primary key (ID)
)^
-- end SIMIDATA_RASTER_DS
-- begin SIMIPRODUCT_DATA_PRODUCT
create table SIMIPRODUCT_DATA_PRODUCT (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    DTYPE varchar(31),
    --
    TITLE varchar(200),
    DERIVED_IDENTIFIER varchar(100) not null,
    IDENT_PART varchar(100),
    KEYWORDS varchar(800),
    SYNONYMS varchar(800),
    THEME_ONLY_FOR_ORG boolean not null,
    IDENT_IS_PARTIAL boolean not null,
    THEME_PUBLICATION_ID uuid not null,
    DESCRIPTION text,
    PUB_SCOPE_ID uuid not null,
    REMARKS text,
    --
    primary key (ID)
)^
-- end SIMIPRODUCT_DATA_PRODUCT
-- begin SIMIEXTENDED_DEPENDENCY
create table SIMIEXTENDED_DEPENDENCY (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    DTYPE varchar(31),
    --
    NAME varchar(100) not null,
    REMARKS text,
    --
    -- from simiExtended_CCCIntegration
    MAP_ID uuid,
    LOCATOR_LAYERS text,
    NOTIFY_LAYERS text,
    --
    -- from simiExtended_FeatureInfo
    DISPLAY_TEMPLATE text,
    SQL_QUERY text,
    PY_MODULE_NAME varchar(100),
    --
    primary key (ID)
)^
-- end SIMIEXTENDED_DEPENDENCY
-- begin SIMIPRODUCT_PROPERTIES_IN_LIST
create table SIMIPRODUCT_PROPERTIES_IN_LIST (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    SORT integer not null,
    --
    VISIBLE boolean not null,
    PRODUCT_LIST_ID uuid not null,
    SINGLE_ACTOR_ID uuid not null,
    --
    primary key (ID)
)^
-- end SIMIPRODUCT_PROPERTIES_IN_LIST
-- begin SIMIPRODUCT_PROPERTIES_IN_FACADE
create table SIMIPRODUCT_PROPERTIES_IN_FACADE (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    SORT integer not null,
    --
    DATA_SET_VIEW_ID uuid not null,
    FACADE_LAYER_ID uuid not null,
    --
    primary key (ID)
)^
-- end SIMIPRODUCT_PROPERTIES_IN_FACADE
-- begin SIMIIAM_USER
create table SIMIIAM_USER (
    ID uuid,
    --
    primary key (ID)
)^
-- end SIMIIAM_USER
-- begin SIMIIAM_GROUP
create table SIMIIAM_GROUP (
    ID uuid,
    --
    PUB_AS_USER boolean not null,
    --
    primary key (ID)
)^
-- end SIMIIAM_GROUP
-- begin SIMIPRODUCT_SINGLE_ACTOR
create table SIMIPRODUCT_SINGLE_ACTOR (
    ID uuid,
    --
    TRANSPARENCY integer not null,
    CUSTOM_LEGEND bytea,
    CUSTOM_LEGEND_SUFFIX varchar(100),
    --
    primary key (ID)
)^
-- end SIMIPRODUCT_SINGLE_ACTOR
-- begin SIMIPRODUCT_PRODUCT_LIST
create table SIMIPRODUCT_PRODUCT_LIST (
    ID uuid,
    --
    primary key (ID)
)^
-- end SIMIPRODUCT_PRODUCT_LIST
-- begin SIMIPRODUCT_FACADE_LAYER
create table SIMIPRODUCT_FACADE_LAYER (
    ID uuid,
    --
    primary key (ID)
)^
-- end SIMIPRODUCT_FACADE_LAYER
-- begin SIMIDATA_DATA_SET_VIEW
create table SIMIDATA_DATA_SET_VIEW (
    ID uuid,
    --
    SERVICE_DOWNLOAD boolean not null,
    IS_FILE_DOWNLOAD_DSV boolean not null,
    STYLE_SERVER text,
    STYLE_SERVER_UPLOADED timestamp,
    STYLE_DESKTOP text,
    STYLE_DESKTOP_UPLOADED timestamp,
    --
    primary key (ID)
)^
-- end SIMIDATA_DATA_SET_VIEW
-- begin SIMIPRODUCT_LAYER_GROUP
create table SIMIPRODUCT_LAYER_GROUP (
    ID uuid,
    --
    primary key (ID)
)^
-- end SIMIPRODUCT_LAYER_GROUP
-- begin SIMIPRODUCT_MAP
create table SIMIPRODUCT_MAP (
    ID uuid,
    --
    BACKGROUND boolean not null,
    --
    primary key (ID)
)^
-- end SIMIPRODUCT_MAP
-- begin SIMIDATA_RASTER_VIEW
create table SIMIDATA_RASTER_VIEW (
    ID uuid,
    --
    RASTER_DS_ID uuid not null,
    --
    primary key (ID)
)^
-- end SIMIDATA_RASTER_VIEW
-- begin SIMIDATA_TABLE_VIEW
create table SIMIDATA_TABLE_VIEW (
    ID uuid,
    --
    POSTGRES_TABLE_ID uuid not null,
    ROW_FILTER_VIEW_NAME varchar(100),
    SEARCH_TYPE varchar(50) not null,
    SEARCH_FACET varchar(100),
    SEARCH_FILTER_WORD varchar(100),
    --
    primary key (ID)
)^
-- end SIMIDATA_TABLE_VIEW
-- begin SIMIIAM_GROUP_USER_LINK
create table SIMIIAM_GROUP_USER_LINK (
    GROUP_ID uuid,
    USER_ID uuid,
    primary key (GROUP_ID, USER_ID)
)^
-- end SIMIIAM_GROUP_USER_LINK
-- begin SIMIIAM_ROLE_GROUP_LINK
create table SIMIIAM_ROLE_GROUP_LINK (
    GROUP_ID uuid,
    ROLE_ID uuid,
    primary key (GROUP_ID, ROLE_ID)
)^
-- end SIMIIAM_ROLE_GROUP_LINK
-- begin SIMIIAM_ROLE_USER_LINK
create table SIMIIAM_ROLE_USER_LINK (
    USER_ID uuid,
    ROLE_ID uuid,
    primary key (USER_ID, ROLE_ID)
)^
-- end SIMIIAM_ROLE_USER_LINK
-- begin SIMIPRODUCT_EXTERNAL_WMS_LAYERS
create table SIMIPRODUCT_EXTERNAL_WMS_LAYERS (
    ID uuid,
    --
    EXT_IDENTIFIER varchar(255) not null,
    FEATURE_INFO_FORMAT varchar(50) not null,
    SERVICE_ID uuid not null,
    --
    primary key (ID)
)^
-- end SIMIPRODUCT_EXTERNAL_WMS_LAYERS
-- begin SIMIPRODUCT_EXTERNAL_WMS_SERVICE
create table SIMIPRODUCT_EXTERNAL_WMS_SERVICE (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    URL varchar(255) not null,
    REMARKS text,
    --
    primary key (ID)
)^
-- end SIMIPRODUCT_EXTERNAL_WMS_SERVICE
-- begin SIMIDATA_DB_SCHEMA
create table SIMIDATA_DB_SCHEMA (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    SCHEMA_NAME varchar(100) not null,
    POSTGRES_DB_ID uuid not null,
    --
    primary key (ID)
)^
-- end SIMIDATA_DB_SCHEMA
-- begin SIMITHEME_ORG_UNIT
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
)^
-- end SIMITHEME_ORG_UNIT
-- begin SIMITHEME_AGENCY
create table SIMITHEME_AGENCY (
    ID uuid,
    --
    ABBREVIATION varchar(10) not null,
    URL varchar(255) not null,
    PHONE varchar(20) not null,
    EMAIL varchar(50) not null,
    --
    primary key (ID)
)^
-- end SIMITHEME_AGENCY
-- begin SIMITHEME_SUB_ORG
create table SIMITHEME_SUB_ORG (
    ID uuid,
    --
    URL varchar(255),
    PHONE varchar(20),
    EMAIL varchar(50),
    AGENCY_ID uuid not null,
    --
    primary key (ID)
)^
-- end SIMITHEME_SUB_ORG
-- begin SIMITHEME_FILE_TYPE
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
)^
-- end SIMITHEME_FILE_TYPE
-- begin SIMITHEME_THEME_PUBLICATION
create table SIMITHEME_THEME_PUBLICATION (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    THEME_ID uuid not null,
    MODEL_UPDATE_TS timestamp,
    MODEL_UPDATED_BY varchar(50),
    PUBLIC_MODEL_NAME varchar(150),
    DESCRIPTION_OVERRIDE varchar(1000),
    COVERAGE_IDENT varchar(100) not null,
    DATA_CLASS varchar(50) not null,
    CLASS_SUFFIX_OVERRIDE varchar(50),
    TITLE_OVERRIDE varchar(200),
    REMARKS text,
    --
    primary key (ID)
)^
-- end SIMITHEME_THEME_PUBLICATION
-- begin SIMITHEME_THEME
create table SIMITHEME_THEME (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    IDENTIFIER varchar(100) not null,
    KEYWORDS_ARR varchar(800),
    SYNONYMS_ARR varchar(800),
    FURTHER_INFO_URL varchar(500),
    DATA_OWNER_ID uuid not null,
    TITLE varchar(200) not null,
    DESCRIPTION varchar(1000) not null,
    REMARKS text,
    --
    primary key (ID)
)^
-- end SIMITHEME_THEME

-- begin SIMITHEME_SUB_AREA
create table SIMITHEME_SUB_AREA (
    ID uuid,
    --
    IDENTIFIER varchar(100) not null,
    GEOM_WKB bytea not null,
    TITLE varchar(255),
    UPDATED timestamp,
    COVERAGE_IDENT varchar(100) not null,
    --
    primary key (ID)
)^
-- end SIMITHEME_SUB_AREA
-- begin SIMITHEME_PUBLISHED_SUB_AREA
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
    SUB_AREA_ID uuid,
    SUB_AREA_IDENT varchar(100),
    THEME_PUBLICATION_ID uuid not null,
    --
    primary key (ID)
)^
-- end SIMITHEME_PUBLISHED_SUB_AREA
-- begin SIMITHEME_THEME_PUBLICATION_CUSTOM_FILE_TYPE_LINK
create table SIMITHEME_THEME_PUBLICATION_CUSTOM_FILE_TYPE_LINK (
    THEME_PUBLICATION_ID uuid,
    CUSTOM_FILE_TYPE_ID uuid,
    primary key (THEME_PUBLICATION_ID, CUSTOM_FILE_TYPE_ID)
)^
-- end SIMITHEME_THEME_PUBLICATION_CUSTOM_FILE_TYPE_LINK
-- begin SIMITHEME_PUBLISHED_SUB_AREA_HELPER
create table SIMITHEME_PUBLISHED_SUB_AREA_HELPER (
    ID uuid,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer not null,
    --
    PUBLISHED timestamp not null,
    PREV_PUBLISHED timestamp not null,
    TPUB_DATA_CLASS varchar(50) not null,
    TPUB_CLASS_SUFFIX_OVERRIDE varchar(50),
    SUBAREA_IDENTIFIER varchar(100) not null,
    SUBAREA_COVERAGE_IDENT varchar(100) not null,
    THEME_IDENTIFIER varchar(100) not null,
    --
    primary key (ID)
)^
-- end SIMITHEME_PUBLISHED_SUB_AREA_HELPER
