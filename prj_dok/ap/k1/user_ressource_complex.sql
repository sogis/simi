WITH

uid_role_direct AS (
	SELECT 
		id_user,
		id_role
	FROM 
		user_role
),

uid_role_via_group AS (
	SELECT 
		id_user,
		id_role
	FROM 
		group_user
	JOIN
		group_role 
			ON group_user.id_group = group_role.id_group 
),

uid_role_all AS (
	SELECT * FROM uid_role_direct
	UNION ALL 
	SELECT * FROM uid_role_via_group
),

realuser_role AS (
	SELECT 
		"name" AS user_ident,
		id_role
	FROM 
		iam."user" u 
	JOIN
		uid_role_all
			ON u.id = uid_role_all.id_user
),

user_from_group__role AS (
	SELECT 
		"name" AS user_ident,
		id_role
	FROM 
		iam."group"
	JOIN
		iam.group_role 
			ON "group".id = group_role.id_group 
	WHERE
		"name" LIKE 'Bau- und Justizdepartement/%' -- Am Beispiel BJD
),

user_from_role__role AS ( -- sauberer, wenn zukünftig der Benutzer "anonymous" erfasst wird, mit Rollenzuweisung zu "public". Wird hier simuliert
	SELECT 
		'anonymous' AS user_ident,
		id AS id_role
	FROM 
		iam."role"
	WHERE
		"name" = 'public'
),

allusers_allroles AS (
	SELECT * FROM realuser_role
	UNION ALL 
	SELECT * FROM user_from_group__role
	UNION ALL
	SELECT * FROM user_from_role__role
),

/*
Unverständlich, wieso es in der config-db knapp 6000 ressourcen gibt. Erwartet: < 1000
*/
perm_level AS (
	SELECT 
		id_role, 
		gdi_oid_resource, 
		CASE "write"
			WHEN TRUE THEN '2_write'
			WHEN FALSE	THEN '1_read'
			ELSE NULL
		END AS perm_level
	FROM 
		iam.resource_permission
),

allusers_maxperm AS (
	SELECT 
		user_ident,
		gdi_oid_resource,
		max(perm_level) AS max_level
	FROM 
		allusers_allroles
	JOIN 
		perm_level 
			ON allusers_allroles.id_role = perm_level.id_role
	GROUP BY 
		user_ident,
		gdi_oid_resource
),

user_ressource_permission AS (
	SELECT 
		user_ident,
		"name" AS resource_ident,
		max_level
	FROM 
		allusers_maxperm
	JOIN
		gdi_knoten.data_set 
			ON allusers_maxperm.gdi_oid_resource = data_set.gdi_oid
	ORDER BY 
		user_ident,
		"name"
)

SELECT 
	json_build_object(
		'user_ident', user_ident, 
		'resource_ident', resource_ident, 
		'max_level', max_level
		) AS json_obj
FROM user_ressource_permission

/*

realuser_maxperm_ AS (
	SELECT
		name AS user_name,
		max(perm_level) AS max_level,
		gdi_oid_resource
	FROM 
		iam.USER
	JOIN
		uid_role_all
			ON iam.USER.id = uid_role_all.id_user
	JOIN 
		perm_level
			ON uid_role_all.id_role = perm_level.id_role
	GROUP BY 
		name,
		gdi_oid_resource
),

user_from_group_maxperm AS (
	SELECT 
		"name" AS user_name,
		max(perm_level) AS max_level,
		gdi_oid_resource
	FROM 
		iam."group"
	JOIN
		iam.group_role 
			ON "group".id = group_role.id_group 
	JOIN
		perm_level
			ON group_role.id_role = perm_level.id_role
	WHERE
		"name" LIKE 'Bau- und Justizdepartement/%' -- Am Beispiel BJD
	GROUP BY 
		name,
		gdi_oid_resource
),

user_from_role_maxperm AS ( -- für "public"
	SELECT 
		"name" AS user_name,
		max(perm_level) AS max_level,
		gdi_oid_resource
	FROM 
		iam."role"
	JOIN
		perm_level
			ON "role".id = perm_level.id_role
	WHERE
		"name" = 'public'
	GROUP BY 
		name,
		gdi_oid_resource
),

*/




