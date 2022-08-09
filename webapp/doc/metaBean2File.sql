WITH

-- Tabellen ********************************************************************************************

attr AS (
  SELECT 
    json_build_object(
      'name', name,
      'alias', alias,
      'short_description', coalesce(description_override, description_model),
      'datatype', 
      CASE
        WHEN str_length IS NOT NULL THEN concat(type_name, '(', str_length, ')')
        ELSE type_name
      END,
      'mandatory', mandatory
    ) AS attr_json,
    table_view_id
  FROM 
    simidata_view_field vf
  JOIN
    simidata_table_field tf ON vf.table_field_id = tf.id 
  WHERE 
      cat_synced = TRUE 
)

,attrs AS (
  SELECT 
    json_agg(attr_json) AS attrs_json,
    table_view_id
  FROM
    attr 
  GROUP BY 
    table_view_id
)

,tableview AS (
  SELECT   
    jsonb_build_object(
      'sql_name', table_name,
      'title', title,
      'table_type', coalesce(geo_type, 'Tabelle ohne Geometrie'),--Dies muss sicher mit. Falls die Geometriespalte auch benannt ist: IN geeigneter Form Spaltenname mitgeben...
      'short_description', coalesce(description_override, description_model),
      'attributes_info', attrs_json
    ) AS tbl_json,
    table_view_id
  FROM 
    simidata_table_view tv
  JOIN
    simidata_postgres_table tbl ON tbl.id = tv.postgres_table_id  
  JOIN 
    attrs a ON tv.id = a.table_view_id
)

,tp_tables AS (
  SELECT 
    jsonb_agg(tbl_json) AS tables,
    dp.theme_publication_id AS tp_id
  FROM 
    tableview tv
  JOIN
    simiproduct_data_product dp ON tv.table_view_id = dp.id
  WHERE 
    dp.theme_only_for_org IS FALSE
  GROUP BY 
    theme_publication_id
)

-- Organisationen ******************************************************************************************

,agency AS (
  SELECT 
    org.name AS amt,
    abbreviation,
    NULL AS division,
    url,
    email,
    phone,
    amt.id AS org_id
  FROM 
    simitheme_agency amt
  JOIN
    simitheme_org_unit org ON amt.id = org.id 
) 

,division AS (
  SELECT 
    amt.amt,
    amt.abbreviation,
    org.name AS division,
    COALESCE(sub.url, amt.url) AS url,
    COALESCE(sub.email, amt.email) AS email,
    COALESCE(sub.phone, amt.phone) AS phone,
    sub.id AS org_id
  FROM 
    simitheme_sub_org sub
  JOIN
    agency amt ON sub.agency_id = amt.org_id
  JOIN 
    simitheme_org_unit org ON sub.id = org.id 
)

,org_raw AS (
  SELECT amt, abbreviation, division, url, email, phone, org_id FROM agency
  UNION ALL 
  SELECT amt, abbreviation, division, url, email, phone, org_id FROM division
)

,org AS (
  SELECT 
    jsonb_build_object(
      'agency_name', amt,
      'abbreviation', abbreviation,
      'division', division,
      'office_at_web', url,
      'email', email,
      'phone', phone
    ) AS org_json, 
    abbreviation,
    org_id
  FROM 
    org_raw
)

,org_agi AS (
  SELECT 
    org_json AS servicer
  FROM
    org
  WHERE 
    lower(abbreviation) = 'agi'
)

,tp_org AS (
  SELECT 
    org_json AS owner,
    servicer,
    tp.id AS tp_id
  FROM
    simitheme_theme th
  JOIN
    org ON th.data_owner_id = org.org_id
  JOIN 
    simitheme_theme_publication tp ON th.id = tp.theme_id 
  CROSS JOIN 
    org_agi
)

-- public model name ******************************************************************************************

,tbl_model AS (
  SELECT 
    tbl.id AS tbl_id,
    'SO_DUMMYMODEL' AS model
  FROM 
    simidata_postgres_table tbl
  JOIN
    simidata_db_schema s ON tbl.db_schema_id = s.id
)

,tp_model AS (
  SELECT
    dp.theme_publication_id AS tp_id,
    max(model) AS model,
    min(model) AS model2 --debug: muss gleich sein wie model, sonst ist die konf nicht gut
  FROM 
    simidata_table_view tv
  JOIN
    simidata_data_set_view dsv USING(id)
  JOIN 
    simiproduct_data_product dp USING(id)
  JOIN
    tbl_model m ON tv.postgres_table_id = m.tbl_id
  GROUP BY
    dp.theme_publication_id 
)

/* --todo where ergänzen in download_tv
  WHERE 
      dsv.is_file_download_dsv IS TRUE 
    AND 
      dp.theme_only_for_org IS FALSE 
*/

-- Regionen **********************************************************************************

,sub_area AS (
  SELECT 
    st_envelope(st_geomfromwkb(geom_wkb)) AS geom_bbox,
    identifier,
    title,
    id AS sub_id
  FROM
    simitheme_sub_area
)

,tp_pub_region AS (
  SELECT 
    jsonb_build_object(
        'left', st_xmin(geom_bbox),
        'lower', st_ymin(geom_bbox),
        'right', st_xmax(geom_bbox),
        'upper', st_ymax(geom_bbox)
    ) AS bbox,
    identifier,
    title,
    published,
    prev_published,
    theme_publication_id AS tp_id
  FROM 
    simitheme_published_sub_area ps
  JOIN
    sub_area s ON ps.sub_area_id = s.sub_id
)

,tp_pub_regions AS (
  SELECT 
    jsonb_agg(
      jsonb_build_object(
        'bbox', bbox,
        'identifier', identifier,
        'name', title,
        'last_publishing_date', published,
        'second_to_last_publishing_date', prev_published
      )
    ) AS items,
    tp_id
  FROM
    tp_pub_region
  GROUP BY 
    tp_id
)

-- Schlagwörter (!!ALPHA!!) **********************************************************************************

,tp_keywords AS (
  SELECT 
    '["k1","k2"]'::jsonb AS keywords,
    tp.id AS tp_id
  FROM
    simitheme_theme_publication tp
)

,tp_synonyms AS (
  SELECT 
    '["s1","s2"]'::jsonb AS synonyms,
    tp.id AS tp_id
  FROM
    simitheme_theme_publication tp
)

/*
,tv_tbl_therms AS (
  SELECT
    tv.id AS tv_id,
    dp.theme_publication_id,
    tbl.keywords_arr::jsonb AS keys,
    tbl.synonyms_arr::jsonb AS synos
  FROM 
    simidata_table_view tv
  JOIN
    simidata_data_set_view dsv USING(id)
  JOIN 
    simiproduct_data_product dp USING(id)
  JOIN
    simidata_postgres_table tbl ON tv.postgres_table_id = tbl.id
  WHERE 
    keywords_arr IS NOT NULL OR synonyms_arr IS NOT NULL 
)

--todo where ergänzen in download_tv
  WHERE 
      dsv.is_file_download_dsv IS TRUE 
    AND 
      dp.theme_only_for_org IS FALSE 


,themepub_keyword AS (
  SELECT DISTINCT
    theme_publication_id,
    exploded.value AS therm
  FROM
    tv_tbl_therms
  CROSS JOIN LATERAL 
    jsonb_array_elements_text(keys) exploded
)

,tb_keywords AS (
  SELECT 
    json_agg(therm ORDER BY therm)::varchar AS keywords,
    theme_publication_id
  FROM 
    themepub_keyword
  GROUP BY 
    theme_publication_id
)
*/

-- Dateitypen *************************************************************************

,vec_default_filetype_raw AS (
  SELECT 
    * 
  FROM (
    VALUES 
      ('Shapefile', 'shp', 'application/vnd.esri_shapefile', true),
      ('Geopackage', 'gpkg', 'application/geopackage+sqlite3', true),
      ('INTERLIS2', 'xtf', 'application/vnd.ili2+xml', false),
      ('DXF (Autocad)', 'dxf', 'application/dxf+text', true)
  ) 
  AS t (name, kuerzel, mime_type, only_pub_models)
)

,vec_default_filetype AS (
    SELECT 
      jsonb_build_object(
        'name', name,
        'kuerzel', kuerzel,
        'mime_type', mime_type
      ) AS filetype_json,
      only_pub_models
    FROM
      vec_default_filetype_raw
)

,simple_default_filetypes AS (
  SELECT 
    jsonb_agg(filetype_json) AS simple_ftypes,
    'tableSimple' AS dclass
  FROM
    vec_default_filetype  
)

,rel_default_filetypes AS (
  SELECT 
    jsonb_agg(filetype_json) AS rel_ftypes,
    'tableRelational' AS dclass
  FROM
    vec_default_filetype  
  WHERE 
    only_pub_models IS FALSE 
)

,themepub_custom_types AS (
  SELECT 
    jsonb_build_object(
        'name', ft.name,
        'kuerzel', ft.kuerzel,
        'mime_type', ft.mime_type
    ) AS custom_ftypes,
    link.theme_publication_id AS tp_id
  FROM 
    simitheme_file_type ft
  JOIN
    simitheme_theme_publication_custom_file_type_link link ON ft.id = link.custom_file_type_id 
  GROUP BY 
    link.theme_publication_id,
    ft."name",
    ft.kuerzel,
    ft.mime_type
)

,tp_filetypes AS (
  SELECT 
    tp.id AS tp_id,
    COALESCE(custom_ftypes, simple_ftypes, rel_ftypes) AS file_formats
  FROM 
    simitheme_theme_publication tp 
  LEFT JOIN
    simple_default_filetypes s ON tp.data_class = s.dclass
  LEFT JOIN 
    rel_default_filetypes r ON tp.data_class = r.dclass
  LEFT JOIN 
    themepub_custom_types c ON tp.id = c.tp_id
)

-- Publish-Dates *****************************************************************

,tp_pub_dates AS (
  SELECT 
    max(published) AS last_publishing_date,
    min(prev_published) AS second_to_last_publishing_date,
    theme_publication_id AS tp_id
  FROM 
    simitheme_published_sub_area ps
  GROUP BY 
    theme_publication_id
)

-- Identifier, Name, Descr ********************************************************************

,identsuffix_map AS (
  SELECT 
    * 
  FROM (
    VALUES 
      ('tableSimple', NULL),
      ('tableRelational', 'relational'),
      ('nonTabular', '!NoDefaultAvailable')
  ) 
  AS t (data_class, default_suffix)
)

,tp_ident_title_desc AS (
  SELECT 
    concat_ws('.', th.identifier, coalesce(class_suffix_override, default_suffix)) AS identifier,
    COALESCE(tp.title_override, th.title) AS title,
    COALESCE(tp.description_override, th.description) AS short_description,
    'todo furtherInfo' AS furtherInformation,
    tp.id AS tp_id
  FROM
    simitheme_theme_publication tp
  JOIN
    simitheme_theme th ON tp.theme_id = th.id 
  JOIN
    identsuffix_map suff ON tp.data_class = suff.data_class
)

-- Services *********************************************************************************

--todo nur die effektiv publizierten und öffentlich zugänglichen Ebenen ausgeben
,layers AS (
  SELECT 
    jsonb_agg(
      jsonb_build_object(
        'title', title,
        'identifier', derived_identifier
      )
    ) AS layers,
    theme_publication_id AS tp_id
  FROM 
    simiproduct_data_product dp 
  WHERE 
    dp.theme_only_for_org IS FALSE OR dp.theme_only_for_org IS TRUE --todo anpassen
  GROUP BY
    dp.theme_publication_id 
)

,tp_services AS (
  SELECT 
    jsonb_build_object(
      'layers', layers,
      'endpoint', 'https://geo.so.ch/api/wms'
    ) AS service,
    tp_id
  FROM
    layers
)

-- Themen-Bereitstellung (alles zusammengenmommen) ********************************

,themepub AS (
  SELECT 
    inf.identifier,
    model,
    last_publishing_date,
    second_to_last_publishing_date,
    inf.title,
    inf.short_description,
    keywords,
    synonyms,
    OWNER,
    servicer,
    furtherInformation,
    'fuu' AS license,
    'https://data.geo.so.ch' AS base_url,
    file_formats,
    items,
    TABLES,
    service
  FROM
    simitheme_theme_publication tp
  JOIN
    tp_ident_title_desc inf ON tp.id = inf.tp_id
  LEFT JOIN 
    tp_model model ON tp.id = model.tp_id
  LEFT JOIN --todo INNER join
    tp_pub_dates dates ON tp.id = dates.tp_id
  LEFT JOIN 
    tp_keywords keys ON tp.id = keys.tp_id
  LEFT JOIN 
    tp_synonyms syn ON tp.id = syn.tp_id
  JOIN 
    tp_org org ON tp.id = org.tp_id
  JOIN 
    tp_filetypes ft ON tp.id = ft.tp_id
  LEFT JOIN --todo INNER join
    tp_pub_regions reg ON tp.id = reg.tp_id
  LEFT JOIN 
    tp_tables tab ON tp.id = tab.tp_id
  LEFT JOIN 
    tp_services srv ON tp.id = srv.tp_id
)

SELECT * FROM themepub
;





