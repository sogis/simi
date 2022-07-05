alter table SIMITHEME_THEME_PUBLICATION add constraint FK_SIMITHEME_THEME_PUBLICATION_ON_THEME foreign key (THEME_ID) references SIMITHEME_THEME(ID) on delete CASCADE;
create index IDX_SIMITHEME_THEME_PUBLICATION_ON_THEME on SIMITHEME_THEME_PUBLICATION (THEME_ID);