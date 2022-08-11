alter table SIMIPRODUCT_DATA_PRODUCT rename column _synonyms_deprecated to _synonyms_deprecated__u48593 ;
alter table SIMIPRODUCT_DATA_PRODUCT rename column _keywords_deprecated to _keywords_deprecated__u80971 ;
alter table SIMIPRODUCT_DATA_PRODUCT add column KEYWORDS varchar(800) ;
alter table SIMIPRODUCT_DATA_PRODUCT add column SYNONYMS varchar(800) ;

--je
UPDATE SIMIPRODUCT_DATA_PRODUCT set KEYWORDS = _keywords_deprecated__u80971;
UPDATE SIMIPRODUCT_DATA_PRODUCT set SYNONYMS = _synonyms_deprecated__u48593;


