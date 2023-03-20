package ch.so.agi.simi.core.dependency;

import ch.so.agi.simi.SimiTestContainer;
import ch.so.agi.simi.entity.dependency.DependencyForTest;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DependencyRootServiceBeanTest {
    @RegisterExtension
    static TestContainer container = SimiTestContainer.Common.INSTANCE;

    static DependencyRootService serviceBean;

    @BeforeAll
    static void beforeAll() {
        serviceBean = AppBeans.get(DependencyRootService.class);
    }

    @Test
    void queryRoots_OK(){
        serviceBean.queryRoots();
    }

}