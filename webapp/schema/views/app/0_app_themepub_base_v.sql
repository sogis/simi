/* 
 * Gibt alle für den Datenbezug (Datensuche, Datenbeschreibung, Geocat) relevanten manuell konfigurierten
 * Informationen als jsonb aus. 
 */
CREATE VIEW simi.app_themepub_base_v AS 

WITH

-- Permissions *****************************************************************************************

public_dp_ids AS (
  SELECT 
    resource_id AS dp_id
  FROM 
    simi.trafo_resource_role_v
  WHERE 
    role_name = 'public'
  AND 
    perm_level IN ('1_read','2_read_write')
)

-- Tabellen ********************************************************************************************

,tbl_attr_type_map AS (
  SELECT 
    * 
  FROM (
    VALUES 
      ('int8', 'Integer'),
      ('timestamptz', 'DateTime'),
      ('varchar', 'Text'),
      ('jsonb', 'Json'),
      ('bool', 'Boolean'),
      ('int4', 'Integer'),
      ('timestamp', 'DateTime'),
      ('text', 'Text'),
      ('int2', 'Integer'),
      ('float8', 'Number'),
      ('numeric', 'Number'),
      ('uuid', 'UUID'),
      ('float4', 'Number'),
      ('date', 'Date')
  ) 
  AS t (db_type, display_type)
)

,tv_nongeo_attr AS (
  SELECT 
    "name",
    alias,
    COALESCE(display_type, type_name) AS type_name,
    mandatory,
    coalesce(description_override, description_model) AS shortDescription,
    table_view_id
  FROM
    simi.simidata_view_field vf
  JOIN
    simi.simidata_table_field tf ON vf.table_field_id = tf.id 
  LEFT JOIN
    tbl_attr_type_map t ON tf.type_name = t.db_type
  WHERE 
    cat_synced = TRUE 
)

,tv_geo_attr AS (
  SELECT
    geo_field_name AS "name", 
    geo_field_name AS alias, 
    geo_type AS type_name,
    TRUE AS mandatory,
    NULL AS shortDescription,
    tv.id AS table_view_id
  FROM 
    simi.simidata_postgres_table t
  JOIN
    simi.simidata_table_view tv ON t.id = tv.postgres_table_id
  WHERE 
    geo_field_name IS NOT NULL 
)

,tv_attr AS (
  SELECT 
    json_build_object(
      'name', name,
      'alias', alias,
      'shortDescription', shortDescription,
      'datatype', type_name,
      'mandatory', mandatory
    ) AS attr_json,
    table_view_id
  FROM
    (
    SELECT * FROM tv_geo_attr
    UNION ALL
    SELECT * FROM tv_nongeo_attr
    ) t
)

,tv_attrs AS (
  SELECT 
    json_agg(attr_json) AS attrs_json,
    table_view_id
  FROM
    tv_attr 
  GROUP BY 
    table_view_id
)

,tableview AS (
  SELECT   
    jsonb_build_object(
      'sqlName', COALESCE(doc_table_name, table_name),
      'title', COALESCE(tbl.title, dp.title, '-'),
      'shortDescription', coalesce(description_override, description_model),
      'attributesInfo', attrs_json
    ) AS tbl_json,
    tv.id AS table_view_id,
    theme_publication_id
  FROM 
    simi.simidata_table_view tv
  JOIN
    public_dp_ids pdi ON tv.id = pdi.dp_id
  JOIN
    simi.simidata_postgres_table tbl ON tbl.id = tv.postgres_table_id  
  JOIN 
    tv_attrs a ON tv.id = a.table_view_id
  JOIN
    simi.simidata_data_set_view dsv ON tv.id = dsv.id
  JOIN
    simi.simiproduct_data_product dp ON tv.id = dp.id
  WHERE  
      dsv.is_file_download_dsv IS TRUE 
    AND 
      dp.theme_only_for_org IS FALSE   
)

,tp_tables AS (
  SELECT 
    jsonb_agg(tbl_json) AS tables_json,
    theme_publication_id AS tp_id
  FROM 
    tableview tv  
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
    'mailto:' || email AS email,
    phone,
    amt.id AS org_id
  FROM 
    simi.simitheme_agency amt
  JOIN
    simi.simitheme_org_unit org ON amt.id = org.id 
) 

,division AS (
  SELECT 
    amt.amt,
    amt.abbreviation,
    org.name AS division,
    COALESCE(sub.url, amt.url) AS url,
    COALESCE('mailto:' || sub.email, amt.email) AS email,
    COALESCE(sub.phone, amt.phone) AS phone,
    sub.id AS org_id
  FROM 
    simi.simitheme_sub_org sub
  JOIN
    agency amt ON sub.agency_id = amt.org_id
  JOIN 
    simi.simitheme_org_unit org ON sub.id = org.id 
)

,org_raw AS (
  SELECT amt, abbreviation, division, url, email, phone, org_id FROM agency
  UNION ALL 
  SELECT amt, abbreviation, division, url, email, phone, org_id FROM division
)

,org AS (
  SELECT 
    jsonb_build_object(
      'agencyName', amt,
      'abbreviation', abbreviation,
      'division', division,
      'officeAtWeb', url,
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
    org_json AS data_owner,
    servicer AS data_servicer,
    tp.id AS tp_id
  FROM
    simi.simitheme_theme th
  JOIN
    org ON th.data_owner_id = org.org_id
  JOIN 
    simi.simitheme_theme_publication tp ON th.id = tp.theme_id 
  CROSS JOIN 
    org_agi
)

-- Schlagwörter **********************************************************************************

,tp_therms AS (
  SELECT 
    keywords_arr::jsonb AS keywords,
    synonyms_arr::jsonb AS synonyms,
    tp.id AS tp_id
  FROM
    simi.simitheme_theme t
  JOIN
    simi.simitheme_theme_publication tp ON t.id = tp.theme_id 
)

-- Dateitypen *************************************************************************

,ft_per_tp_default AS ( -- default filetypes for all tp with dataclass "tablesimple"
  SELECT 
    jsonb_agg(
      jsonb_build_object(
          'name', ft.name,
          'abbreviation', ft.kuerzel,
          'mimetype', ft.mime_type
      )
    ) AS formats,
    tp.id AS tp_id
  FROM 
    simi.simitheme_file_type ft
  CROSS JOIN
    simi.simitheme_theme_publication tp
  WHERE 
      ft.kuerzel IN ('xtf.zip', 'gpkg.zip', 'shp.zip', 'dxf.zip', 'xtf', 'gpkg', 'shp', 'dxf')  
    AND 
      tp.data_class = 'tableSimple'
  GROUP BY 
    tp.id
)

,ft_per_tp_configured AS (
  SELECT 
    jsonb_agg(
      jsonb_build_object(
          'name', ft.name,
          'abbreviation', ft.kuerzel,
          'mimetype', ft.mime_type
      )
    ) AS formats,
    theme_publication_id AS tp_id
  FROM
    simi.simitheme_theme_publication_custom_file_type_link link
  JOIN
    simi.simitheme_file_type ft ON link.custom_file_type_id = ft.id
  GROUP BY 
    theme_publication_id
)

,tp_filetypes AS (
  SELECT 
    COALESCE(conf.formats, def.formats) AS file_formats,
    COALESCE(conf.tp_id, def.tp_id) AS tp_id
  FROM 
    ft_per_tp_configured conf
  FULL OUTER JOIN 
    ft_per_tp_default def ON conf.tp_id = def.tp_id
)

-- Identifier, Name, Descr ********************************************************************

,tp_ident_title_desc AS (
  SELECT 
    concat_ws('.', th.identifier, class_suffix_override) AS identifier,
    COALESCE(tp.title_override, th.title) AS title,
    COALESCE(tp.description_override, th.description) AS short_description,
    further_info_url AS further_info,
    tp.id AS tp_id
  FROM
    simi.simitheme_theme_publication tp
  JOIN
    simi.simitheme_theme th ON tp.theme_id = th.id 
)

-- Services *********************************************************************************

,layer_viewsrv_base AS (
  SELECT 
    c.pub_to_wms::int,
    c.pub_to_wgc::int,
    dp.id AS dp_id
  FROM 
    simi.simiproduct_data_product dp
  JOIN
    simi.simiproduct_data_product_pub_scope c ON dp.pub_scope_id  = c.id
)

,parent_layergroups AS (
  SELECT 
    pub_to_wms,
    pub_to_wgc,
    pil.single_actor_id AS sa_id
  FROM 
    simi.simiproduct_properties_in_list pil
  JOIN
    simi.simiproduct_layer_group lg ON pil.product_list_id = lg.id
  JOIN 
    layer_viewsrv_base l ON pil.product_list_id = l.dp_id
)

,layer_viewsrv_marker AS (
  SELECT 
    CASE 
      WHEN max(pub_to_wms) = 1 THEN 'WMS'
      ELSE null
    END AS wms_marker,
    CASE 
      WHEN max(pub_to_wgc) = 1 THEN 'WGC'
      ELSE null
    END AS wgc_marker,
    dp_id
  FROM (
    SELECT pub_to_wms, pub_to_wgc, sa_id AS dp_id FROM parent_layergroups
    UNION ALL 
    SELECT pub_to_wms, pub_to_wgc, dp_id FROM layer_viewsrv_base
  ) t  
  GROUP BY 
    dp_id
)

,layer_datasrv_marker AS (
  SELECT 
    dsv.id AS dp_id,
    'DATA,WFS' AS data_marker,
    tbl.title AS tbl_title
  FROM 
    simi.simidata_data_set_view dsv
  JOIN
    simi.simidata_table_view tv ON dsv.id = tv.id
  JOIN
    simi.simidata_postgres_table tbl ON tv.postgres_table_id = tbl.id
  WHERE 
    service_download IS TRUE 
)

,layer_chann_conc AS (
  SELECT 
    COALESCE(v.dp_id, d.dp_id) AS dp_id,
    d.tbl_title,
    concat_ws(',', wms_marker, wgc_marker, data_marker) AS channel_markers
  FROM
    layer_viewsrv_marker v
  FULL OUTER JOIN 
    layer_datasrv_marker d ON v.dp_id = d.dp_id
)

,layer_chann_exploded AS (
  SELECT 
    UNNEST(string_to_array(channel_markers, ',')) AS channel,
    dp.derived_identifier,
    COALESCE(dp.title, tbl_title, derived_identifier) AS title,
    dp.theme_publication_id
  FROM
    layer_chann_conc c
  JOIN
    public_dp_ids pdi ON c.dp_id = pdi.dp_id
  JOIN
    simi.simiproduct_data_product dp ON c.dp_id = dp.id
  WHERE 
    dp.theme_only_for_org IS FALSE
)

,tp_chann_raw AS (
  SELECT 
    channel,
    jsonb_build_object(
      'identifier', derived_identifier,
      'title', title
    ) AS layer_props,
    tp.id AS tp_id
  FROM 
    layer_chann_exploded l
  JOIN
    simi.simitheme_theme_publication tp ON l.theme_publication_id = tp.id
)

,tp_service_raw AS (
  SELECT 
    channel,
    jsonb_agg(layer_props) AS layers,
    tp_id
  FROM 
    tp_chann_raw
  GROUP BY
    channel,
    tp_id
)

,tp_services AS (
  SELECT 
    jsonb_agg(
      jsonb_build_object(
        'type', channel,
        'endpoint', channel,
        'layers', layers
      )
    ) AS services,
    tp_id
  FROM
    tp_service_raw
  GROUP BY 
    tp_id
)

-- Themen-Bereitstellung (alles zusammengenommen) ********************************

,themepub AS (
  SELECT 
    inf.identifier,
    public_model_name,
    inf.title,
    inf.short_description,
    keywords,
    synonyms,
    data_owner,
    data_servicer,
    further_info,
    file_formats,
    tables_json,
    services,
    data_class != 'tableRelational' as portals_published,
    tp.id AS tp_id
  FROM
    simi.simitheme_theme_publication tp
  JOIN
    tp_ident_title_desc inf ON tp.id = inf.tp_id
  JOIN
    tp_therms th ON tp.id = th.tp_id
  JOIN 
    tp_org org ON tp.id = org.tp_id
  JOIN 
    tp_filetypes ft ON tp.id = ft.tp_id
  LEFT JOIN 
    tp_tables tab ON tp.id = tab.tp_id
  LEFT JOIN 
    tp_services srv ON tp.id = srv.tp_id
)

SELECT 
    *
FROM
    themepub
;