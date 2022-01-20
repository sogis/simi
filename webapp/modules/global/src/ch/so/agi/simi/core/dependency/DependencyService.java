package ch.so.agi.simi.core.dependency;

import java.util.List;
import java.util.UUID;

public interface DependencyService {
    String NAME = "simi_DependencyService";

    /**
     * Sammelt die Abhängigkeiten der mittels ID übergebenen Postgrestable.
     * @param tableId Id der Postgrestable.
     * @return Liste von DependencyInfo Objekten. Ein Objekt beschreibt menschenlesbar das abhängige Objekt
     */
    List<DependencyInfo> collectAllDependenciesForTable(UUID tableId);

    //List<DependencyInfo> collectDependenciesForUnregisteredTable(String schemaName, String tableName);
}