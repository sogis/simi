alter table SIMI_STYLE_ASSET add column IS_FOR_SERVER boolean ^
update SIMI_STYLE_ASSET set IS_FOR_SERVER = false where IS_FOR_SERVER is null ;
alter table SIMI_STYLE_ASSET alter column IS_FOR_SERVER set not null ;
