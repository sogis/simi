package ch.so.agi.simi.service.dependency;

public class GretlSearchConfigForTest implements GretlSearchConfig {

    private String gitHubSearchUrl;
    private String reposToSearch;

    public GretlSearchConfigForTest(String gitHubSearchUrl, String reposToSearch){
        this.gitHubSearchUrl = gitHubSearchUrl;
        this.reposToSearch = reposToSearch;
    }

    @Override
    public String getGitHubSearchBaseUrl() {
        return gitHubSearchUrl;
    }

    @Override
    public String getReposToSearch() {
        return reposToSearch;
    }
}
