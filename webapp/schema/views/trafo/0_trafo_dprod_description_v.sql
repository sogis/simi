/*
 * Ermittelt die WGC-Ebenen-Beschreibung abhängig vom Typ und Themenbezug des Datenprodukts.
 */
CREATE VIEW simi.trafo_dprod_description_v AS 

WITH 

themepub_descr_part AS (
    SELECT 
        concat_ws(
            '<br/><br/>', 
            'Teil des Themas <b>' || COALESCE(tp.title_override, t.title) || ':</b>', 
            COALESCE(tp.description_override, t.description),
            '<a href="' || t.further_info_url || '"  target="_blank">Weitere Informationen zum Thema</a>'
            ) AS descr_part,
        tp.id AS tp_id
    FROM 
        simi.simitheme_theme_publication tp 
    JOIN
        simi.simitheme_theme t ON tp.theme_id = t.id
    WHERE -- Ausschluss der Pseudo-Themen
            t.identifier != 'theme.from.migration'
        AND 
            NOT t.identifier LIKE 'orgtheme.%'
),

dprod_descr_part AS (
    SELECT 
        dp.id AS dp_id,
        dp.theme_publication_id AS tp_id,
        CASE
            WHEN m.id IS NULL THEN COALESCE(dp.description, tbl.description_override, tbl.description_model, '-')
            ELSE 'Info: Dieses Produkt ist eine Karte. Karten haben keine Beschreibung'
        END AS descr_part
    FROM
        simi.simiproduct_data_product dp
    LEFT JOIN -- Falls produkt = tableview: Beschreibung berücksichtigen
        simi.simidata_table_view tv ON dp.id = tv.id 
    LEFT JOIN 
        simi.simidata_postgres_table tbl ON tv.postgres_table_id = tbl.id 
    LEFT JOIN 
        simi.simiproduct_map m ON dp.id = m.id
)

SELECT 
    concat_ws( -- Gibt die Ebenen-Beschreibung zurück, falls Themenbeschreibung null
        '<br/><br/>',
        l.descr_part,
        t.descr_part
    ) AS descr,
    l.dp_id
FROM 
    dprod_descr_part l
LEFT JOIN
    themepub_descr_part t ON l.tp_id = t.tp_id
;
