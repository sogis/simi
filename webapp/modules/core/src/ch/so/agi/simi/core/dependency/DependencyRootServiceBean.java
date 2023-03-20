package ch.so.agi.simi.core.dependency;

import ch.so.agi.simi.entity.data.DbSchema;
import ch.so.agi.simi.entity.data.PostgresTable;
import ch.so.agi.simi.entity.product.DataProduct;
import ch.so.agi.simi.entity.product.SingleActor;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(DependencyRootService.NAME)
public class DependencyRootServiceBean implements DependencyRootService {

    @Inject
    private DataManager dataManager;

    @Override
    public List<DependencyRootDto> queryRoots() {
        List<DependencyRootDto> res = new LinkedList<>();

        res.addAll(queryTableAndSchemaRoots());
        res.addAll(querySingleActorRoots());

        return res;
    }

    private List<DependencyRootDto> queryTableAndSchemaRoots(){
        List<PostgresTable> tbls = dataManager.load(PostgresTable.class)
                .query("select t from simiData_PostgresTable t")
                .view("dependencyRoot-postgresTable")
                .list();

        List<DependencyRootDto>  res = tbls.stream().map(tbl -> {
            DependencyRootDto dt = new DependencyRootDto();
            dt.setId(tbl.getId());
            dt.setDisplay(tbl.getDbSchema().getSchemaName() + "." + tbl.getTableName() + " | Db: " + tbl.getDbSchema().getPostgresDB().getTitle());
            return dt;
        }).collect(Collectors.toList());

        // Add schemas
        Map<DbSchema, List<PostgresTable>> schemaTables = tbls.stream().collect(Collectors.groupingBy(PostgresTable::getDbSchema));
        schemaTables.forEach((schema, tables) -> {
                DependencyRootDto dt = new DependencyRootDto();
                dt.setId(schema.getId());
                dt.setIsSchema(true);
                dt.setDisplay(schema.getSchemaName() + " | Db: " + schema.getPostgresDB().getTitle());
                res.add(dt);
        });

        return res;
    }

    private List<DependencyRootDto> querySingleActorRoots(){
        List<SingleActor> sas = dataManager.load(SingleActor.class)
                .query("select s from simiProduct_SingleActor s")
                .view("dependencyRoot-singleActor")
                .list();

        List<DependencyRootDto> res = sas.stream().map(sa -> {
            DependencyRootDto dt = new DependencyRootDto();
            dt.setId(sa.getId());
            dt.setDisplay(sa.getDerivedIdentifier() + " | " + sa.getTitle());
            return dt;
        }).collect(Collectors.toList());

        return res;
    }
}