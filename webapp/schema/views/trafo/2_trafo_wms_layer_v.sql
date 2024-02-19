CREATE VIEW simi.trafo_wms_layer_v AS

/*
 * Gibt für den WMS die Dataproducts (DP) mit ihren jeweiligen Detailinformationen aus.
 * 
 * Mittels Flag "print_or_ext" wird unterschieden, ob das DP nur im "Print-WMS" erscheint. 
 */
WITH

-- CTE für WMS-weit eindeutige singleactor identifier

published_sa AS ( -- Alle publizierten SingleActors  
  SELECT 
    sa.id AS sa_id,
    p.root_published
  FROM
    simi.simiproduct_single_actor sa
  JOIN
    simi.trafo_published_dp_v p ON sa.id = p.dp_id 
)

,layergroup_children AS ( 
  SELECT 
    product_list_id AS lg_id,
    single_actor_id AS sa_id
  FROM 
    simi.simiproduct_layer_group lg
  JOIN 
    simi.simiproduct_properties_in_list pil ON lg.id = pil.product_list_id
)

,facade_dsv_children AS (
  SELECT 
    data_set_view_id AS child_id, 
    facade_layer_id AS parent_id,
    lg_id AS grandpa_id
  FROM 
    simi.simiproduct_properties_in_facade pif
  JOIN
    published_sa p ON pif.data_set_view_id = p.sa_id
  LEFT JOIN 
    layergroup_children lg ON pif.facade_layer_id = lg.sa_id
)

,group_sa_children AS (
  SELECT 
    product_list_id AS parent_id, 
    single_actor_id AS child_id
  FROM 
    simi.simiproduct_properties_in_list pil
  JOIN 
    simi.simiproduct_layer_group lg ON pil.product_list_id = lg.id 
  JOIN
    published_sa p ON pil.single_actor_id = p.sa_id   
)

,root_sa AS (
  SELECT 
    sa_id AS child_id
  FROM 
    published_sa
  WHERE
    root_published IS TRUE            
)

/*
Suffix-Prioritäten:
1. Root publizierte Singleactors parent=null
2. Singleactors in Layergruppen parent.type = layergroup
3. Datasetviews in Facadelayern parent.type = facadelayer
*/
,sa_treenodes AS (
  SELECT child_id, parent_id, grandpa_id, 'facade' AS parent_type, 3 AS name_prio FROM facade_dsv_children
  UNION ALL 
  SELECT child_id, parent_id, NULL AS grandpa_id, 'group' AS parent_type, 2 AS name_prio FROM group_sa_children
  UNION ALL 
  SELECT child_id, NULL AS parent_id, NULL AS grandpa_id, 'none' AS parent_type, 1 AS name_prio FROM root_sa
)


/*
Weist innerhalb aller publizierten Datasetviews den Duplikaten einen Suffix von 2 bis * zu.
Genau einer Kopie jedes publizierten Singleactor bleibt der Original-Identifier erhalten (suffix = null).
*/
,sa_unique_suffix AS (
  SELECT 
    child_id,
    parent_id,
    grandpa_id,
    NULLIF(ROW_NUMBER() OVER (PARTITION BY s.child_id ORDER BY s.name_prio ASC), 1) AS suffix     
  FROM 
    sa_treenodes s
)

-- CTE für Json-Erzeugung ...

,productlist_children AS ( -- Alle publizierten Kinder einer Productlist, sortiert nach pil.sort
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
    sa_unique_suffix u ON pil.product_list_id = u.parent_id AND pil.single_actor_id = u.child_id
  GROUP BY 
    product_list_id  
)

,productlist AS ( -- Alle publizierten Productlists, mit ihren publizierten Kindern. (Background-)Map.print_or_ext = TRUE, Layergroup.print_or_ext = FALSE 
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
)

,null_uid_placeholder AS ( -- Künstliche eindeutige uid. Wird für root publizierte SingleActor als Ersatz der layergruppen-id verwendet (macht where clause einfacher).
  SELECT   
    gen_random_uuid() AS null_uid
)

,lg_childlinks AS (
  SELECT 
    l.id AS lg_id,
    p.single_actor_id AS sa_id
  FROM 
    simi.simiproduct_layer_group l
  JOIN
    simi.simiproduct_properties_in_list p ON l.id = p.product_list_id
)

-- FL im Layer-Tree. Mit Referenz auf den Parent oder die null_uid für root FL. 
-- Ein root publizierter und LG enthaltener FL wird nur als "LG-Kind" zurückgegeben
,facade_node AS (
  SELECT    
    f.id AS fl_id,
    coalesce(lc.lg_id, null_uid) AS lg_or_null_id
  FROM 
    simi.simiproduct_facade_layer f 
  CROSS JOIN 
    null_uid_placeholder nid
  LEFT JOIN
    lg_childlinks lc ON f.id = lc.sa_id
)

,facade_node_2 AS (
  SELECT 
    concat_ws('.', dp.derived_identifier, suffix) AS fl_identifier,
    fl_id,
    lg_or_null_id
  FROM 
    facade_node fn
  CROSS JOIN
    null_uid_placeholder nid
  JOIN
    sa_unique_suffix s ON fn.fl_id = s.child_id AND fn.lg_or_null_id = COALESCE(s.parent_id, null_uid)
  JOIN
    simi.simiproduct_data_product dp ON fn.fl_id = dp.id
)

,facade_node_child_ident AS (--  Kinder von Facade-Knoten im Layerbaum. Werden mit baumweit eindeutigen identifiern versehen
  SELECT 
    concat_ws('.', dp.derived_identifier, suffix) AS dsv_identifier,
    fn.fl_id,
    fn.lg_or_null_id,
    pif.sort
  FROM 
    sa_unique_suffix s
  CROSS JOIN 
    null_uid_placeholder nid
  JOIN
    facade_node fn ON s.parent_id = fn.fl_id AND COALESCE(s.grandpa_id, nid.null_uid) = fn.lg_or_null_id
  JOIN 
    simi.simiproduct_properties_in_facade pif ON s.child_id = pif.data_set_view_id AND s.parent_id = pif.facade_layer_id
  JOIN
    simi.simiproduct_data_product dp ON s.child_id = dp.id
)

,facade_node_child_arr AS (
  SELECT 
    jsonb_agg(
      dsv_identifier ORDER BY sort
    ) AS ident_json,
    fl_id,
    lg_or_null_id
  FROM 
    facade_node_child_ident
  GROUP BY 
    fl_id,
    lg_or_null_id
)

,facadelayer AS (
  SELECT 
    fl_identifier AS identifier,
    print_or_ext,
    jsonb_build_object(
      'name', fl_identifier,
      'type', 'productset',
      'title', title_ident,
      'sublayers', ident_json
    ) AS layer_json
  FROM 
    facade_node_2 fn
  JOIN 
    simi.trafo_published_dp_v dp ON fn.fl_id = dp.dp_id
  JOIN
    facade_node_child_arr fc ON fn.fl_id = fc.fl_id AND fn.lg_or_null_id = fc.lg_or_null_id
)

,dsv_qml_assetfile AS (
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
)

,dsv_qml_assetfiles AS (
  SELECT 
    dsv_id,
    jsonb_agg(file_json) AS assetfiles_json
  FROM 
    dsv_qml_assetfile
  GROUP BY 
    dsv_id
)

,vector_layer AS (
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
    sa_unique_suffix s ON tv.tv_id = s.child_id
  LEFT JOIN 
    dsv_qml_assetfiles files ON tv.tv_id = files.dsv_id
)

,raster_layer AS (
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
    sa_unique_suffix s ON dp.dp_id = s.child_id
)

,ext_wms_layerbase AS (
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
)

,ext_wms AS (
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
)

,layer_union AS (
  SELECT identifier, print_or_ext, layer_json FROM productlist
  UNION ALL 
  SELECT identifier, print_or_ext, layer_json FROM facadelayer
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