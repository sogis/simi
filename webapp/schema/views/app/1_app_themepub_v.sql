/* 
 * Gibt alle für den Datenbezug (Datensuche, Datenbeschreibung, Geocat) relevanten Informationen als
 * jsonb aus. 
 * Jede Zeile umfasst alle Informationen zu einer Themenbereitstellung. Die View wird 
 * im meta2File.jar (CLI-Tool) abgefragt.
 */
CREATE VIEW simi.app_themepub_v AS 

WITH

-- Regionen **********************************************************************************

area_geom_simplified AS (
  SELECT 
    st_snaptogrid(
      ST_SimplifyPreserveTopology(
        st_geomfromwkb(geom_wkb),
        5
      ),
      1.0
    ) AS geom_simplified,
    identifier,
    title,
    id AS sub_id
  FROM
    simi.simitheme_sub_area
)

,area_geom_simplified_envelope AS (
  SELECT 
    st_envelope(geom_simplified) AS geom_bbox,
    geom_simplified,
    identifier,
    title,
    sub_id
  FROM
    area_geom_simplified
)

,tp_pub_region AS (
  SELECT 
    jsonb_build_object(
        'left', st_xmin(geom_bbox),
        'bottom', st_ymin(geom_bbox),
        'right', st_xmax(geom_bbox),
        'top', st_ymax(geom_bbox)
    ) AS bbox,
    st_astext(geom_simplified) AS geom_simplified_wkt,
    identifier,
    title,
    published,
    prev_published,
    theme_publication_id AS tp_id
  FROM 
    simi.simitheme_published_sub_area ps
  JOIN
    area_geom_simplified_envelope s ON ps.sub_area_id = s.sub_id
)

,tp_pub_regions AS (
  SELECT 
    jsonb_agg(
      jsonb_build_object(
        'bbox', bbox,
        'geometry', geom_simplified_wkt,
        'identifier', identifier,
        'title', title,
        'lastPublishingDate', published,
        'secondToLastPublishingDate', prev_published
      )
    ) AS pub_regions,
    tp_id
  FROM
    tp_pub_region
  GROUP BY 
    tp_id
)

,tp_envelope AS (
  SELECT 
    st_envelope(
      st_collect(geom_bbox)
    ) AS geom_bbox,
    theme_publication_id AS tp_id
  FROM
    area_geom_simplified_envelope sub
  JOIN
    simi.simitheme_published_sub_area psa ON sub.sub_id = psa.sub_area_id 
  GROUP BY 
    psa.theme_publication_id 
)

,tp_bbox AS (
    SELECT 
      jsonb_build_object(
        'left', st_xmin(geom_bbox),
        'bottom', st_ymin(geom_bbox),
        'right', st_xmax(geom_bbox),
        'top', st_ymax(geom_bbox)
      ) AS bbox,
      tp_id
    FROM
      tp_envelope
)

-- Publish-Dates *****************************************************************

,tp_pub_dates AS (
  SELECT 
    max(published) AS published,
    min(prev_published) AS prev_published,
    theme_publication_id AS tp_id
  FROM 
    simi.simitheme_published_sub_area ps
  GROUP BY 
    theme_publication_id
)

-- Themen-Bereitstellung (alles zusammengenommen) ********************************



,themepub AS (
  SELECT 
    identifier,
    public_model_name,
    published,
    prev_published,
    title,
    short_description,
    keywords,
    synonyms,
    data_owner,
    data_servicer,
    further_info,
    'https://geo.so.ch/deprecated' AS licence,
    file_formats,
    pub_regions,
    bbox,
    tables_json,
    services,
    tp.tp_id
  FROM
    simi.app_themepub_base_v tp
  JOIN
    tp_pub_dates dates ON tp.tp_id = dates.tp_id
  JOIN
    tp_pub_regions reg ON tp.tp_id = reg.tp_id
  JOIN
    tp_bbox bbox ON tp.tp_id = bbox.tp_id
)

SELECT 
  jsonb_pretty(
    jsonb_build_object(
      'identifier', identifier,
      'model', public_model_name,
      'lastPublishingDate', published,
      'secondToLastPublishingDate', prev_published,    
      'title', title,
      'shortDescription', short_description,
      'keywordsList', keywords,
      'synonymsList', synonyms,
      'furtherInformation', further_info,
      'licence', licence,
      'owner', data_owner,
      'servicer', data_servicer,
      'tablesInfo', tables_json,
      'services', services,
      'items', pub_regions,
      'bbox', bbox,
      'fileFormats', file_formats
    ) 
  ) AS tp_json,
  tp_id
FROM
  themepub
;