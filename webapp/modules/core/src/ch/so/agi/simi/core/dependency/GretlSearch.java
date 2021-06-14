package ch.so.agi.simi.core.dependency;

import ch.so.agi.simi.util.properties.PropsUtil;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GretlSearch {

    private String schemaName;
    private String tableName;

    private String confUrl;
    private String[] confRepos;

    private RestTemplate restTemplate = new RestTemplate();

    private GretlSearch(String schemaName, String tableName, GretlSearchConfig config){
        this.schemaName = schemaName;
        this.tableName = tableName;

        resolveConfig(config);
    }

    private void resolveConfig(GretlSearchConfig config){
        this.confUrl = config.getGitHubSearchBaseUrl();
        this.confRepos = PropsUtil.toArray(config.getReposToSearch());
    }

    public static List<DependencyInfo> queryGretlDependencies(String[] qualTableName, GretlSearchConfig config){
        GretlSearch query = new GretlSearch(qualTableName[0], qualTableName[1], config);
        return query.execQuery();
    }

    private List<DependencyInfo> execQuery(){

        List<DependencyInfo> diUnion = new LinkedList<>();

        for(String repo : confRepos){

            String qValue = MessageFormat.format(
                    "repo:{0} {1} {2}",
                    repo,
                    schemaName,
                    tableName
            );

            HashMap<String, String> params = new HashMap<>();
            params.put("q", "\'" + qValue + "\'");

            GretlSearchResult res = restTemplate.getForObject(confUrl + "?q={q}", GretlSearchResult.class, params);

            if(res != null)
                appendToDiList(diUnion, res.getItems());
        }

        return diUnion;
    }

    private static void appendToDiList(List<DependencyInfo> diUnion, List<GretlSearchItem> items) {

        if(items == null)
            return;

        for( GretlSearchItem item : items){
            DependencyInfo di = new DependencyInfo();

            di.setObjectName(item.getPath());
            di.setDependencyInfo("Schema- und Tabellenname kommt in GRETL-Datei vor.");
            di.setObjectType("GRETL-Datei");

            diUnion.add(di);
        }
    }
}
