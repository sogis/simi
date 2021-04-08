package ch.so.agi.simi.core.props;

import ch.so.agi.simi.util.properties.SimiProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.SortedSet;

@Service(PropertiesCheckerService.NAME)
public class PropertiesCheckerServiceBean implements PropertiesCheckerService {

    private static Logger log = LoggerFactory.getLogger(PropertiesCheckerServiceBean.class);

    @Inject
    private CheckerProps conf;

    @Override
    public void assertAllPropertiesConfigured(SortedSet<SimiProperty> props){

        SimiProperty.setProps(props);
        SimiProperty.addValuesForModule(false);

        boolean allMandatoriesPresent = SimiProperty.assertConfigComplete();

        if(allMandatoriesPresent){
            log.info("Simi configuration is complete");
        }
        else {
            log.error("Simi configuration is incomplete. See above for details");
        }

        /*
        if(!allMandatoriesPresent && conf.stopOnIncomplete())
            throw new RuntimeException("Configuration parameters are missing. See log for details");

         */


    }

}