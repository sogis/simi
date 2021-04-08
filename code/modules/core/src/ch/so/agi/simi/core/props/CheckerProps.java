package ch.so.agi.simi.core.props;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;

@Source(type = SourceType.APP)
public interface CheckerProps extends Config {
    @Property("simi.config.stopOnIncomplete")
    boolean stopOnIncomplete();
}

