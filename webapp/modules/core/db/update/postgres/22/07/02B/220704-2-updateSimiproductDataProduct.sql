alter table SIMIPRODUCT_DATA_PRODUCT rename column synonyms to _synonyms_deprecated ; --je
alter table SIMIPRODUCT_DATA_PRODUCT rename column keywords to _keywords_deprecated ; --je

--je
--alter table SIMIPRODUCT_DATA_PRODUCT add column _SYNONYMS_DEPRECATED varchar(800) ;
--alter table SIMIPRODUCT_DATA_PRODUCT add column _KEYWORDS_DEPRECATED varchar(500) ;
