package ch.so.agi.simi.core.dependency;

import java.util.ArrayList;
import java.util.List;

public class GretlSearchConfigForTest implements GretlSearchConfig {

    private String gitHubSearchUrl;
    private List<String> reposToSearch;

    public GretlSearchConfigForTest(String gitHubSearchUrl, String repoToSearch){
        this.gitHubSearchUrl = gitHubSearchUrl;

        this.reposToSearch = new ArrayList<String>();
        this.reposToSearch.add(repoToSearch);
    }

    @Override
    public String getGitHubSearchBaseUrl() {
        return gitHubSearchUrl;
    }

    @Override
    public List<String> getReposToSearch() {
        return reposToSearch;
    }
}
