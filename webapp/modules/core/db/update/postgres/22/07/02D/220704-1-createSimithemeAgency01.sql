create table SIMITHEME_AGENCY (
    ID uuid,
    --
    ABBREVIATION varchar(10) not null,
    URL varchar(255) not null,
    PHONE varchar(20) not null,
    EMAIL varchar(50) not null,
    --
    primary key (ID)
);