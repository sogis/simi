package ch.so.agi.simi.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;

@Source(type = SourceType.APP)
public interface SchemareaderConfig extends Config {

    @Property("schemareader.host")
    @Default("http://localhost")
    String getSchemareaderHost();

    @Property("schemareader.port")
    @Default("8081")
    int getSchemareaderPort();
}