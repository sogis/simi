package ch.so.agi.simi.core.dependency.simi;

import ch.so.agi.simi.core.dependency.DependencyListResult;
import ch.so.agi.simi.core.dependency.DependencyListServiceBean;
import ch.so.agi.simi.entity.dependency.Dependency;
import ch.so.agi.simi.entity.dependency.DependencyBase;
import com.haulmont.cuba.core.global.DataManager;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class SimiSearch {

    private DataManager dataManager;

    private Class subEntity = Dependency.class; // Modified by test to run with test data

    public SimiSearch(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    void _setDbEntityClass(Class c) {
        if(!(DependencyBase.class.isAssignableFrom(c)))
            throw new RuntimeException("Given class must be a subclass of " + DependencyBase.class.getSimpleName());

        this.subEntity = c;
    }

    public List<DependencyBase> loadSimiDependencies(List<UUID> ids) {

        List<DependencyBase> previousLevel = queryRootLevel(ids);

        List<DependencyBase> allLevels = new LinkedList<>();

        for(int i=0; i<10; i++) { //stop in any case after 10 iterations
            allLevels.addAll(previousLevel);

            List<DependencyBase> nextLevel = queryNextLevel(previousLevel);
            if(nextLevel.size() == 0)
                break;

            previousLevel = nextLevel;
        }

        return allLevels;
    }

    private List<DependencyBase> queryNextLevel(List<DependencyBase> previousLevel){
        // build upstream id list from previous
        List<UUID> prevIds = previousLevel.stream().map(previous -> previous.getObjId()).collect(Collectors.toList());

        List<? extends DependencyBase> nextLevel = dataManager.load(subEntity)
                .query("select d from " + nameForSubEntity() + " d where d.upstreamId in :ids")
                .parameter("ids", prevIds)
                .list();

        List<DependencyBase> nextLevelBase = toBaseList(nextLevel);

        DependencyListServiceBean.copyRootToNext(previousLevel, nextLevelBase);

        return nextLevelBase;
    }

    private List<DependencyBase> queryRootLevel(List<UUID> ids){
        // Query root level
        List<? extends DependencyBase> rootLevel = dataManager.load(subEntity)
                .query("select d from " + nameForSubEntity() + " d where d.objId in :ids")
                .parameter("ids", ids)
                .list();

        // Set root name to self
        for(DependencyBase obj : rootLevel){
            obj.setRootObjName(obj.getObjName());
        }

        return toBaseList(rootLevel);
    }

    private static List<DependencyBase> toBaseList(List<? extends DependencyBase> list){
        LinkedList<DependencyBase> res = new LinkedList<>();
        res.addAll(list);

        return res;
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