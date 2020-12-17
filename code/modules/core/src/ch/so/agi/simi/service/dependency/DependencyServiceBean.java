package ch.so.agi.simi.service.dependency;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Resources;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Service(DependencyService.NAME)
public class DependencyServiceBean implements DependencyService {

    @Inject
    private GretlSearchConfig gretlSearchConfig;
    @Inject
    private Resources resources;
    @Inject
    private Persistence persistence;


    public List<DependencyInfo> collectDependenciesForTable(UUID tableId){
        return DependencyQuery.execute(tableId, persistence, resources);
    }

    /*public List<DependencyInfo> productDependenciesForSingleActor(UUID singleActorId){
        return null;
    }
*/
    public List<DependencyInfo> collectDependenciesForUnregisteredTable(String schemaName, String tableName){
        return GretlSearch.queryGretlDependencies(schemaName, tableName, gretlSearchConfig);
    }

}