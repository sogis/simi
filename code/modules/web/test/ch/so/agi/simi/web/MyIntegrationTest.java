package ch.so.agi.simi.web;

import ch.so.agi.simi.SimiWebTestContainer;
import com.haulmont.cuba.web.testsupport.TestUiEnvironment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.*;

// See https://doc.cuba-platform.com/manual-7.2/integration_tests_client.html

class MyIntegrationTest {

    @RegisterExtension
    TestUiEnvironment environment = new TestUiEnvironment(new SimiWebTestContainer()).withUserLogin("admin");

    @Test
    void myTestMethod() {

    }
}