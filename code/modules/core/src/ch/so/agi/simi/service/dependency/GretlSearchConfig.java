package ch.so.agi.simi.service.dependency;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;

@Source(type = SourceType.APP)
public interface GretlSearchConfig extends Config {

    @Property("simi.core.gretlquery.baseurl")
    String getGitHubSearchBaseUrl();

    @Property("simi.core.gretlquery.repos")
    String getReposToSearch();
}