package ch.so.agi.simi.core.dependency.v1;

import ch.so.agi.simi.SimiTestContainer;
import ch.so.agi.simi.core.dependency.v1.TableNameQuery;
import ch.so.agi.simi.core.dependency.v1.TableNotFoundException;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.UUID;

// See https://doc.cuba-platform.com/manual-7.2/integration_tests_mw.html

class TableNameQueryTest {

    @RegisterExtension
    static TestContainer container = SimiTestContainer.Common.INSTANCE;

    static DataManager dataManager;

    @BeforeAll
    static void beforeAll() {
        dataManager = AppBeans.get(DataManager.class);
    }

    @Test
    void queryFullTableName_throwsTableNotFoundException() {

        UUID dataSetId = UUID.randomUUID();

        Assertions.assertThrows(TableNotFoundException.class ,() -> {
            TableNameQuery.execute(UUID.randomUUID(), dataManager);
        });
    }
}