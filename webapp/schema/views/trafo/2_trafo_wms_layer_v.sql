CREATE VIEW simi.trafo_wms_layer_v AS

/*
 * Gibt für den WMS die Dataproducts (DP) mit ihren jeweiligen Detailinformationen aus.
 * 
 * Mittels Flag "print_or_ext" wird unterschieden, ob das DP nur im "Print-WMS" erscheint. 
 */
WITH

-- CTE für WMS-weit eindeutige Datasetview identifier

published_dsv AS (
  SELECT 
    id AS dsv_id,
    p.root_published
  FROM
    simi.simidata_data_set_view d
  JOIN
    simi.trafo_published_dp_v p ON d.id = p.dp_id 
),

layergroup_children AS (
  SELECT 
    product_list_id AS lg_id,
    single_actor_id AS sa_id
  FROM 
    simi.simiproduct_layer_group lg
  JOIN 
    simi.simiproduct_properties_in_list pil ON lg.id = pil.product_list_id
),

facade_dsv_children AS (
  SELECT 
    data_set_view_id AS child_id, 
    facade_layer_id AS parent_id,
    lg_id AS grandpa_id
  FROM 
    simi.simiproduct_properties_in_facade pif
  JOIN
    published_dsv p ON pif.data_set_view_id = p.dsv_id
  LEFT JOIN 
    layergroup_children lg ON pif.facade_layer_id = lg.sa_id
),

group_dsv_children AS (
  SELECT 
    product_list_id AS parent_id, 
    single_actor_id AS child_id
  FROM 
    simi.simiproduct_properties_in_list pil
  JOIN 
    simi.simiproduct_layer_group lg ON pil.product_list_id = lg.id 
  JOIN
    published_dsv p ON pil.single_actor_id = p.dsv_id   
),

root_dsv AS (
  SELECT 
    dsv_id AS child_id
  FROM 
    published_dsv
  WHERE
    root_published IS TRUE            
),

dsv_treenodes AS (
  SELECT child_id, parent_id, grandpa_id, 'facade' AS parent_type, 3 AS name_prio FROM facade_dsv_children
  UNION ALL 
  SELECT child_id, parent_id, NULL AS grandpa_id, 'group' AS parent_type, 2 AS name_prio FROM group_dsv_children
  UNION ALL 
  SELECT child_id, NULL AS parent_id, NULL AS grandpa_id, 'none' AS parent_type, 1 AS name_prio FROM root_dsv
),

/*
Weist innerhalb aller publizierten Datasetviews den Duplikaten einen Suffix von 2 bis * zu.
Genau einer Kopie jedes publizierten Singleactor bleibt der Original-Identifier erhalten (suffix = null).
*/
dsv_unique_suffix AS (
  SELECT 
    child_id,
    parent_id,
    parent_type,
    NULLIF(ROW_NUMBER() OVER (PARTITION BY s.child_id ORDER BY s.name_prio ASC), 1) AS suffix,
    grandpa_id
  FROM 
    dsv_treenodes s
),

-- CTE für Json-Erzeugung ...

productlist_children AS ( -- Alle publizierten Kinder einer Productlist, sortiert nach pil.sort
  SELECT  
    pil.product_list_id, 
    jsonb_agg(
      concat_ws('.', identifier, suffix) ORDER BY pil.sort
    ) AS ident_json
  FROM 
    simi.simiproduct_properties_in_list pil 
  JOIN 
    simi.trafo_published_dp_v dp ON pil.single_actor_id = dp.dp_id
  LEFT JOIN 
    dsv_unique_suffix u ON pil.product_list_id = u.parent_id AND pil.single_actor_id = u.child_id
  GROUP BY 
    product_list_id  
),

productlist AS ( -- Alle publizierten Productlists, mit ihren publizierten Kindern. (Background-)Map.print_or_ext = TRUE, Layergroup.print_or_ext = FALSE 
  SELECT 
    identifier,
    print_or_ext, 
    jsonb_build_object(
      'name', identifier,
      'type', 'productset',
      'title', title_ident,
      'sublayers', ident_json
    ) AS layer_json
  FROM 
    simi.trafo_published_dp_v dp
  JOIN
    productlist_children sa ON dp.dp_id = sa.product_list_id
  LEFT JOIN 
    simi.simiproduct_map m ON dp.dp_id = m.id
),

facade_placements AS ( -- Facadelayer innerhalb Gruppe oder auf root-ebene platziert
  SELECT 
    parent_id AS fl_id,
    grandpa_id AS lg_id
  FROM 
    dsv_unique_suffix
  WHERE 
    parent_type = 'facade'
  GROUP BY 
    parent_id, 
    grandpa_id
),

null_uid_placeholder AS ( -- Künstliche eindeutige uid. Wird für Root-Facadelayer als Ersatz von NULL als grandpa_id verwendet, damit homogenes query geschrieben werden kann.
  SELECT   
    uuid_generate_v4() AS null_uid
),

facade_placements_suffix AS (
  SELECT 
    fl_id,
    NULLIF(ROW_NUMBER() OVER (PARTITION BY fl_id ORDER BY lg_id ASC), 1) AS fl_suffix,
    COALESCE(lg_id, null_uid) AS lg_id_nullreplaced
  FROM
    facade_placements,
    null_uid_placeholder
),    

facade_placements_childarr AS ( -- Alle direkt oder indirekt publizierten Kinder eines Facadelayer, sortiert nach pif.sort
  SELECT 
    fl_id,
    fl_suffix,
    lg_id_nullreplaced,
    jsonb_agg(
      concat_ws('.', identifier, suffix) ORDER BY pif.sort
    ) AS ident_json
  FROM 
    facade_placements_suffix fp
  JOIN
    simi.simiproduct_properties_in_facade pif ON fp.fl_id = pif.facade_layer_id
  JOIN 
    simi.trafo_published_dp_v dp ON pif.data_set_view_id = dp.dp_id
  CROSS JOIN
    null_uid_placeholder nup
  JOIN 
    dsv_unique_suffix u ON fp.fl_id = u.parent_id AND pif.data_set_view_id = u.child_id AND fp.lg_id_nullreplaced = COALESCE(u.grandpa_id, nup.null_uid)    
  GROUP BY 
    fl_id,
    lg_id_nullreplaced,
    fl_suffix
),

facadelayer_placed AS ( -- fl mit referenz auf die enthaltende layergruppe (Attribut lg_id_nullreplaced). Facadelayer kann mehrfach vorkommen
  SELECT 
    concat_ws('.', identifier, fl_suffix) AS identifier,
    print_or_ext,
    jsonb_build_object(
      'name', concat_ws('.', identifier, fl_suffix),
      'type', 'productset',
      'title', title_ident,
      'sublayers', ident_json
    ) AS layer_json,
    lg_id_nullreplaced
  FROM 
    simi.simiproduct_facade_layer fl
  JOIN 
    simi.trafo_published_dp_v dp ON fl.id = dp.dp_id
  JOIN
    facade_placements_childarr dsv ON dp.dp_id = dsv.fl_id
),

dsv_qml_assetfile AS (
  SELECT 
    dataset_set_view_id AS dsv_id,
    jsonb_build_object(
      'path', file_name,
      'base64', encode(file_content, 'base64')
    ) AS file_json
  FROM
    simi.simidata_styleasset     
  WHERE 
    is_for_server IS TRUE  
),

dsv_qml_assetfiles AS (
  SELECT 
    dsv_id,
    jsonb_agg(file_json) AS assetfiles_json
  FROM 
    dsv_qml_assetfile
  GROUP BY 
    dsv_id
),

vector_layer AS (
  SELECT 
    concat_ws('.', identifier, suffix) AS identifier,
    FALSE AS print_or_ext,
    jsonb_strip_nulls(
      jsonb_build_object(
      'name', concat_ws('.', identifier, suffix),
      'type', 'layer',
      'datatype', 'vector',
      'title', title_ident,
      'postgis_datasource', tbl_json,
      'qml_base64', encode(convert_to(style_server, 'UTF8'), 'base64'),
      'qml_assets', COALESCE(assetfiles_json, jsonb_build_array()),
      'attributes', attr_name_js
      )
    ) AS layer_json
  FROM
    simi.trafo_wms_tableview_v tv
  JOIN
    simi.simidata_data_set_view dsv ON tv.tv_id = dsv.id
  JOIN 
    simi.trafo_wms_geotable_v tbl ON tv.tv_id = tbl.tv_id 
  JOIN
    dsv_unique_suffix s ON tv.tv_id = s.child_id
  LEFT JOIN 
    dsv_qml_assetfiles files ON tv.tv_id = files.dsv_id
),

raster_layer AS (
  SELECT 
    concat_ws('.', identifier, suffix) AS identifier,
    print_or_ext,
    jsonb_strip_nulls(jsonb_build_object(
      'name', concat_ws('.', identifier, suffix),
      'type', 'layer',
      'datatype', 'raster',
      'title', title_ident,
      'qml_base64', encode(convert_to(style_server, 'UTF8'), 'base64'),
      'raster_datasource', jsonb_build_object('datasource', concat_ws('/', '/geodata/geodata', "path"), 'srid', 2056) 
    )) AS layer_json
  FROM
    simi.trafo_published_dp_v dp
  JOIN 
    simi.simidata_data_set_view dsv ON dp.dp_id = dsv.id
  JOIN 
    simi.simidata_raster_view rv ON dsv.id = rv.id
  JOIN 
    simi.simidata_raster_ds rds ON rv.raster_ds_id = rds.id   
  JOIN
    dsv_unique_suffix s ON dp.dp_id = s.child_id
),

ext_wms_layerbase AS (
  SELECT  
    identifier,
    title_ident,  
    jsonb_build_object(
      'wms_url', concat(url, '/'), -- TRAILING slash für qgis server notwendig
      'layers', el.ext_identifier,
      'format', 'image/png',
      'srid', 2056,
      'styles', '',
      'featureCount', 300
    ) AS wms_datasource_json
  FROM
    simi.trafo_published_dp_v dp
  JOIN 
    simi.simiproduct_external_wms_layers el ON dp.dp_id = el.id
  JOIN
    simi.simiproduct_external_wms_service es ON el.service_id = es.id
),

ext_wms AS (
  SELECT 
    identifier,
    TRUE AS print_or_ext,
    jsonb_build_object(
      'name', identifier,
      'type', 'layer',
      'datatype', 'wms',
      'title', title_ident,
      'wms_datasource', wms_datasource_json
    ) AS layer_json
  FROM
    ext_wms_layerbase
),

layer_union AS (
  SELECT identifier, print_or_ext, layer_json FROM productlist
  UNION ALL 
  SELECT identifier, print_or_ext, layer_json FROM facadelayer_placed
  UNION ALL 
  SELECT identifier, print_or_ext, layer_json FROM vector_layer
  UNION ALL 
  SELECT identifier, print_or_ext, layer_json FROM raster_layer
  UNION ALL 
  SELECT identifier, print_or_ext, layer_json FROM ext_wms
)

SELECT 
  identifier,
  print_or_ext, 
  layer_json 
FROM
  layer_union
;

