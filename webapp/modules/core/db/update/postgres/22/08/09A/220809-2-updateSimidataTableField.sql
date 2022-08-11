alter table SIMIDATA_TABLE_FIELD add column ILI_ENUM boolean ^
update SIMIDATA_TABLE_FIELD set ILI_ENUM = false where ILI_ENUM is null ;
alter table SIMIDATA_TABLE_FIELD alter column ILI_ENUM set not null ;
