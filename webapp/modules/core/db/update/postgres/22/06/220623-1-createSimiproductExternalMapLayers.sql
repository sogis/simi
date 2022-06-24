create table SIMIPRODUCT_EXTERNAL_MAP_LAYERS (
    ID uuid,
    --
    IDENTIFIER_LIST varchar(255) not null,
    SERVICE_ID uuid not null,
    --
    primary key (ID)
);