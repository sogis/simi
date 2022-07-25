alter table SIMITHEME_THEME_PUBLICATION add column COVERAGE_IDENT varchar(100) ^
--je
update SIMITHEME_THEME_PUBLICATION set COVERAGE_IDENT = 'ktso' where COVERAGE_IDENT is null ;
alter table SIMITHEME_THEME_PUBLICATION alter column COVERAGE_IDENT set not null ;
