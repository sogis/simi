package ch.so.agi.simi.core.props;

import ch.so.agi.simi.util.properties.SimiProperty;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.SortedSet;

@Service(PropertiesConfiguredService.NAME)
public class PropertiesConfiguredServiceBean implements PropertiesConfiguredService {

    @Inject
    private PropertiesConfiguredProps conf;

    @Override
    public void assertAllPropertiesConfigured(SortedSet<SimiProperty> props){

        SimiProperty.setProps(props);
        SimiProperty.addValuesForModule(false);

        boolean allMandatoriesPresent = SimiProperty.assertConfigComplete();

        if(!allMandatoriesPresent && conf.isShutdownOnIncomplete())
            throw new RuntimeException("Configuration parameters are missing. See log for details");
    }

}