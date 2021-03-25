package ch.so.agi.simi.core.props;

import ch.so.agi.simi.util.properties.SimiProperty;

import java.util.SortedSet;

public interface PropertiesConfiguredService {
    String NAME = "simi_PropertiesConfiguredService";

    void assertAllPropertiesConfigured(SortedSet<SimiProperty> props);
}