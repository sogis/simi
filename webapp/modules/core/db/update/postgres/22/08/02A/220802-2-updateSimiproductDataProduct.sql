alter table SIMIPRODUCT_DATA_PRODUCT add column THEME_ONLY_FOR_ORG boolean ^
--je
update SIMIPRODUCT_DATA_PRODUCT set THEME_ONLY_FOR_ORG = true where THEME_ONLY_FOR_ORG is null ;
alter table SIMIPRODUCT_DATA_PRODUCT alter column THEME_ONLY_FOR_ORG set not null ;
