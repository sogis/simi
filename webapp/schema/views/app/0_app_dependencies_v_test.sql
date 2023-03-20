
DROP TABLE IF EXISTS SIMI.APP_DEPENDENCIES_V_TEST;

create table SIMI.APP_DEPENDENCIES_V_TEST (
    ID integer,
    display text not null,
    LEVEL_NUM integer not null,
    ident text not null,
    type_display text not null,
    type_ident text not null,
    upstream_id uuid,
    obj_id uuid not null,
    --
    primary key (ID)
);


WITH 

dtype_map AS (
    SELECT
        *
    FROM (
        VALUES
        ('schema', 0, 'Schema'),
        ('table', 1, 'Tabelle'),
        ('module', 3, 'Modul'),
        ('feat_info', 3, 'Spez. Featureinfo'),
        ('ccc', 3, 'CCC-Integration'),
        ('report', 3, 'Report'),
        ('map', 4, 'Karte'),
        ('dsv_ext', 2, 'Ext. WMS'),
        ('fassade', 3, 'Fassade'),
        ('group', 4, 'Gruppe'),
        ('dsv_ras', 2, 'View (Raster)'),
        ('dsv_vec', 2, 'View (Vector)'),
        ('dsv_search', 2, 'View (Suche)')
    )
    AS t (type_ident, level_num, type_display)   
),

test_obj AS (
    SELECT
        *
    FROM (
        VALUES
        (0, 'schema', gen_random_uuid()),
        (1, 'schema', gen_random_uuid()),
        (10, 'table', gen_random_uuid()),
        (11, 'table', gen_random_uuid()),
        (20, 'dsv_ext', gen_random_uuid()),
        (21, 'dsv_ras', gen_random_uuid()),
        (22, 'dsv_vec', gen_random_uuid()),
        (23, 'dsv_search', gen_random_uuid()),
        (30, 'fassade', gen_random_uuid()),
        (31, 'feat_info', gen_random_uuid()),
        (32, 'ccc', gen_random_uuid()),
        (33, 'report', gen_random_uuid()),
        (34, 'module', gen_random_uuid()),
        (35, 'group', gen_random_uuid()),
        (36, 'map', gen_random_uuid()),
        (40, 'group', gen_random_uuid()),
        (41, 'map', gen_random_uuid())
    )
    AS t (id, type_ident, obj_id) 
),

test_obj_grp AS (
    SELECT 
        *,
        id / 10 AS group_id
    FROM
        test_obj       
),

upstream_ids AS (
    SELECT 
        min(id) AS id        
    FROM
        test_obj_grp
    GROUP BY 
        group_id
),

upstream_obj AS (
    SELECT 
        o.*
    FROM
        test_obj_grp o
    JOIN 
        upstream_ids i ON o.id = i.id
),

obj_base AS ( --
    SELECT 
        o.*,
        u.obj_id AS upstream_id
    FROM 
        test_obj_grp o
    LEFT JOIN
        upstream_obj u ON o.group_id = (u.group_id + 1)
),

obj AS (
    SELECT 
        concat_ws('.', o.type_ident, o.id) AS ident,
        m.type_display,
        m.level_num,
        o.type_ident,
        o.id,
        o.obj_id,
        CASE 
            WHEN o.type_ident IN ('dsv_ras', 'dsv_ext') THEN NULL
            ELSE o.upstream_id            
        END AS upstream_id
    FROM 
        obj_base o
    LEFT JOIN
        dtype_map m ON o.type_ident = m.type_ident 
)

INSERT INTO SIMI.APP_DEPENDENCIES_V_TEST(
    ID,
    display,
    LEVEL_NUM,
    ident,
    type_display,
    type_ident,
    upstream_id,
    obj_id
)
SELECT
    ID,
    ident AS display,
    LEVEL_NUM,
    ident,
    type_display,
    type_ident,
    upstream_id,
    obj_id
FROM
    obj
WHERE 
    type_ident != 'schema'
;