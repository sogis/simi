package ch.so.agi.simi.service.dependency;

import ch.so.agi.simi.SimiTestContainer;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.UUID;

// See https://doc.cuba-platform.com/manual-7.2/integration_tests_mw.html

class DependencyQueryTest {

    @RegisterExtension
    static TestContainer container = SimiTestContainer.Common.INSTANCE;

    static Resources resources;

    @BeforeAll
    static void beforeAll() {
        resources = AppBeans.get(Resources.class);
    }

    @Test
    void queryDependencies_throwsNoException() {

        UUID dataSetId = UUID.randomUUID();

        Assertions.assertDoesNotThrow(() -> {
            DependencyQuery.execute(dataSetId, container.persistence(), resources);
        });
    }
}