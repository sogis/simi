package ch.so.agi.simi.web.beans.publish;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;


@Source(type = SourceType.APP)
public interface PublishConfig extends Config {

    @Property("simi.publishJob.baseUrl")
    String getJobBaseUrl();

    @Property("simi.publishJob.pollTimeout")
    int getTimeOutMillis();

    @Property("simi.publishJob.secToken")
    String getSecToken();
}
