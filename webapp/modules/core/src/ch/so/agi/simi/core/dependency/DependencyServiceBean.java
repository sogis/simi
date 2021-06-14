package ch.so.agi.simi.core.dependency;

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
    @Inject
    private DataManager dataManager;

    public List<DependencyInfo> collectAllDependenciesForTable(UUID tableId){

        List<DependencyInfo> dependencies = DependencyQuery.execute(tableId, persistence, resources);

        String[] qualTableName = TableNameQuery.execute(tableId, dataManager);

        List<DependencyInfo> gretlDep = GretlSearch.queryGretlDependencies(qualTableName, gretlSearchConfig);
        dependencies.addAll(gretlDep);

        return dependencies;
    }
}