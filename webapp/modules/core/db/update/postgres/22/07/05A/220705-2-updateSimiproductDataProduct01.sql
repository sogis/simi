alter table SIMIPRODUCT_DATA_PRODUCT add constraint FK_SIMIPRODUCT_DATA_PRODUCT_ON_THEME_PUBLICATION foreign key (THEME_PUBLICATION_ID) references SIMITHEME_THEME_PUBLICATION(ID);
create index IDX_SIMIPRODUCT_DATA_PRODUCT_ON_THEME_PUBLICATION on SIMIPRODUCT_DATA_PRODUCT (THEME_PUBLICATION_ID);
