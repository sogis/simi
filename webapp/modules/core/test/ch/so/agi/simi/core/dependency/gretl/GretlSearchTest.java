package ch.so.agi.simi.core.dependency.gretl;

import ch.so.agi.simi.core.dependency.DependencyInfo;
import org.junit.jupiter.api.*;

import java.text.MessageFormat;
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

    private static final String TABLE_EXISTING = "simi_autotest_table";
    private static final String TABLE_NONEXISTING = UUID.randomUUID().toString();
    private static final String DUMMY_TOKEN = "DUMMY_TOKEN";

    @BeforeEach
    void beforeEach(){
        System.setProperty(GretlSearch.SIMI_GITSEARCH_TOKEN_PROP, DUMMY_TOKEN); // replace with real token to run tests
    }

    @Test
    void searchExistingTherm_Success(){
        assumeRealTokenPresent();

        List<String> di = GretlSearch.loadGretlDependencies(TABLE_EXISTING, CONF_VALID);

        Assertions.assertEquals(
                12,
                di.size(),
                "Size must be 12. Therms must be found in all 6 files, with the test repo being configured twice. 6 Files * 2 Repos = 12"
        );
    }

    @Test
    void searchMissingTherm_EmptyList(){
        assumeRealTokenPresent();

        List<String> di = GretlSearch.loadGretlDependencies(TABLE_NONEXISTING, CONF_VALID);

        Assertions.assertEquals(
                0,
                di.size(),
                "Size must be 0. UUID search therms are not part of the test repo"
        );
    }

    @Test
    void searchInvalidRepo_RuntimeException(){
        assumeRealTokenPresent();

        Assertions.assertThrows(RuntimeException.class, () -> {
            GretlSearch.loadGretlDependencies(TABLE_NONEXISTING, CONF_INVALID_REPO);
        });
    }


    @Test
    void searchInvalidUrl_RuntimeException(){
        assumeRealTokenPresent();

        Assertions.assertThrows(RuntimeException.class, () -> {
            GretlSearch.loadGretlDependencies(TABLE_NONEXISTING, CONF_INVALID_URL);
        });
    }

    @Test
    void searchInvalidToken_Throws(){
        System.setProperty(GretlSearch.SIMI_GITSEARCH_TOKEN_PROP, "dummy");

        Assertions.assertThrows(Exception.class, () -> {
            GretlSearch.loadGretlDependencies(TABLE_EXISTING, CONF_VALID);
        });
    }

    private void assumeRealTokenPresent(){
        Assumptions.assumeFalse(
                DUMMY_TOKEN.equals(System.getProperty(GretlSearch.SIMI_GITSEARCH_TOKEN_PROP)),
                MessageFormat.format("System Property '{0}' must be set to run test", GretlSearch.SIMI_GITSEARCH_TOKEN_PROP)
        );
    }
}
