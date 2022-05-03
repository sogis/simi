UPDATE
  simi.simiproduct_data_product
SET
  dtype = 'simiProduct_ExternalWmsLayers'
WHERE
  dtype = 'simiProduct_ExternalMapLayers'
;