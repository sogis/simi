/* 
 * Gibt alle für den Datenbezug (Datensuche, Datenbeschreibung, Geocat) relevanten manuell konfigurierten
 * Informationen als jsonb aus. 
 */
CREATE VIEW simi.app_pubsubarea_v AS 

WITH 

tp_ident_title AS (
  SELECT 
    concat_ws('.', th.identifier, class_suffix_override) AS identifier,
    COALESCE(tp.title_override, th.title) AS title,
    tp.id AS tp_id
  FROM
    simi.simitheme_theme_publication tp
  JOIN
    simi.simitheme_theme th ON tp.theme_id = th.id 
)

,subarea_coverage_ident AS (
	SELECT 
		identifier AS sub_ident,
		coverage_ident,
		id AS sub_id
	FROM
		simi.simitheme_sub_area
)

SELECT 
	concat(identifier, ' (', title, ')') AS tp_display,
	concat(sub_ident, ' (', coverage_ident, ')') AS sub_display,
	published,
	id
FROM
	simi.simitheme_published_sub_area ps
JOIN
	tp_ident_title tp ON ps.theme_publication_id = tp.tp_id
JOIN 
    subarea_coverage_ident ident ON ps.sub_area_id = ident.sub_id
;
	
	
