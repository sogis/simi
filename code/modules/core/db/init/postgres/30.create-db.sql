insert into SIMIPRODUCT_DATA_PRODUCT_PUB_SCOPE
    (ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, DISPLAY_TEXT, OVERALL_STATE, DEFAULT_VALUE, FOR_DSV, FOR_GROUP, FOR_MAP, PUB_TO_WMS, PUB_TO_WGC, PUB_TO_LOCATOR, SORT) 
values
    ('3db555be-7b10-5fb2-1c5d-61af147b3ee4', 1, NOW(), 'admin', NOW(), null, null, null, 'WGC, QGIS u. WMS', 10, true, true, true, false, true, true, true, 1),
    ('c849480f-40f7-5c33-b002-84da80fadc40', 1, NOW(), 'admin', NOW(), null, null, null, 'Nur WMS', 10, false, true, true, false, true, false, false, 10),
    ('cb94803d-03d3-dc1f-8f08-3a842f14bf45', 1, NOW(), 'admin', NOW(), null, null, null, 'WGC u. QGIS', 10, true, false, false, true, false, true, true, 20),
    ('0a866696-8903-c333-c2c7-db6b6fd8d628', 1, NOW(), 'admin', NOW(), null, null, null, 'Nicht (selbst) publiziert', 20, false, true, true, true, false, false, false, 30),
    ('55bdf0dd-d997-c537-f95b-7e641dc515df', 1, NOW(), 'admin', NOW(), null, null, null, 'Zu Löschen', 30, false, true, true, true, false, false, false, 40);
