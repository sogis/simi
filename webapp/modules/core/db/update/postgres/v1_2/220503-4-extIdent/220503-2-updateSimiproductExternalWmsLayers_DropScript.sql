begin;

--move data to renamed column
update SIMIPRODUCT_EXTERNAL_WMS_LAYERS
set ext_identifier = IDENTIFIER_LIST__U14970;

--drop old column
alter table SIMIPRODUCT_EXTERNAL_WMS_LAYERS drop column IDENTIFIER_LIST__U14970 cascade ;

commit;
