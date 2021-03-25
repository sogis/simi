package ch.so.agi.simi.listeners;

import ch.so.agi.simi.core.props.PropertiesConfiguredService;
import com.haulmont.cuba.core.sys.events.AppContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("simi_AppLifecycleEventListener_ApplicationContextListener")
public class AppLifecycleEventListener {

    @Inject
    PropertiesConfiguredService service;

    @EventListener
    public void applicationContextStarted(AppContextStartedEvent event) {


/*
        SimiProperty.addValuesForModule(true);
        service.assertAllPropertiesConfigured(SimiProperty.getProps());

 */
    }
}