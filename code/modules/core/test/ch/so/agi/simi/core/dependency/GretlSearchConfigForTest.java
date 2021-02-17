package ch.so.agi.simi.core.dependency;

import java.util.ArrayList;
import java.util.List;

public class GretlSearchConfigForTest implements GretlSearchConfig {

    private String gitHubSearchUrl;
    private List<String> reposToSearch;

    public GretlSearchConfigForTest(String gitHubSearchUrl, String reposToSearch){
        this.gitHubSearchUrl = gitHubSearchUrl;

        this.reposToSearch = reposToSearch;
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
