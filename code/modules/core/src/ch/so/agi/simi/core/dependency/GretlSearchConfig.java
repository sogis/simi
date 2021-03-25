package ch.so.agi.simi.core.dependency;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;

@Source(type = SourceType.APP)
public interface GretlSearchConfig extends Config {

    @Property("simi.gitSearch.url")
    String getGitHubSearchBaseUrl();

    @Property("simi.gitSearch.repos")
    String getReposToSearch();
}