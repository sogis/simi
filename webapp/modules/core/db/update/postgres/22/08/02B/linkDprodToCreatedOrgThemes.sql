-- Skript erstellt nach den Organisationseinheiten aufgeteilte Pseudothemen und weist die bestehenden Datenprodukte den Pseudothemen zu
WITH

amt_lookup AS (
  SELECT
    *
  FROM (
    VALUES
      ('agi', 'Amt für Geoinformation'),
      ('afu', 'Amt für Umwelt'),
      ('ada', 'Amt für Denkmalschutz und Archäologie'),
      ('avt', 'Amt für Verkehr und Tiefbau'),
      ('arp', 'Amt für Raumplanung'),
      ('awjf', 'Amt für Wald, Jagd und Fischerei'),
      ('alw', 'Amt für Landwirtschaft'),
      ('amb', 'Amt für Militär und Bevölkerungsschutz'),
      ('ksta', 'Steueramt KSTA'),
      ('awa', 'Amt für Wirtschaft und Arbeit'),
      ('sgv', 'Solothurnische Gebäudeversicherung')
  )
  AS t (amt_key, amt_name)
),

derived_orgs AS (
  SELECT
    split_part(dp.derived_identifier , '.', 3) AS amt_ident,
    public.uuid_generate_v4 () AS uid
  FROM
    simiproduct_data_product dp
  GROUP BY
    split_part(dp.derived_identifier , '.', 3)
),

org_base AS (
  SELECT
    uid,
    amt_ident,
    amt_name
  FROM
    derived_orgs
  INNER JOIN
    amt_lookup ON amt_ident = amt_key
),

theme_base AS (
  SELECT
    public.uuid_generate_v4 () AS theme_uid,
    public.uuid_generate_v4 () AS themepub_uid,
    uid AS data_owner_id,
    concat('orgtheme.', amt_ident) AS ident,
    concat(amt_ident, ' Pseudo Thema') AS title,
    concat('Pseudo-Thema für die Organisation ', amt_name) AS description,
    'ktso' AS coverage_ident,
    amt_ident
  FROM
    org_base
),

org_unit_ins AS (
  INSERT INTO simitheme_org_unit(id, VERSION, dtype, name)
  SELECT uid, 1, 'simiTheme_Agency', amt_name FROM org_base
),

agency_ins AS (
  INSERT INTO simitheme_agency(id, abbreviation, url, phone, email)
  SELECT uid, amt_ident, 'url', 'phone', 'email' FROM org_base
),

theme_ins AS (
  INSERT INTO simitheme_theme(id, VERSION, identifier, data_owner_id, title, description)
  SELECT theme_uid, 1, ident, data_owner_id, title, description FROM theme_base
),

themepub_ins AS (
  INSERT INTO simitheme_theme_publication(id, VERSION, THEME_ID, DATA_CLASS, coverage_ident)
  SELECT themepub_uid, 1, theme_uid, 'tableSimple', 'ktso' FROM theme_base
)

--update dataprod set fk
UPDATE
  simiproduct_data_product
SET
  theme_publication_id = themepub_uid
FROM
  theme_base
WHERE
  split_part(derived_identifier , '.', 3) = amt_ident
;