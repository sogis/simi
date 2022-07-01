alter table SIMIDATA_DATA_SET_VIEW rename column raw_download to raw_download__u67670 ;
alter table SIMIDATA_DATA_SET_VIEW alter column raw_download__u67670 drop not null ;
alter table SIMIDATA_DATA_SET_VIEW add column SERVICE_DOWNLOAD boolean ^
update SIMIDATA_DATA_SET_VIEW set SERVICE_DOWNLOAD = false where SERVICE_DOWNLOAD is null ;
alter table SIMIDATA_DATA_SET_VIEW alter column SERVICE_DOWNLOAD set not null ;
alter table SIMIDATA_DATA_SET_VIEW add column IS_FILE_DOWNLOAD_DSV boolean ^
update SIMIDATA_DATA_SET_VIEW set IS_FILE_DOWNLOAD_DSV = false where IS_FILE_DOWNLOAD_DSV is null ;
alter table SIMIDATA_DATA_SET_VIEW alter column IS_FILE_DOWNLOAD_DSV set not null ;
