alter table SIMITHEME_SUB_ORG add constraint FK_SIMITHEME_SUB_ORG_ON_AGENCY foreign key (AGENCY_ID) references SIMITHEME_AGENCY(ID) on delete CASCADE;
alter table SIMITHEME_SUB_ORG add constraint FK_SIMITHEME_SUB_ORG_ON_ID foreign key (ID) references SIMITHEME_ORG_UNIT(ID) on delete CASCADE;
create index IDX_SIMITHEME_SUB_ORG_ON_AGENCY on SIMITHEME_SUB_ORG (AGENCY_ID);
