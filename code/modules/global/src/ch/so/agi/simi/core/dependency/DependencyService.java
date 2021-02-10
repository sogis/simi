package ch.so.agi.simi.core.dependency;

import java.util.List;
import java.util.UUID;

public interface DependencyService {
    String NAME = "simi_DependencyService";

    List<DependencyInfo> collectAllDependenciesForTable(UUID tableId);

    //List<DependencyInfo> collectDependenciesForUnregisteredTable(String schemaName, String tableName);
}