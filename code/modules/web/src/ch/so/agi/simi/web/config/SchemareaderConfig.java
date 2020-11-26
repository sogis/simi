package ch.so.agi.simi.web.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;

@Source(type = SourceType.APP)
public interface SchemareaderConfig extends Config {

    @Property("simi.web.schemareader.baseurl")
    String getBaseUrl();
}