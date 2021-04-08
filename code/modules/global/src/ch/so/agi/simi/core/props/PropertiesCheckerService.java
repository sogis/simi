package ch.so.agi.simi.core.props;

import ch.so.agi.simi.util.properties.SimiProperty;

import java.util.SortedSet;

public interface PropertiesCheckerService {
    String NAME = "simi_PropertiesCheckerService";

    void assertAllPropertiesConfigured(SortedSet<SimiProperty> props);
}