alter table SIMITHEME_SUB_AREA rename column tmp_coverage_ident to tmp_coverage_ident__u81900 ;
alter table SIMITHEME_SUB_AREA add column COVERAGE_IDENT varchar(100) ^
--je
update SIMITHEME_SUB_AREA set COVERAGE_IDENT = 'ktso' where COVERAGE_IDENT is null ;
alter table SIMITHEME_SUB_AREA alter column COVERAGE_IDENT set not null ;
