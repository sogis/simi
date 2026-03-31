-- View: simi.trafo_wms_editlayer_v

-- DROP VIEW simi.trafo_wms_editlayer_v;

CREATE OR REPLACE VIEW simi.trafo_wms_editlayer_v
 AS
 SELECT concat_ws('.'::text, tv.identifier) AS identifier,
    false AS print_or_ext,
    jsonb_strip_nulls(jsonb_build_object('name', concat_ws('.'::text, tv.identifier), 'type', 'layer', 'datatype', 'vector', 'title', tv.title_ident, 'postgis_datasource', tbl.tbl_json, 'qml_base64', encode(convert_to(ta.form_qgs, 'UTF-8'::name), 'base64'::text), 'attributes', tv.attr_name_alias_js)) AS layer_json
   FROM simi.simidata_table_view ta
     JOIN simi.trafo_wms_tableview_v tv ON ta.id = tv.tv_id
     JOIN simi.simidata_data_set_view dsv ON tv.tv_id = dsv.id
     JOIN simi.trafo_wms_geotable_v tbl ON tv.tv_id = tbl.tv_id
  WHERE ta.form_qgs IS NOT NULL;

ALTER TABLE simi.trafo_wms_editlayer_v
    OWNER TO simi_ddl;

GRANT ALL ON TABLE simi.trafo_wms_editlayer_v TO simi_ddl;
GRANT SELECT ON TABLE simi.trafo_wms_editlayer_v TO simi_read;
GRANT SELECT ON TABLE simi.trafo_wms_editlayer_v TO simi_write;

