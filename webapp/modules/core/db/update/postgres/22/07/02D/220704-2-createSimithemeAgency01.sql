alter table SIMITHEME_AGENCY add constraint FK_SIMITHEME_AGENCY_ON_ID foreign key (ID) references SIMITHEME_ORG_UNIT(ID) on delete CASCADE;
create unique index IDX_SIMITHEME_AGENCY_UK_ABBREVIATION on SIMITHEME_AGENCY (ABBREVIATION);
