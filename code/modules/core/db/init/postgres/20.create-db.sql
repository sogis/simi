-- begin SIMI_ROLE
create unique index IDX_SIMI_ROLE_UK_NAME on SIMI_ROLE (NAME) where DELETE_TS is null ^
-- end SIMI_ROLE
-- begin FI_LAYER_RELATION
alter table FI_LAYER_RELATION add constraint FK_FI_LAYER_RELATION_ON_DATA_SET_VIEW foreign key (DATA_SET_VIEW_ID) references PRODUCT_DATA_SET_VIEW(ID)^
alter table FI_LAYER_RELATION add constraint FK_FI_LAYER_RELATION_ON_FEATURE_INFO foreign key (FEATURE_INFO_ID) references FI_FEATURE_INFO(ID)^
create index IDX_FI_LAYER_RELATION_ON_DATA_SET_VIEW on FI_LAYER_RELATION (DATA_SET_VIEW_ID)^
create index IDX_FI_LAYER_RELATION_ON_FEATURE_INFO on FI_LAYER_RELATION (FEATURE_INFO_ID)^
-- end FI_LAYER_RELATION
-- begin SIMI_DATA_PRODUCT
alter table SIMI_DATA_PRODUCT add constraint FK_SIMI_DATA_PRODUCT_ON_PUB_SCOPE foreign key (PUB_SCOPE_ID) references SIMI_DATA_PRODUCT_PUB_SCOPE(ID)^
create unique index IDX_SIMI_DATA_PRODUCT_UK_IDENTIFIER on SIMI_DATA_PRODUCT (IDENTIFIER) where DELETE_TS is null ^
create index IDX_SIMI_DATA_PRODUCT_ON_PUB_SCOPE on SIMI_DATA_PRODUCT (PUB_SCOPE_ID)^
-- end SIMI_DATA_PRODUCT
-- begin PRODUCT_DATA_SET_VIEW
alter table PRODUCT_DATA_SET_VIEW add constraint FK_PRODUCT_DATA_SET_VIEW_ON_FEATURE_INFO foreign key (FEATURE_INFO_ID) references FI_FEATURE_INFO(ID)^
create index IDX_PRODUCT_DATA_SET_VIEW_ON_FEATURE_INFO on PRODUCT_DATA_SET_VIEW (FEATURE_INFO_ID)^
-- end PRODUCT_DATA_SET_VIEW
-- begin SIMI_IDENTITY
create unique index IDX_SIMI_IDENTITY_UK_IDENTIFIER on SIMI_IDENTITY (IDENTIFIER) where DELETE_TS is null ^
-- end SIMI_IDENTITY
-- begin SIMI_PROPERTIES_IN_LIST
alter table SIMI_PROPERTIES_IN_LIST add constraint FK_SIMI_PROPERTIES_IN_LIST_ON_PRODUCT_LIST foreign key (PRODUCT_LIST_ID) references SIMI_PRODUCT_LIST(ID)^
alter table SIMI_PROPERTIES_IN_LIST add constraint FK_SIMI_PROPERTIES_IN_LIST_ON_SINGLE_ACTOR foreign key (SINGLE_ACTOR_ID) references SIMI_SINGLE_ACTOR(ID)^
create index IDX_SIMI_PROPERTIES_IN_LIST_ON_PRODUCT_LIST on SIMI_PROPERTIES_IN_LIST (PRODUCT_LIST_ID)^
create index IDX_SIMI_PROPERTIES_IN_LIST_ON_SINGLE_ACTOR on SIMI_PROPERTIES_IN_LIST (SINGLE_ACTOR_ID)^
-- end SIMI_PROPERTIES_IN_LIST
-- begin SIMI_SINGLE_ACTOR
alter table SIMI_SINGLE_ACTOR add constraint FK_SIMI_SINGLE_ACTOR_ON_ID foreign key (ID) references SIMI_DATA_PRODUCT(ID) on delete CASCADE^
-- end SIMI_SINGLE_ACTOR
-- begin SIMI_PRODUCT_LIST
alter table SIMI_PRODUCT_LIST add constraint FK_SIMI_PRODUCT_LIST_ON_ID foreign key (ID) references SIMI_DATA_PRODUCT(ID) on delete CASCADE^
-- end SIMI_PRODUCT_LIST
-- begin SIMI_USER
alter table SIMI_USER add constraint FK_SIMI_USER_ON_ID foreign key (ID) references SIMI_IDENTITY(ID) on delete CASCADE^
-- end SIMI_USER
-- begin SIMI_GROUP
alter table SIMI_GROUP add constraint FK_SIMI_GROUP_ON_ID foreign key (ID) references SIMI_IDENTITY(ID) on delete CASCADE^
-- end SIMI_GROUP
-- begin SIMI_LAYER_LIST
alter table SIMI_LAYER_LIST add constraint FK_SIMI_LAYER_LIST_ON_ID foreign key (ID) references SIMI_PRODUCT_LIST(ID) on delete CASCADE^
-- end SIMI_LAYER_LIST
-- begin SIMI_MAP
alter table SIMI_MAP add constraint FK_SIMI_MAP_ON_ID foreign key (ID) references SIMI_PRODUCT_LIST(ID) on delete CASCADE^
-- end SIMI_MAP
-- begin SIMI_GROUP_USER_LINK
alter table SIMI_GROUP_USER_LINK add constraint FK_GROUSE_ON_GROUP foreign key (GROUP_ID) references SIMI_GROUP(ID)^
alter table SIMI_GROUP_USER_LINK add constraint FK_GROUSE_ON_USER foreign key (USER_ID) references SIMI_USER(ID)^
-- end SIMI_GROUP_USER_LINK
-- begin SIMI_ROLE_GROUP_LINK
alter table SIMI_ROLE_GROUP_LINK add constraint FK_ROLGRO_ON_GROUP foreign key (GROUP_ID) references SIMI_GROUP(ID)^
alter table SIMI_ROLE_GROUP_LINK add constraint FK_ROLGRO_ON_ROLE foreign key (ROLE_ID) references SIMI_ROLE(ID)^
-- end SIMI_ROLE_GROUP_LINK
-- begin SIMI_ROLE_USER_LINK
alter table SIMI_ROLE_USER_LINK add constraint FK_ROLUSE_ON_USER foreign key (USER_ID) references SIMI_USER(ID)^
alter table SIMI_ROLE_USER_LINK add constraint FK_ROLUSE_ON_ROLE foreign key (ROLE_ID) references SIMI_ROLE(ID)^
-- end SIMI_ROLE_USER_LINK
