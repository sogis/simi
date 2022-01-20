package ch.so.agi.simi.core.props;

import ch.so.agi.simi.util.properties.SimiProperty;

import java.util.SortedSet;

public interface PropertiesCheckerService {
    String NAME = "simi_PropertiesCheckerService";

    /**
     * Prüft, ob die ENV-Konfiguration von SIMI komplett ist. Wird beim Startup von Simi aufgerufen.
     * @param props Set der ENV-Konfigurationen für Modul Web.
     */
    void assertAllPropertiesConfigured(SortedSet<SimiProperty> props);
}