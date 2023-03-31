package ch.so.agi.simi.core.dependency.gretl;

import ch.so.agi.simi.global.exc.SimiException;
import ch.so.agi.simi.util.properties.PropsUtil;
import com.haulmont.cuba.core.sys.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.*;

public class GretlSearch {

    private static Logger log = LoggerFactory.getLogger(GretlSearch.class);

    public static final String SIMI_GITSEARCH_TOKEN_PROP = "simi.gitSearch.token";

    private String tableName;
    private String gitSearchToken;

    private String confUrl;
    private String[] confRepos;

    private RestTemplate restTemplate = new RestTemplate();

    private GretlSearch(String tableName, GretlSearchConfig config){

        this.gitSearchToken = readToken();
        if(this.gitSearchToken == null || this.gitSearchToken.length() == 0)
            throw new SimiException("Configuration error - required property " + SIMI_GITSEARCH_TOKEN_PROP + " not set.");

        this.tableName = tableName;

        resolveConfig(config);
    }

    private String readToken(){

        String res = null;

        try{
            res = AppContext.getProperty(SIMI_GITSEARCH_TOKEN_PROP);
        }
        catch(Exception e){}

        if(res == null || res.length() == 0) { // unit test
            try {
                res = System.getProperty(SIMI_GITSEARCH_TOKEN_PROP);
            }
            catch(Exception e){}
        }

        if(res == null || res.length() == 0)
            throw new SimiException("Could not read property {0} from environment", SIMI_GITSEARCH_TOKEN_PROP);

        return res;
    }

    private void resolveConfig(GretlSearchConfig config){
        this.confUrl = config.getGitHubSearchBaseUrl();
        this.confRepos = PropsUtil.toArray(config.getReposToSearch());
    }

    public static List<String> loadGretlDependencies(String tableName, GretlSearchConfig config){
        GretlSearch query = new GretlSearch(tableName, config);

        return query.exec();
    }

    private List<String> exec(){

        List<String> searchRes = new LinkedList<>();
        String message = "";

        for (String repo : confRepos) {
            GretlSearchResult gitResult = searchInRepo(repo);

            if (gitResult == null || gitResult.getItems() == null)
                continue;

            for (GretlSearchItem item : gitResult.getItems()) {
                searchRes.add(item.getPath());
            }
        }

        return searchRes;
    }

    private GretlSearchResult searchInRepo(String repoName){

        String queryUrl = MessageFormat.format("{0}?q=repo:{1} {2}", confUrl, repoName, tableName);

        log.debug("Searching github using url {}", queryUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + gitSearchToken);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<GretlSearchResult> response = restTemplate.exchange(queryUrl,
                HttpMethod.GET, entity, new ParameterizedTypeReference<GretlSearchResult>() {});

        return response.getBody();
    }
}
