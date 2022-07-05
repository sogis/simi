-- alter table SIMIPRODUCT_DATA_PRODUCT add column THEME_PUBLICATION_ID uuid ^
-- update SIMIPRODUCT_DATA_PRODUCT set THEME_PUBLICATION_ID = <default_value> ;
-- alter table SIMIPRODUCT_DATA_PRODUCT alter column THEME_PUBLICATION_ID set not null ;
--alter table SIMIPRODUCT_DATA_PRODUCT add column THEME_PUBLICATION_ID uuid not null ;

-- je
alter table SIMIPRODUCT_DATA_PRODUCT add column THEME_PUBLICATION_ID uuid ^

INSERT INTO simitheme_org_unit(id, VERSION, dtype, name)
VALUES ('8b2285f7-4ea2-4bca-adde-b4fbf6e54723', 1, 'simiTheme_Agency', 'org.from.migration') ^

INSERT INTO simitheme_agency(id, abbreviation, url, phone, email)
VALUES ('8b2285f7-4ea2-4bca-adde-b4fbf6e54723', 'db.mig', 'url', 'phone', 'email') ^

INSERT INTO simitheme_theme(
  id,
  VERSION,
  identifier,
  data_owner_id,
  title,
  description,
  coverage_ident
)
VALUES (
  '648a60d1-f109-4331-8c2a-2fe676fa5768',
  1,
  'theme.from.migration',
  '8b2285f7-4ea2-4bca-adde-b4fbf6e54723',
  'theme.from.migration',
  'theme.from.migration',
  'coverage_ident.from.migration'
) ^

INSERT INTO simitheme_theme_publication(
  id,
  VERSION,
  THEME_ID,
  DATA_CLASS
)
VALUES (
  '3319c99f-52b2-485d-adbd-c5914111c17a',
  1,
  '648a60d1-f109-4331-8c2a-2fe676fa5768',
  'tableSimple'
) ^

update SIMIPRODUCT_DATA_PRODUCT set THEME_PUBLICATION_ID = '3319c99f-52b2-485d-adbd-c5914111c17a' ^

alter table SIMIPRODUCT_DATA_PRODUCT alter column THEME_PUBLICATION_ID set not null ;







