
WITH

pairing AS (
  SELECT
    'av_gem' AS coverage_ident,
    'ch.so.agi.av' AS themepub_ident
  FROM
    pg_catalog.generate_series(1, 1)
)

,pubSubArea_Draft AS (
  SELECT
    p.coverage_ident,
    p.themepub_ident,
    sa.identifier,
    tp.id AS theme_publication_id,
    sa.id AS sub_area_id,
    localtimestamp AS published,
    localtimestamp AS prev_published,
    uuid_generate_v4() AS id,
    1 AS "version"
  FROM
    simitheme_theme t
  JOIN
    simitheme_theme_publication tp ON t.id = tp.theme_id
  JOIN
    pairing p ON t.identifier = p.themepub_ident -- Nur für Testdaten OK - Joint alle themePubs des themas
  JOIN
    simitheme_sub_area sa ON p.coverage_ident = sa.coverage_ident
)

INSERT INTO
  simitheme_published_sub_area (
    theme_publication_id,
    sub_area_id,
    published,
    prev_published,
    id,
    "version"
  )
SELECT
    theme_publication_id,
    sub_area_id,
    published,
    prev_published,
    id,
    "version"
FROM
  pubSubArea_Draft
;

