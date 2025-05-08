alter table SIMIEXTENDED_DEPENDENCY rename column rep_primary_key to rep_primary_key__u01681 ;
alter table SIMIEXTENDED_DEPENDENCY add column REP_UNIQUE_KEY varchar(255) ;

update
    SIMIEXTENDED_DEPENDENCY
set
    REP_UNIQUE_KEY = 'migration dummy - please replace'
where
    DTYPE = 'simiExtended_Report'
;