package ch.so.agi.simi.core.dependency.gretl;

import ch.so.agi.simi.core.dependency.DependencyInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

public class GretlSearchV1Test {

    private static final GretlSearchConfigForTest CONF_VALID  = new GretlSearchConfigForTest(
            "https://api.github.com/search/code",
            "simi-so/autotest_datadependency,simi-so/autotest_datadependency"
    );

    private static final GretlSearchConfigForTest CONF_INVALID_URL  = new GretlSearchConfigForTest(
            "https://api.github.com__/search/code",
            "simi-so/autotest_datadependency"
    );

    private static final GretlSearchConfigForTest CONF_INVALID_REPO  = new GretlSearchConfigForTest(
            "https://api.github.com/search/code",
            "simi-so/autotest_datadependency__"
    );

    private static final String[] TABLE_EXISTING = new String[]{"simi_autotest_schema","simi_autotest_table"};
    private static final String[] TABLE_NONEXISTING = new String[]{UUID.randomUUID().toString(),UUID.randomUUID().toString()};

    //@Test
    public void searchExistingTherm_Success_v1(){
        List<DependencyInfo> di = GretlSearchV1.queryGretlDependencies(TABLE_EXISTING, CONF_VALID);

        Assertions.assertEquals(
                12,
                di.size(),
                "Size must be 12. Therms must be found in all 6 files, with the test repo being configured twice. 6 Files * 2 Repos = 12"
        );
    }

    //@Test
    public void searchExistingTherm_Success(){
        List<String> di = GretlSearchV1.loadGretlDependencies(TABLE_EXISTING, CONF_VALID);

        Assertions.assertEquals(
                12,
                di.size(),
                "Size must be 12. Therms must be found in all 6 files, with the test repo being configured twice. 6 Files * 2 Repos = 12"
        );
    }

    //@Test
    public void searchMissingTherm_EmptyList_V1(){
        List<DependencyInfo> di = GretlSearchV1.queryGretlDependencies(TABLE_NONEXISTING, CONF_VALID);

        Assertions.assertEquals(
                0,
                di.size(),
                "Size must be 0. UUID search therms are not part of the test repo"
        );
    }

    //@Test
    public void searchMissingTherm_EmptyList(){
        List<String> di = GretlSearchV1.loadGretlDependencies(TABLE_NONEXISTING, CONF_VALID);

        Assertions.assertEquals(
                0,
                di.size(),
                "Size must be 0. UUID search therms are not part of the test repo"
        );
    }

    //@Test
    public void searchInvalidRepo_RuntimeException_V1(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            GretlSearchV1.queryGretlDependencies(TABLE_NONEXISTING, CONF_INVALID_REPO);
        });
    }

    //@Test
    public void searchInvalidRepo_RuntimeException(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            GretlSearchV1.loadGretlDependencies(TABLE_NONEXISTING, CONF_INVALID_REPO);
        });
    }

    //@Test
    public void searchInvalidUrl_RuntimeException_V1(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            GretlSearchV1.queryGretlDependencies(TABLE_NONEXISTING, CONF_INVALID_URL);
        });
    }

    //@Test
    public void searchInvalidUrl_RuntimeException(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            GretlSearchV1.loadGretlDependencies(TABLE_NONEXISTING, CONF_INVALID_URL);
        });
    }
}
