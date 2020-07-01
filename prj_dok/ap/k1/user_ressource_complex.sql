--Bildet die Berechtigungen exemplarisch auf die DatasetView's ab, um die Machbarkeit der Ausgabe mittels Json zu demonstrieren
WITH 

ds_attributes AS (
	SELECT
		data_set_view.gdi_oid,
		data_set_view."name" AS identifier,
		jsonb_agg(data_set_view_attributes.name ORDER BY data_set_view_attributes.name) AS attr_json
	FROM 
		gdi_knoten.data_set_view
	JOIN
		gdi_knoten.data_set_view_attributes
			ON data_set_view.gdi_oid = data_set_view_attributes.gdi_oid_data_set_view
	GROUP BY 
		data_set_view.gdi_oid,
		data_set_view."name"
),

ds_json AS (
	SELECT 
		gdi_oid,
		json_build_object('name', identifier, 'attributes', attr_json) AS ds_json
	FROM 
		ds_attributes	
),

role_permission AS (
	SELECT 
		r."name" AS role_name,
		to_jsonb(array_agg(ds_json.ds_json)) AS layers
	FROM 
		iam.resource_permission rp 
	JOIN 
		iam."role" r ON rp.id_role = r.id 
	JOIN 
		ds_json ON rp.gdi_oid_resource = ds_json.gdi_oid
	GROUP BY 
		r."name" 
)


SELECT * FROM role_permission



