package ch.so.agi.simi.core.dependency;

import ch.so.agi.simi.SimiTestContainer;
import ch.so.agi.simi.entity.dependency.*;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

class DependencyListServiceBeanTest {

    /*
    Intentionally empty, as code to collect simi and gretl dependencies is covered in the corresponding test classes
    and rhe remaining code in DependencyListServiceBean is little with small probabibility for errors -> not test covered.
    */
}

