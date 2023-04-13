/*
 * Basis-View aller in SIMI dokumentierten Abhängigkeiten gegliedert nach level.
 * Level muss man sich vorstellen wie Geschosse in einem Gebäude:
 * UG2: DB-Schema (nur als upstream_id enthalten, da nichts angezeigt wird=
 * UG1: DB-Tabellen
 * EG: Datasetview Level
 * OG1: Alle direkten Abhängigkeiten von Datasetviews (Facadelayer, Module, Spez. Featinfo, ...)
 * OG2: Layergruppen und Karten, welche Facadelayer enthalten
 * */
CREATE VIEW simi.app_dependencies_v AS 

WITH 

-- common ---------------------------------------------------------------------------------

dtype_map AS (
  SELECT
    *
  FROM (
    VALUES
      ('simiExtended_Component', 3, 'Modul'),
      ('simiExtended_FeatureInfo', 3, 'Spez. Featureinfo'),
      ('simiExtended_CCCIntegration', 3, 'CCC-Integration'),
      ('simiExtended_Report', 3, 'Report'),      
      ('simiProduct_Map', 4, 'Karte'),
      ('simiProduct_ExternalWmsLayers', 2, 'Ext. WMS'),
      ('simiProduct_FacadeLayer', 3, 'Fassade'),
      ('simiProduct_LayerGroup', 4, 'Gruppe'),      
      ('simiData_RasterView', 2, 'View (Raster)'),
      ('simiData_TableView', 2, 'View (Vector)')
  )
  AS t (dtype, level_num, type_display)    
),

product_common AS (
    SELECT
        concat_ws(' | ', derived_identifier, title) as obj,
        level_num,
        COALESCE(type_display, 'ERROR: dtype not in map')  as typ,
        id
    FROM
        simi.simiproduct_data_product p
    LEFT JOIN
        dtype_map m ON p.dtype  = m.dtype
),


-- table level objects -----------------------------------------------------------------------

tablelevel_obj AS (
    SELECT
        concat_ws('.', s.schema_name, t.table_name) AS obj,
        1 AS level_num,
        'Tabelle' AS typ,
        t.id as id,
        s.id as upstream_id
    FROM
        simi.simidata_postgres_table t
    JOIN
        simi.simidata_db_schema s ON t.db_schema_id = s.id
),

-- dsvlevel objects. Contains also ext wms as in same level ---------------------------

dsvlevel_base AS (
    SELECT 
        p.*,
        v.postgres_table_id AS upstream_id,
        v.search_type
    FROM
        product_common p
    JOIN
        simi.simiproduct_single_actor s ON p.id = s.id --ONLY single actors
    LEFT JOIN 
        simi.simiproduct_facade_layer f ON p.id = f.id
    LEFT JOIN
        simi.simidata_table_view v ON p.id = v.id
    WHERE 
        f.id IS NULL -- EXCLUDE facade layer        
),

dsvlevel_search AS (
    SELECT     
        level_num,
        obj,
        'View (Suchkonfiguration)' AS typ,
        id,
        upstream_id
    FROM 
        dsvlevel_base b
    WHERE 
        b.search_type NOT LIKE '1%' -- match auf "1_no_search"
),

dsvlevel_obj AS (
    SELECT obj, typ, level_num, id, upstream_id FROM dsvlevel_base
    UNION ALL 
    SELECT obj, typ, level_num, id, upstream_id FROM dsvlevel_search
),

-- dsv plus 1 level objects. All obj that depend directly on dsv level: facade, group, report, module, ccc, ...

dsvlevel_p1_facade AS (
    SELECT 
        p.*,
        l.data_set_view_id AS upstream_id
    FROM
        product_common p
    JOIN
        simi.simiproduct_properties_in_facade l ON p.id = l.facade_layer_id 
),

dsvlevel_p1_prodlist AS (
    SELECT 
        p.*,
        l.single_actor_id  AS upstream_id
    FROM
        product_common p
    JOIN
        simi.simiproduct_properties_in_list l ON p.id = l.product_list_id 
    LEFT JOIN 
        simi.simiproduct_facade_layer f ON l.single_actor_id = f.id
    WHERE 
        f.id IS NULL -- EXCLUDE facade layer
),

dsvlevel_p1_dependencies AS (
    SELECT
        level_num,
        d."name" AS obj,
        COALESCE(type_display, 'ERROR: dtype not in map')  as typ,
        d.id,
        r.data_set_view_id AS upstream_id
    FROM
        simi.simiextended_dependency d
    JOIN
        simi.simiextended_relation r ON d.id = r.dependency_id
    LEFT JOIN
        dtype_map m ON d.dtype  = m.dtype
    WHERE 
        r.relation_type != '1_display'
),

dsvlevel_p1_obj AS (
    SELECT obj, typ, level_num, id, upstream_id  FROM dsvlevel_p1_facade
    UNION ALL   
    SELECT obj, typ, level_num, id, upstream_id FROM dsvlevel_p1_prodlist
    UNION ALL
    SELECT obj, typ, level_num, id, upstream_id FROM dsvlevel_p1_dependencies
),    

-- map level objects. All obj that depend directly on facade layer -------------------------

maplevel_obj AS (
    SELECT 
        p.*,
        l.single_actor_id  AS upstream_id
    FROM
        product_common p
    JOIN
        simi.simiproduct_properties_in_list l ON p.id = l.product_list_id 
    JOIN 
        simi.simiproduct_facade_layer f ON l.single_actor_id = f.id
),

-- union all -----------------------------------------------------------------------

unionall AS (
    SELECT obj, typ, level_num, id, upstream_id, obj AS qual_table_name FROM tablelevel_obj
    UNION ALL
    SELECT obj, typ, level_num, id, upstream_id, NULL AS qual_table_name FROM dsvlevel_obj
    UNION ALL
    SELECT obj, typ, level_num, id, upstream_id, NULL AS qual_table_name FROM dsvlevel_p1_obj
    UNION ALL
    SELECT obj, typ, level_num, id, upstream_id, NULL AS qual_table_name FROM maplevel_obj
)

SELECT
    obj AS obj_name, 
    typ AS type_name, 
    level_num, 
    id AS obj_id, 
    upstream_id,
    qual_table_name,
    ROW_NUMBER() OVER() AS id
FROM 
    unionall
;




