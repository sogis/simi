alter table SIMITHEME_THEME rename column coverage_ident to coverage_ident__u42590 ;
alter table SIMITHEME_THEME alter column coverage_ident__u42590 drop not null ;
