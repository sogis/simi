--CREATE VIEW simi.trafo_wms_layer_v AS

/*
 * Gibt für den WMS die Dataproducts (DP) mit ihren jeweiligen Detailinformationen aus.
 * 
 * Mittels Flag "print_or_ext" wird unterschieden, ob das DP nur im "Print-WMS" erscheint. 
 */
WITH

-- CTE für WMS-weit eindeutige singleactor identifier

productlist_children AS ( -- Alle publizierten pl aka gruppen und maps mit ihren publizierten kind-sa
  SELECT 
    product_list_id AS pl_id,
    single_actor_id AS sa_id,
    (lg.id IS NOT NULL) AS list_is_group,
    pil.sort
  FROM  
    simi.simiproduct_properties_in_list pil
  JOIN
    simi.trafo_published_dp_v plg ON pil.product_list_id = plg.dp_id
  JOIN 
    simi.trafo_published_dp_v psa ON pil.single_actor_id = psa.dp_id
  LEFT JOIN  
    simi.simiproduct_layer_group lg ON pil.product_list_id = lg.id
)

,layergroup_children AS (
  SELECT 
    pl_id AS lg_id,
    sa_id
  FROM 
    productlist_children
  WHERE 
    list_is_group IS TRUE
)

-- Alle publizierten fl mit ihren publizierten kind-dsv. 
-- lg_id != NULL, falls die fl teil einer lg ist. Entsprechend kann eine fl mehrfach vorkommen
,facade_children_lg AS ( 
  SELECT 
    facade_layer_id AS fl_id,
    data_set_view_id AS dsv_id, 
    lg.lg_id
  FROM 
    simi.simiproduct_properties_in_facade pif
  JOIN
    simi.trafo_published_dp_v pfl ON pif.facade_layer_id = pfl.dp_id
  JOIN
    simi.trafo_published_dp_v pdsv ON pif.data_set_view_id = pdsv.dp_id
  LEFT JOIN 
    layergroup_children lg ON pif.facade_layer_id = lg.sa_id
)

,root_sa AS ( -- Alle Top-LEVEL (= Root) publizierten singleactor
  SELECT 
    sa.id AS sa_id
  FROM
    simi.simiproduct_single_actor sa
  JOIN
    simi.trafo_published_dp_v p ON sa.id = p.dp_id 
  WHERE
    root_published IS TRUE        
)

-- Suffix-Prioritäten:
-- 1. Root publizierte Singleactors parent=null
-- 2. Singleactors in Layergruppen parent.type = layergroup
-- 3. Datasetviews in Facadelayern parent.type = facadelayer
,sa_treenodes AS (
  SELECT dsv_id AS child_id, fl_id AS parent_id, lg_id AS grandpa_id, 'facade' AS parent_type, 3 AS name_prio FROM facade_children_lg
  UNION ALL 
  SELECT sa_id AS child_id, lg_id AS parent_id, NULL AS grandpa_id, 'group' AS parent_type, 2 AS name_prio FROM layergroup_children
  UNION ALL 
  SELECT sa_id AS child_id, NULL AS parent_id, NULL AS grandpa_id, 'none' AS parent_type, 1 AS name_prio FROM root_sa
)

-- Weist innerhalb aller publizierten Datasetviews den Duplikaten einen Suffix von 2 bis * zu.
-- Genau einer Kopie jedes publizierten Singleactor bleibt der Original-Identifier erhalten (suffix = null).
,sa_unique_suffix AS (
  SELECT 
    child_id,
    parent_id,
    grandpa_id,
    NULLIF(ROW_NUMBER() OVER (PARTITION BY s.child_id ORDER BY s.name_prio, parent_id::text, grandpa_id::text ASC), 1) AS suffix     
  FROM 
    sa_treenodes s
)

-- CTE für Json-Erzeugung ...

,productlist_children_json AS ( -- Json-ARRAY der singleactor einer productlist
  SELECT  
    pc.pl_id, 
    jsonb_agg(
      concat_ws('.', derived_identifier, suffix) ORDER BY pc.sort
    ) AS ident_json
  FROM 
    productlist_children pc
  JOIN 
    simi.simi.simiproduct_data_product dp ON pc.sa_id = dp.id
  LEFT JOIN 
    sa_unique_suffix u ON pc.pl_id = u.parent_id AND pc.sa_id = u.child_id
  GROUP BY 
    pl_id  
)

,productlist AS ( -- Alle publizierten Productlists, mit ihren publizierten Kindern. (Background-)Map.print_or_ext = TRUE, Layergroup.print_or_ext = FALSE 
  SELECT 
    dp.identifier,
    dp.print_or_ext, 
    jsonb_build_object(
      'name', dp.identifier,
      'type', 'productset',
      'title', dp.title_ident,
      'sublayers', sa.ident_json
    ) AS layer_json
  FROM 
    simi.trafo_published_dp_v dp
  JOIN
    productlist_children_json sa ON dp.dp_id = sa.pl_id
)


,null_uid_placeholder AS ( -- Künstliche eindeutige uid. Wird für root publizierte SingleActor als Ersatz der layergruppen-id verwendet (macht where clause einfacher).
  SELECT   
    gen_random_uuid() AS null_uid
)

-- FL im Layer-Tree. Mit Referenz auf den Parent oder die null_uid für root FL. 
-- Ein root publizierter und in einer LG enthaltener FL wird nur als "LG-Kind" zurückgegeben
,facade_node AS (
  SELECT    
    fl.id AS fl_id,
    coalesce(lgc.lg_id, nid.null_uid) AS lg_or_null_id
  FROM 
    simi.simiproduct_facade_layer fl 
  CROSS JOIN 
    null_uid_placeholder nid
  LEFT JOIN
    layergroup_children lgc ON fl.id = lgc.sa_id
)

,facade_node_ident AS ( -- facade_node mit layer-ident samt suffix
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

,facade_node_child_ident AS (-- Kind-DSV von Facade-Knoten im Layerbaum. Werden mit baumweit eindeutigen identifiern versehen
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

,facade_node_child_arr AS ( -- FL im Tree, mit Kindern als Json-Array
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

,facadelayer AS ( -- FL im Tree, kompletter Informationsgehalt
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
    facade_node_ident fn
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