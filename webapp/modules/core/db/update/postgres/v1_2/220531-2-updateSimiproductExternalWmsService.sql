alter table SIMIPRODUCT_EXTERNAL_WMS_SERVICE rename column feature_info_format to feature_info_format__u06031 ;
alter table SIMIPRODUCT_EXTERNAL_WMS_SERVICE alter column feature_info_format__u06031 drop not null ;