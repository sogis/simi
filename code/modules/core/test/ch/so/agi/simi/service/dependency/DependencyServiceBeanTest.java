package ch.so.agi.simi.service.dependency;

import ch.so.agi.simi.SimiTestContainer;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

// See https://doc.cuba-platform.com/manual-7.2/integration_tests_mw.html

class DependencyServiceBeanTest {

    @RegisterExtension
    static TestContainer container = SimiTestContainer.Common.INSTANCE;
    static DataManager dataManager;

    static DependencyServiceBean bean;

    @BeforeAll
    static void beforeAll() {
        dataManager = AppBeans.get(DataManager.class);
        bean = AppBeans.get(DependencyServiceBean.class);
    }

    @Test
    void tableByName_() {

    }


    /*
Es muss nur der Sonnenpfad getestet werden
- Keine Fehlermeldungen beim Ausführen des query auf die DB (etwa durch schemaänderungen)
- Keine Fehlermeldungen beim Abfragen des github search repo
     */
}