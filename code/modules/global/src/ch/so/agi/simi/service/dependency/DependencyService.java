package ch.so.agi.simi.service.dependency;

import java.util.List;
import java.util.UUID;

public interface DependencyService {
    String NAME = "simi_DependencyService";

    List<DependencyInfo> collectDependenciesForTable(UUID tableId);

    List<DependencyInfo> productDependenciesForSingleActor(UUID singleActorId);

    List<DependencyInfo> collectDependenciesForUnregisteredTable(String schemaName, String tableName);
}