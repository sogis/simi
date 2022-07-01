alter table SIMIDATA_VIEW_FIELD add column WGC_EXPOSED boolean ^
update SIMIDATA_VIEW_FIELD set WGC_EXPOSED = false where WGC_EXPOSED is null ;
alter table SIMIDATA_VIEW_FIELD alter column WGC_EXPOSED set not null ;
