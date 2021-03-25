package ch.so.agi.simi.core.props;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.Default;

@Source(type = SourceType.APP)
public interface PropertiesConfiguredProps extends Config {

    @Property("simi.config.shutdownOnIncomplete")
    @Default("TRUE")
    boolean isShutdownOnIncomplete();
}
