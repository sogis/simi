/*
 * CTE's sammeln die Informationen zu den Abhängigkeiten auf ein Dataset oder auf eine DataSetView 
 * des Dataset. Indirekte Abhängigkeiten werden nicht ausgegeben. Beispiel: 
 * Von der Kette DSV --> Fassade --> Gruppe wird nur die direkte Beziehung DSV --> Fassade ausgegeben
 */

WITH

-- Shared ********************************************************************

ds_dsv AS ( -- datasetviews für ein dataset
	SELECT 
		COALESCE(rv.raster_ds_id, tv.postgres_table_id) AS ds_id,
		dsv.id AS dsv_id,
		'View' AS typ,
		1 AS sort
	FROM 
		simiproduct_data_set_view dsv
	LEFT JOIN 
		simidata_raster_view rv ON dsv.id = rv.id
	LEFT JOIN 
		simidata_table_view tv ON dsv.id = tv.id
),

-- DATENPRODUKTE ********************************************************************

ds_fl AS ( -- facadelayer für eine datasetview
	SELECT 
		ds_id,
		dsv_id,
		pif.facade_layer_id AS fl_id,
		'Fassade' AS typ,
		2 AS sort
	FROM 
		ds_dsv
	JOIN 
		simiproduct_properties_in_facade pif ON dsv_id = pif.data_set_view_id 
	WHERE 
		pif.delete_ts IS NULL 
),

ds_pl AS ( -- productlist für eine datasetview. Productlist = Karte oder Layergruppe
	SELECT 
		ds_id,
		dsv_id,
		pil.product_list_id AS pl_id,
		--(_map.id IS NOT NULL) AS is_map,
		CASE
			WHEN _map.id IS NULL THEN 'Gruppe'
			ELSE 'Karte'
		END AS typ,
		CASE
			WHEN _map.id IS NULL THEN 3
			ELSE 4
		END AS sort
	FROM 
		ds_dsv
	JOIN 
		simiproduct_properties_in_list pil ON dsv_id = pil.single_actor_id 
	LEFT JOIN 
		simiproduct_map _map ON pil.product_list_id = _map.id 
	WHERE 
		pil.delete_ts IS NULL 
),

dp_union AS (
	SELECT ds_id, dsv_id, dsv_id AS dp_id, typ, sort FROM ds_dsv
	UNION ALL
	SELECT ds_id, dsv_id, fl_id AS dp_id, typ, sort FROM ds_fl
	UNION ALL
	SELECT ds_id, dsv_id, pl_id AS dp_id, typ, sort FROM ds_pl
),

dataprod_cols AS ( -- notwendige informationen aller ungelöschten Datenprodukte
	SELECT 
		id,
		title,
		identifier
	FROM 
		simiproduct_data_product dp
	WHERE
		dp.delete_ts IS NULL
),

dataprod AS (
	SELECT
		ds_id,
		concat(dp_union.typ, ' (Produkt)') AS dep_typ,
		concat(dp.identifier, ' | ', dp.title) AS dep_name,
		CASE 
			WHEN dsv_id = dp_id THEN 'Ist View des Dataset'
			ELSE concat('Dataset ist via View "', dsv.identifier, '" in ', dp_union.typ, ' enthalten.')
		END AS dep_relation,
		sort AS subsort,
		1 AS sort
	FROM 
		dp_union
	JOIN 
		dataprod_cols dp ON dp_union.dp_id = dp.id
	JOIN 
		dataprod_cols dsv ON dp_union.dsv_id = dsv.id
),

-- Dependencies ********************************************************************

ds_dsv_relation AS ( -- Alle Beziehungen eines DS via DSV auf eine Dependency
	SELECT 
		ds_id,
		dsv_id,
		dp.identifier AS dsv_identifier,
		rel.dependency_id AS dep_id
	FROM 
		ds_dsv
	JOIN 
		simidependency_relation rel ON ds_dsv.dsv_id = rel.data_set_view_id
	JOIN 
		simiproduct_data_product dp ON ds_dsv.dsv_id = dp.id 
	WHERE 
			rel.delete_ts IS NULL 
		AND 
			dp.delete_ts IS NULL
		AND 
			rel.relation_type NOT LIKE '%display%' -- "Verzeihender" Filter, falls die Nachführung vergessen geht...
),

dependency_extprop AS (
	SELECT 
		* 
	FROM (
		VALUES 
			('simiDependency_Component', 'Modul', 1),
			('simiDependency_FeatureInfo', 'Spez. Featureinfo', 3),
			('simiDependency_CCCIntegration', 'CCC-Integration', 6),
			('simiDependency_Report', 'Report', 9)
	) 
	AS t (dtype, typename, sort)
),

dependency AS (
	SELECT 
		ds_id,
		"name" AS dep_name,
		concat(typename, ' (Abhängigkeit)') AS dep_typ,
		concat('Dataset ist via View "', rel.dsv_identifier, '" in ', typename, ' enthalten.') AS dep_relation,
		sort AS subsort,
		2 AS sort
	FROM 
		simidependency_dependency dep
	JOIN
		dependency_extprop ep ON dep.dtype = ep.dtype
	JOIN 
		ds_dsv_relation rel ON dep.id = rel.dep_id
	WHERE 
		dep.delete_ts IS NULL 
),

-- ***********************************************************************************

all_dependencies AS (
	SELECT * FROM (
		SELECT ds_id, dep_typ, dep_name, dep_relation, subsort, sort FROM dataprod
		UNION ALL 
		SELECT ds_id, dep_typ, dep_name, dep_relation, subsort, sort FROM dependency
	) sub
	ORDER BY 
		sort, 
		subsort,
		dep_name
)

SELECT dep_typ, dep_name, dep_relation FROM all_dependencies WHERE ds_id = ?

