alter table SIMITHEME_PUBLISHED_SUB_AREA add constraint FK_SIMITHEME_PUBLISHED_SUB_AREA_ON_SUB_AREA foreign key (SUB_AREA_ID) references SIMITHEME_SUB_AREA(ID) on delete CASCADE;
alter table SIMITHEME_PUBLISHED_SUB_AREA add constraint FK_SIMITHEME_PUBLISHED_SUB_AREA_ON_THEME_PUBLICATION foreign key (THEME_PUBLICATION_ID) references SIMITHEME_THEME_PUBLICATION(ID) on delete CASCADE;
create unique index IDX_SIMI_PUBLISHED_SUB_AREA_UNQ on SIMITHEME_PUBLISHED_SUB_AREA (SUB_AREA_ID, THEME_PUBLICATION_ID);
create index IDX_SIMITHEME_PUBLISHED_SUB_AREA_ON_SUB_AREA on SIMITHEME_PUBLISHED_SUB_AREA (SUB_AREA_ID);
create index IDX_SIMITHEME_PUBLISHED_SUB_AREA_ON_THEME_PUBLICATION on SIMITHEME_PUBLISHED_SUB_AREA (THEME_PUBLICATION_ID);
