package ch.so.agi.simi.service.dependency;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

public class GretlSearchTest {

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

    private static final String[] SCHEMA_TABLE_EXISTING = new String[]{"simi_autotest_schema","simi_autotest_table"};
    private static final String[] SCHEMA_TABLE_NONEXISTING = new String[]{UUID.randomUUID().toString(),UUID.randomUUID().toString()};

    @Test
    public void searchExistingTherm_Success(){
        List<DependencyInfo> di = GretlSearch.queryGretlDependencies(SCHEMA_TABLE_EXISTING[0], SCHEMA_TABLE_EXISTING[1], CONF_VALID);

        Assert.assertEquals(
                "Size must be 12. Therms must be found in all 6 files, with the test repo being configured twice. 6 Files * 2 Repos = 12",
                12,
                di.size()
        );
    }

    @Test
    public void searchMissingTherm_EmptyList(){
        List<DependencyInfo> di = GretlSearch.queryGretlDependencies(SCHEMA_TABLE_NONEXISTING[0], SCHEMA_TABLE_NONEXISTING[1], CONF_VALID);

        Assert.assertEquals(
                "Size must 0. UUID search therms are not part of the test repo",
                0,
                di.size()
        );
    }

    @Test
    public void searchInvalidRepo_RuntimeException(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            GretlSearch.queryGretlDependencies(SCHEMA_TABLE_NONEXISTING[0], SCHEMA_TABLE_NONEXISTING[1], CONF_INVALID_REPO);
        });
    }

    @Test
    public void searchInvalidUrl_RuntimeException(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            GretlSearch.queryGretlDependencies(SCHEMA_TABLE_NONEXISTING[0], SCHEMA_TABLE_NONEXISTING[1], CONF_INVALID_URL);
        });
    }

}
