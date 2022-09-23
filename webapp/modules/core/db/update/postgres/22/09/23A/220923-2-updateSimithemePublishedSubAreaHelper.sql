alter table SIMITHEME_PUBLISHED_SUB_AREA_HELPER rename column theme_pub_data_class to theme_pub_data_class__u46340 ;
alter table SIMITHEME_PUBLISHED_SUB_AREA_HELPER alter column theme_pub_data_class__u46340 drop not null ;
alter table SIMITHEME_PUBLISHED_SUB_AREA_HELPER rename column theme_pub_data_class_override to theme_pub_data_class_override__u03562 ;
alter table SIMITHEME_PUBLISHED_SUB_AREA_HELPER rename column sub_area_ident to sub_area_ident__u67068 ;
alter table SIMITHEME_PUBLISHED_SUB_AREA_HELPER alter column sub_area_ident__u67068 drop not null ;
alter table SIMITHEME_PUBLISHED_SUB_AREA_HELPER add column TPUB_CLASS_SUFFIX_OVERRIDE varchar(50) ;
alter table SIMITHEME_PUBLISHED_SUB_AREA_HELPER add column SUBAREA_COVERAGE_IDENT varchar(100) ^
update SIMITHEME_PUBLISHED_SUB_AREA_HELPER set SUBAREA_COVERAGE_IDENT = '' where SUBAREA_COVERAGE_IDENT is null ;
alter table SIMITHEME_PUBLISHED_SUB_AREA_HELPER alter column SUBAREA_COVERAGE_IDENT set not null ;
alter table SIMITHEME_PUBLISHED_SUB_AREA_HELPER add column TPUB_DATA_CLASS varchar(50) ^
update SIMITHEME_PUBLISHED_SUB_AREA_HELPER set TPUB_DATA_CLASS = 'tableSimple' where TPUB_DATA_CLASS is null ;
alter table SIMITHEME_PUBLISHED_SUB_AREA_HELPER alter column TPUB_DATA_CLASS set not null ;
alter table SIMITHEME_PUBLISHED_SUB_AREA_HELPER add column SUBAREA_IDENTIFIER varchar(100) ^
update SIMITHEME_PUBLISHED_SUB_AREA_HELPER set SUBAREA_IDENTIFIER = '' where SUBAREA_IDENTIFIER is null ;
alter table SIMITHEME_PUBLISHED_SUB_AREA_HELPER alter column SUBAREA_IDENTIFIER set not null ;
