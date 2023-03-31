package ch.so.agi.simi.core.dependency;

import ch.so.agi.simi.core.dependency.gretl.GretlSearch;
import ch.so.agi.simi.core.dependency.gretl.GretlSearchConfig;
import ch.so.agi.simi.core.dependency.simi.SimiSearch;
import ch.so.agi.simi.core.dependency.v1.DependencyServiceBean;
import ch.so.agi.simi.entity.dependency.Dependency;
import ch.so.agi.simi.entity.dependency.DependencyBase;
import com.haulmont.cuba.core.global.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service(DependencyListService.NAME)
public class DependencyListServiceBean implements DependencyListService {

    private static Logger log = LoggerFactory.getLogger(DependencyServiceBean.class);

    @Inject
    private GretlSearchConfig gretlSearchConfig;

    @Inject
    private DataManager dataManager;

    private Class subEntity = Dependency.class; // Modified by test to run with test data

    @Override
    public DependencyListResult listDependenciesForObjs(List<UUID> ids){
        List<String> messages = new LinkedList<>();

        List<DependencyBase> allDep = new LinkedList<>();
        List<DependencyBase> simiDep =  new SimiSearch(dataManager).loadSimiDependencies(ids);

        allDep.addAll(simiDep);

        String msg = tryAppendGretlDependencies(allDep);
        if(msg != null)
            messages.add(msg);

        List<DependencyDto> dto = convertToRes(allDep);
        return new DependencyListResult(dto, messages);
    }

    private static List<DependencyDto> convertToRes(List<DependencyBase> lDeps){
        List<DependencyDto> res = new LinkedList<>();

        // join each lDep to its upstream dependency
        Map<UUID, DependencyBase> lookUp = lDeps.stream().collect(Collectors.toMap(
                DependencyBase::getObjId,
                Function.identity(),
                (dep1, dep2) -> dep1 // lDeps can contain a dependency multiple times. Taking first by convention OK because only next -> previous and root is returned, not whole tree
        ));

        for(DependencyBase lDep : lDeps){

            if(lDep.getObjName().equals(lDep.getRootObjName())) //exclude root entries from result
                continue;

            DependencyBase upstream = lookUp.get(lDep.getUpstreamId());

            DependencyDto dr = new DependencyDto();
            dr.setDisplay(lDep.getObjName());
            dr.setRootObjName(lDep.getRootObjName());
            dr.setUpstreamDisplay(upstream.getObjName());
            dr.setDependency(lDep.getTypeName() +  " | verwendet | " + upstream.getTypeName());

            res.add(dr);
        }

        return res;
    }

    private String tryAppendGretlDependencies(List<DependencyBase> simiDependencies){

        String messageResult = null;

        try {
            List<DependencyBase> gretlDep = new LinkedList<>();

            for (DependencyBase dep : simiDependencies) {

                if (dep.getQualTableName() == null)
                    continue;

                String tableName = dep.getQualTableName().split("\\.")[1];
                List<String> gretlPaths = GretlSearch.loadGretlDependencies(tableName, gretlSearchConfig);

                for (String path : gretlPaths) {
                    DependencyBase gretl = new DependencyBase();
                    gretl.setObjName(path);
                    gretl.setTypeName("GRETL");
                    gretl.setLevelNum(dep.getLevelNum() + 1);
                    gretl.setUpstreamId(dep.getObjId());
                    gretl.setObjId(UUID.randomUUID());

                    gretlDep.add(gretl);
                }
            }

            copyRootToNext(simiDependencies, gretlDep);
            simiDependencies.addAll(gretlDep);
        }
        catch(Exception e){
            Set<String> schemas = simiDependencies.stream()
                    .filter(dep -> dep.getQualTableName() != null)
                    .map(dep -> dep.getQualTableName().split("\\.")[0])
                    .collect(Collectors.toSet());
            String schemaName = schemas.iterator().next();

            messageResult = "Abfrage der GRETL-Abhängigkeiten der Tabellen in Schema " + schemaName + " nicht möglich.<br/>" + e.getMessage();
        }

        return messageResult;
    }

    public static void copyRootToNext(List<DependencyBase> previous, List<DependencyBase> next){
        Map<UUID, DependencyBase> prevMap = previous.stream().collect(Collectors.toMap(
                DependencyBase::getObjId,
                Function.identity(),
                (dep1, dep2) -> dep1 // previous can contain a dependency multiple times. Take first by convention
        ));

        for(DependencyBase obj : next){
            DependencyBase prev = prevMap.get(obj.getUpstreamId());

            obj.setRootObjName(prev.getRootObjName());
        }
    }

    private String nameForSubEntity(){
        String res = null;

        try {
            Field nameField = subEntity.getField("NAME");
            res = nameField.get(null).toString();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

        return res;
    }
}