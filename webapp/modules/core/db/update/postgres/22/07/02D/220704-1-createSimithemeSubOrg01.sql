create table SIMITHEME_SUB_ORG (
    ID uuid,
    --
    URL varchar(255),
    PHONE varchar(20),
    EMAIL varchar(50),
    AGENCY_ID uuid not null,
    --
    primary key (ID)
);