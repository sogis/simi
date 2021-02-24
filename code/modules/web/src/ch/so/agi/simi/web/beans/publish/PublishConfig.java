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

/*
Folgende URL verwenden, um einen entfernte Build auszulösen: JENKINS_URL/job/confjob/build?token=TOKEN_NAME oder /buildWithParameters?token=TOKEN_NAME
Fügen Sie optional ein &cause=Grund+des+Builds an die URL an. Dieser Text wird dann in den aufgezeichneten Grund für diesen Build aufgenommen.


 */
