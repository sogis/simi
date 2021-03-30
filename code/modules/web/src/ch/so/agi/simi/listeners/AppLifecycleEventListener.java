package ch.so.agi.simi.listeners;

import ch.so.agi.simi.core.props.PropertiesConfiguredService;
import ch.so.agi.simi.util.properties.SimiProperty;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.core.sys.events.AppContextStartedEvent;
import com.haulmont.cuba.core.sys.servlet.events.ServletContextInitializedEvent;
import com.haulmont.cuba.security.auth.AuthenticationDetails;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.security.AnonymousUserCredentials;
import com.haulmont.cuba.web.security.providers.AnonymousLoginProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("simi_AppLifecycleEventListener")
public class AppLifecycleEventListener {


    private static Logger log = LoggerFactory.getLogger(AppLifecycleEventListener.class);

    @Inject
    private AnonymousLoginProvider loginProvider;

    @Inject
    private PropertiesConfiguredService service;

    @EventListener
    public void applicationContextStarted(AppContextStartedEvent event) {

        log.info("Health check");

        AnonymousUserCredentials credentials = new AnonymousUserCredentials();
        try {
            AuthenticationDetails authenticationDetails = loginProvider.login(credentials);
            if (authenticationDetails == null) {
                log.warn("Could not check configuration");
                return;
            }

            UserSession userSession = authenticationDetails.getSession();
            AppContext.withSecurityContext(new SecurityContext(userSession), () -> {
                SimiProperty.addValuesForModule(true);
                service.assertAllPropertiesConfigured(SimiProperty.getProps());
            });

        } catch (Exception e) {
            log.warn("Exception while trying to check configuration", e);
        }
    }
}