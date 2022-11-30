-- SIMIPRODUCT_DATA_PRODUCT_PUB_SCOPE

/*
Zentrale Steuerungstabelle bezüglich der Publikation der verschiedenen Dataproducts.

Zu beachten: Die UUID '55bdf0dd-d997-c537-f95b-7e641dc515df' der zu löschenden Dataproducts wird
in den sql's zur Erzeugung der config.json direkt referenziert. MUSS ALSO STABIL BLEIBEN.

FOR_* steuert, ob die entsprechende Zeile der Steuerungstabelle im SIMI für das DataProduct angeboten wird.
Sprich, ob der Display-Text in der Combobox im SIMI-GUI zur Auswahl erscheint oder nicht.
FOR_DSV ist nicht treffend - sollte bei Schemaänderung in FOR_SA (SingleActor) umbenannt werden, da auch für FacadeLayer gültig.

PUB_TO_WMS steuert, ob ein SingleActor im WMS auf dem "root-level" publiziert wird.

PUB_TO_WGC: Ist aktuell (Februar 2022) gleichbedeutend mit "Ebene erscheint in der Suche"
Ausblick: Im Rahmen von "EXT WM(T)S Serverless" umformen in WGC_SEARCH_VISIBLE. 
PUB_TO_LOCATOR umformen in LOCATOR_SEARCH_VISIBLE. 
Die Unterscheidung nach Client ist notwendig wegen der "Hintergrundkarten"

DEFAULT_VALUE: Steuerung der SIMI-Combobox bei neuen Dataproducts
SORT: Reihenfolge in der SIMI-Combobox

OVERALL_STATE: Vorgesehen für einfache queries in den SQLs der Json-Erzeugung. Aktuell nicht verwendet (Februar 2022)
*/
insert into SIMIPRODUCT_DATA_PRODUCT_PUB_SCOPE
    (ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DEFAULT_VALUE, OVERALL_STATE, FOR_DSV, FOR_GROUP, FOR_MAP, SORT, PUB_TO_WMS, PUB_TO_WGC, PUB_TO_LOCATOR, DISPLAY_TEXT)
values
    ('3db555be-7b10-5fb2-1c5d-61af147b3ee4', 1, NOW(), 'admin', NOW(), null,  true, 10,  true,  true, false,  1,  true,  true,  true, 'WGC, QGIS u. WMS'),
    ('c849480f-40f7-5c33-b002-84da80fadc40', 1, NOW(), 'admin', NOW(), null, false, 10,  true,  true, false, 10,  true, false, false, 'Nur WMS'),
    ('cb94803d-03d3-dc1f-8f08-3a842f14bf45', 1, NOW(), 'admin', NOW(), null,  true, 10, false, false,  true, 20, false,  true,  true, 'WGC u. QGIS'),
    ('0a866696-8903-c333-c2c7-db6b6fd8d628', 1, NOW(), 'admin', NOW(), null, false, 20,  true, false, false, 30, false, false, false, 'Nicht (selbst) publiziert'),
    ('55bdf0dd-d997-c537-f95b-7e641dc515df', 1, NOW(), 'admin', NOW(), null, false, 30,  true,  true,  true, 40, false, false, false, 'Zu Löschen');

-- Datenbank postgis-enablen
CREATE EXTENSION IF NOT EXISTS postgis;
