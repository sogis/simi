/*
 * Basis-View aller in SIMI dokumentierten Abhängigkeiten gegliedert nach level.
 * Level muss man sich vorstellen wie Geschosse in einem Gebäude:
 * UG2: DB-Schema (nur als upstream_id enthalten, da nichts angezeigt wird=
 * UG1: DB-Tabellen
 * EG: Datasetview Level
 * OG1: Alle direkten Abhängigkeiten von Datasetviews (Facadelayer, Module, Spez. Featinfo, ...)
 * OG2: Layergruppen und Karten, welche Facadelayer enthalten
 * */
--CREATE VIEW 0_app_dependencies_v AS 

WITH 

-- common ---------------------------------------------------------------------------------

dtype_map AS (
  SELECT
    *
  FROM (
    VALUES
      ('simiExtended_Component', 'module', 'Modul'),
      ('simiExtended_FeatureInfo', 'feat_info', 'Spez. Featureinfo'),
      ('simiExtended_CCCIntegration', 'ccc', 'CCC-Integration'),
      ('simiExtended_Report', 'report', 'Report'),      
      ('simiProduct_Map', 'map', 'Karte'),
      ('simiProduct_ExternalWmsLayers', 'dsv_ext', 'Ext. WMS'),
      ('simiProduct_FacadeLayer', 'fassade', 'Fassade'),
      ('simiProduct_LayerGroup', 'group', 'Gruppe'),      
      ('simiData_RasterView', 'dsv_ras', 'View (Raster)'),
      ('simiData_TableView', 'dsv_vec', 'View (Vector)')
  )
  AS t (dtype, type_ident, type_display)    
),

product_common AS (
    SELECT
        derived_identifier AS ident,
        concat_ws(' | ', derived_identifier, title) as display,
        COALESCE(type_ident, 'ERROR: dtype not in map')  as type_ident,
        COALESCE(type_display, 'ERROR: dtype not in map')  as type_display,
        id
    FROM
        simiproduct_data_product p
    LEFT JOIN
        dtype_map m ON p.dtype  = m.dtype
),

-- table level objects -----------------------------------------------------------------------

tablelevel_obj AS (
    SELECT
        concat_ws('.', s.schema_name, t.table_name) AS ident,
        concat_ws(' | ', table_name, title) as display,
        'table' as type_ident,
        'Tabelle' AS type_display,
        t.id as id,
        s.id as upstream_id
    FROM
        simidata_postgres_table t
    JOIN
        simidata_db_schema s ON t.db_schema_id = s.id
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
        simiproduct_single_actor s ON p.id = s.id --ONLY single actors
    LEFT JOIN 
        simiproduct_facade_layer f ON p.id = f.id
    LEFT JOIN
        simidata_table_view v ON p.id = v.id
    WHERE 
        f.id IS NULL -- EXCLUDE facade layer        
),

dsvlevel_search AS (
    SELECT     
        ident,
        display,
        'dsv_search' as type_ident,
        'View (Suchkonfiguration)' AS type_display,
        id,
        upstream_id
    FROM 
        dsvlevel_base b
    WHERE 
        b.search_type NOT LIKE '1%' -- match auf "1_no_search"
),

dsvlevel_obj AS (
    SELECT display, type_ident, type_display, id, upstream_id FROM dsvlevel_search
),

-- dsv plus 1 level objects. All obj that depend directly on dsv level: facade, group, report, module, ccc, ...

dsvlevel_p1_facade AS (
    SELECT 
        p.*,
        l.data_set_view_id AS upstream_id
    FROM
        product_common p
    JOIN
        simiproduct_properties_in_facade l ON p.id = l.facade_layer_id 
),

dsvlevel_p1_prodlist AS (
    SELECT 
        p.*,
        l.single_actor_id  AS upstream_id
    FROM
        product_common p
    JOIN
        simiproduct_properties_in_list l ON p.id = l.product_list_id 
    LEFT JOIN 
        simiproduct_facade_layer f ON l.single_actor_id = f.id
    WHERE 
        f.id IS NULL -- EXCLUDE facade layer
),

dsvlevel_p1_dependencies AS (
    SELECT
        d."name" AS ident,
        d."name" AS display,
        COALESCE(type_ident, 'ERROR: dtype not in map')  as type_ident,
        COALESCE(type_display, 'ERROR: dtype not in map')  as type_display,
        d.id,
        r.data_set_view_id AS upstream_id
    FROM
        simiextended_dependency d
    JOIN
        simiextended_relation r ON d.id = r.dependency_id
    LEFT JOIN
        dtype_map m ON d.dtype  = m.dtype
),

dsvlevel_p1_obj AS (
    SELECT display, type_ident, type_display, id, upstream_id FROM dsvlevel_p1_facade
    UNION ALL   
    SELECT display, type_ident, type_display, id, upstream_id FROM dsvlevel_p1_prodlist
    UNION ALL
    SELECT display, type_ident, type_display, id, upstream_id FROM dsvlevel_p1_dependencies
),    

-- map level objects. All obj that depend directly on facade layer -------------------------

maplevel_obj AS (
    SELECT 
        p.*,
        l.single_actor_id  AS upstream_id
    FROM
        product_common p
    JOIN
        simiproduct_properties_in_list l ON p.id = l.product_list_id 
    JOIN 
        simiproduct_facade_layer f ON l.single_actor_id = f.id
),

-- union all -----------------------------------------------------------------------

unionall AS (
    SELECT display, type_ident, type_display, id, upstream_id FROM tablelevel_obj
    UNION ALL
    SELECT display, type_ident, type_display, id, upstream_id FROM dsvlevel_obj
    UNION ALL
    SELECT display, type_ident, type_display, id, upstream_id FROM dsvlevel_p1_obj
    UNION ALL
    SELECT display, type_ident, type_display, id, upstream_id FROM maplevel_obj
)

SELECT * FROM unionall

-- bis 16:15



