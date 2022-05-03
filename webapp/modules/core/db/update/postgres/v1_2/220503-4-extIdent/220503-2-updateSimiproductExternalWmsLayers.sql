alter table SIMIPRODUCT_EXTERNAL_WMS_LAYERS rename column identifier_list to identifier_list__u14970 ;
alter table SIMIPRODUCT_EXTERNAL_WMS_LAYERS alter column identifier_list__u14970 drop not null ;
alter table SIMIPRODUCT_EXTERNAL_WMS_LAYERS add column EXT_IDENTIFIER varchar(255) ^
update SIMIPRODUCT_EXTERNAL_WMS_LAYERS set EXT_IDENTIFIER = '' where EXT_IDENTIFIER is null ;
alter table SIMIPRODUCT_EXTERNAL_WMS_LAYERS alter column EXT_IDENTIFIER set not null ;
