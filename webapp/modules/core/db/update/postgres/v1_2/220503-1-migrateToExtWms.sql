
-- migrate from wms and wmts to wms service with featureinfo

begin;

-- create the new tables

create table SIMIPRODUCT_EXTERNAL_WMS_SERVICE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    URL varchar(255) not null,
    FEATURE_INFO_FORMAT varchar(50) not null,
    REMARKS text,
    --
    primary key (ID)
);

create table SIMIPRODUCT_EXTERNAL_WMS_LAYERS (
    ID uuid,
    --
    IDENTIFIER_LIST varchar(255) not null,
    SERVICE_ID uuid not null,
    --
    primary key (ID)
);

-- FKs and indexes

alter table SIMIPRODUCT_EXTERNAL_WMS_LAYERS add constraint FK_SIMIPRODUCT_EXTERNAL_WMS_LAYERS_ON_SERVICE foreign key (SERVICE_ID) references SIMIPRODUCT_EXTERNAL_WMS_SERVICE(ID);
alter table SIMIPRODUCT_EXTERNAL_WMS_LAYERS add constraint FK_SIMIPRODUCT_EXTERNAL_WMS_LAYERS_ON_ID foreign key (ID) references SIMIPRODUCT_DATA_PRODUCT(ID) on delete CASCADE;
create index IDX_SIMIPRODUCT_EXTERNAL_WMS_LAYERS_ON_SERVICE on SIMIPRODUCT_EXTERNAL_WMS_LAYERS (SERVICE_ID);

create unique index IDX_SIMIPRODUCT_EXTERNAL_WMS_SERVICE_UK_URL on SIMIPRODUCT_EXTERNAL_WMS_SERVICE (URL) where DELETE_TS is null ;

-- move data
INSERT INTO
  simi.simiproduct_external_wms_service(
    id,
    "version",
    create_ts,
    created_by,
    update_ts,
    updated_by,
    delete_ts,
    deleted_by,
    feature_info_format,
    url,
    remarks
  )
SELECT
    id,
    "version",
    create_ts,
    created_by,
    update_ts,
    updated_by,
    delete_ts,
    deleted_by,
    'application/vnd.ogc.gml' AS feature_info_format,
    url,
    remarks
FROM
  simi.simiproduct_external_map_service
;

INSERT INTO simi.simiproduct_external_wms_layers(id, identifier_list, service_id)
SELECT id, identifier_list, service_id
FROM simi.simiproduct_external_map_layers
;

-- drop tables
drop table simi.simiproduct_external_map_layers cascade;

drop table simi.simiproduct_external_map_service cascade;

commit;

