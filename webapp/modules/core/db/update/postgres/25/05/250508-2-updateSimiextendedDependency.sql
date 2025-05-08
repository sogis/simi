alter table SIMIEXTENDED_DEPENDENCY add column REP_TABLE varchar(255) ;
alter table SIMIEXTENDED_DEPENDENCY add column REP_DATASOURCE varchar(255) ;
alter table SIMIEXTENDED_DEPENDENCY add column REP_PARAMETER_NAME varchar(255) ;
alter table SIMIEXTENDED_DEPENDENCY add column REP_PRIMARY_KEY varchar(255) ;

update
    SIMIEXTENDED_DEPENDENCY
set
    REP_TABLE = 'migration dummy - please replace',
    REP_DATASOURCE = 'migration dummy - please replace',
    REP_PARAMETER_NAME = 'migration dummy - please replace',
    REP_PRIMARY_KEY = 'migration dummy - please replace'
where
    DTYPE = 'simiExtended_Report'
;
