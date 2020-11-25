package ch.so.agi.simi.web.beans.gretl;

import org.springframework.stereotype.Component;

/*
curl \
-G \
-H "Accept: application/vnd.github.v3+json" \
--data-urlencode q="ada_denkmalschutz_pub repo:sogis/gretljobs" \
https://api.github.com/search/code



Nützliche information
* Repo (falls mehrere Durchsucht werden): items[x].repository.full_name
* Name und Pfad innerhalb repo: --> items[x].path

Ausschnitt aus Response:
{
	"total_count": 1,
	"incomplete_results": false,
	"items": [{
		"name": "build.gradle",
		"path": "ada_denkmalschutz_pub/build.gradle",
		"repository": {
			"id": 98890540,
			"node_id": "MDEwOlJlcG9zaXRvcnk5ODg5MDU0MA==",
			"name": "gretljobs",
			"full_name": "sogis/gretljobs",
			"private": false,
		},
		"score": 1.0
	}]
}



 */

@Component(GithubSearchBean.NAME)
public class GithubSearchBean {
    public static final String NAME = "simi_GithubSearch";
}