/* 
 * Gibt für die Darstellung der einer Themenbereitstellung
 * zugeordneten Produkte neben den Produkteigenschaften das
 * Attribut [DataSetView].is_file_download_dsv aus (true/false/NULL).
 */
CREATE VIEW simi.app_theme_dprod_v AS 
SELECT 
  dp.id,
  dp.theme_publication_id,
  dp.derived_identifier, 
  dp.title,
  dp.theme_only_for_org,
  dsv.is_file_download_dsv 
FROM 
  simi.simiproduct_data_product dp
LEFT JOIN
  simi.simidata_data_set_view dsv 
    ON dp.id = dsv.id  