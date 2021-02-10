package ch.so.agi.simi.core.dependency;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.type.Factory;
import com.haulmont.cuba.core.config.type.StringListStringify;
import com.haulmont.cuba.core.config.type.StringListTypeFactory;
import com.haulmont.cuba.core.config.type.Stringify;

import java.util.List;

@Source(type = SourceType.APP)
public interface GretlSearchConfig extends Config {

    @Property("simi.gitSearch.url")
    String getGitHubSearchBaseUrl();

    @Property("simi.gitSearch.repos")
    @Factory(factory = StringListTypeFactory.class)
    @Stringify(stringify = StringListStringify.class)
    List<String> getReposToSearch();
}