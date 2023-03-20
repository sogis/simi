package ch.so.agi.simi.core.dependency.v1;

import ch.so.agi.simi.core.dependency.DependencyInfo;
import ch.so.agi.simi.core.dependency.DependencyService;
import ch.so.agi.simi.core.dependency.gretl.GretlSearch;
import ch.so.agi.simi.core.dependency.gretl.GretlSearchConfig;
import ch.so.agi.simi.entity.dependency.Dependency;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.*;

@Service(DependencyService.NAME)
public class DependencyServiceBean implements DependencyService {

    private static Logger log = LoggerFactory.getLogger(DependencyServiceBean.class);

    @Inject
    private GretlSearchConfig gretlSearchConfig;
    @Inject
    private Resources resources;
    @Inject
    private Persistence persistence;
    @Inject
    private DataManager dataManager;

    private static final String TABLE_TYPE_DEPENDENCY = "table";

    private Class subEntity = Dependency.class; // Modified by test to run with test data

    @Override
    public List<DependencyInfo> collectAllDependenciesForTable(UUID tableId){

        log.info("Collecting dependencies for pgtable {} with gretl search conf {}", tableId, gretlConf_ToString());

        List<DependencyInfo> dependencies = DependencyQuery.execute(tableId, persistence, resources);

        String[] qualTableName = TableNameQuery.execute(tableId, dataManager);

        List<DependencyInfo> gretlDep = GretlSearch.queryGretlDependencies(qualTableName, gretlSearchConfig);
        dependencies.addAll(gretlDep);

        return dependencies;
    }

    private String gretlConf_ToString(){
        return MessageFormat.format("repos: {0}, search api url {1}", gretlSearchConfig.getReposToSearch(), gretlSearchConfig.getGitHubSearchBaseUrl());
    }
}