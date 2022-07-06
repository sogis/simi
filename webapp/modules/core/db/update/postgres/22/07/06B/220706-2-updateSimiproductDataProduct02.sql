alter table SIMIPRODUCT_DATA_PRODUCT rename column identifier to identifier__u79287 ;
alter table SIMIPRODUCT_DATA_PRODUCT alter column identifier__u79287 drop not null ;

alter table SIMIPRODUCT_DATA_PRODUCT add column IDENT_IS_PARTIAL boolean ^
update SIMIPRODUCT_DATA_PRODUCT set IDENT_IS_PARTIAL = false where IDENT_IS_PARTIAL is null ;
alter table SIMIPRODUCT_DATA_PRODUCT alter column IDENT_IS_PARTIAL set not null ;

alter table SIMIPRODUCT_DATA_PRODUCT add column IDENT_PART varchar(100) ;
alter table SIMIPRODUCT_DATA_PRODUCT add column DERIVED_IDENTIFIER varchar(100) ^

--je: Die beiden neuen ident-felder befüllen
update SIMIPRODUCT_DATA_PRODUCT set IDENT_PART = identifier__u79287,  DERIVED_IDENTIFIER = identifier__u79287;

alter table SIMIPRODUCT_DATA_PRODUCT alter column DERIVED_IDENTIFIER set not null ;
