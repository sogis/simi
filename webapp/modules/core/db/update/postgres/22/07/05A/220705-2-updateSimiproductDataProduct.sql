-- alter table SIMIPRODUCT_DATA_PRODUCT add column THEME_PUBLICATION_ID uuid ^
-- update SIMIPRODUCT_DATA_PRODUCT set THEME_PUBLICATION_ID = <default_value> ;
-- alter table SIMIPRODUCT_DATA_PRODUCT alter column THEME_PUBLICATION_ID set not null ;
alter table SIMIPRODUCT_DATA_PRODUCT add column THEME_PUBLICATION_ID uuid not null ;
