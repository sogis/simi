package ch.so.agi.simi.service.dependency;

import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Service(DependencyService.NAME)
public class DependencyServiceBean implements DependencyService {

    @Inject
    private GretlSearchConfig gretlSearchConfig;

    public List<DependencyInfo> collectDependenciesForTable(UUID tableId){
        return null;
    }

    public List<DependencyInfo> productDependenciesForSingleActor(UUID singleActorId){
        return null;
    }

    public List<DependencyInfo> collectDependenciesForUnregisteredTable(String schemaName, String tableName){
        return GretlSearch.queryGretlDependencies(schemaName, tableName, gretlSearchConfig);
    }

}