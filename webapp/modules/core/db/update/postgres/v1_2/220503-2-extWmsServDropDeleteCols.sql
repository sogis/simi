begin;

-- delete rows with delete stamp
DELETE FROM
  simi.simiproduct_external_wms_service
WHERE
  delete_ts IS NOT NULL
;

-- drop columns
ALTER TABLE simi.simiproduct_external_wms_service DROP COLUMN delete_ts;
ALTER TABLE simi.simiproduct_external_wms_service DROP COLUMN deleted_by;

commit;
