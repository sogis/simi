alter table SIMIPRODUCT_EXTERNAL_WMS_LAYERS add column FEATURE_INFO_FORMAT varchar(50) ^
update SIMIPRODUCT_EXTERNAL_WMS_LAYERS set FEATURE_INFO_FORMAT = 'application/vnd.ogc.gml' where FEATURE_INFO_FORMAT is null ;
alter table SIMIPRODUCT_EXTERNAL_WMS_LAYERS alter column FEATURE_INFO_FORMAT set not null ;
