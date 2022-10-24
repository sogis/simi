/* 
 * Gibt die Spalten der base view mit pretty_print zwecks Darstellung im SIMI AUS.
 */
CREATE VIEW simi.app_themepub_validation_v AS 

SELECT 
  identifier,
  public_model_name,
  title,
  short_description,
  jsonb_pretty(keywords) AS keywords,
  jsonb_pretty(synonyms) AS synonyms,
  jsonb_pretty(data_owner) AS data_owner,
  jsonb_pretty(data_servicer) AS data_servicer,
  further_info,
  jsonb_pretty(file_formats) AS file_formats,
  jsonb_pretty(tables_json) AS tables_json,
  jsonb_pretty(services) AS services,
  tp_id
FROM
  simi.app_themepub_base_v
;