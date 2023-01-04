package ch.so.agi.simi.entity.theme;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/*
    Used praefix in method names:
    Pt = PreviousType
    Ps = PreviousSuffix
    Ct = CurrentType
 */
class ThemePublication_TypeEnumTest {

    private static final ThemePublication_TypeEnum NULL_DEFAULT = ThemePublication_TypeEnum.TABLE_SIMPLE;
    private static final ThemePublication_TypeEnum NON_NULL_DEFAULT = ThemePublication_TypeEnum.TABLE_RELATIONAL;

    @BeforeAll
    public static void assertTypDefaultResults(){
        Assertions.assertNotNull(NON_NULL_DEFAULT.getDefaultSuffix(), "Used DEFAULT_NON_NULL type must yield non null default suffix");
        Assertions.assertNull(NULL_DEFAULT.getDefaultSuffix(), "Used DEFAULT_NULL type must yield default suffix with value 'null'");
    }

    @Test
    public void adaptSuffixToTypeChange_PtNull_PsNull_CtWithDefault() {
        String newSuffix = ThemePublication_TypeEnum.adaptSuffixToTypeChange(null, null, NON_NULL_DEFAULT);
        Assertions.assertEquals(NON_NULL_DEFAULT.getDefaultSuffix(), newSuffix);
    }

    @Test
    public void adaptSuffixToTypeChange_PtNull_PsNull_CtWithNullDefault() {
        String newSuffix = ThemePublication_TypeEnum.adaptSuffixToTypeChange(null, null, NULL_DEFAULT);
        Assertions.assertNull(newSuffix);
    }

    @Test
    public void adaptSuffixToTypeChange_PsNull_PtWithNullDefault_CtWithNullDefault() {
        String newSuffix = ThemePublication_TypeEnum.adaptSuffixToTypeChange(NULL_DEFAULT, null, NULL_DEFAULT);
        Assertions.assertNull(newSuffix);
    }

    @Test
    public void adaptSuffixToTypeChange_PsNull_PtWithNullDefault_CtWithDefault() {
        String newSuffix = ThemePublication_TypeEnum.adaptSuffixToTypeChange(NULL_DEFAULT, null, NON_NULL_DEFAULT);
        Assertions.assertEquals(NON_NULL_DEFAULT.getDefaultSuffix(), newSuffix);
    }

    @Test
    public void adaptSuffixToTypeChange_PsNull_PtWithDefault_CtWithNullDefault() {
        String newSuffix = ThemePublication_TypeEnum.adaptSuffixToTypeChange(NON_NULL_DEFAULT, null, NULL_DEFAULT);
        Assertions.assertNull(newSuffix);
    }

    @Test
    public void adaptSuffixToTypeChange_PsNull_PtWithDefault_CtWithDefault() {
        String newSuffix = ThemePublication_TypeEnum.adaptSuffixToTypeChange(NON_NULL_DEFAULT, null, NULL_DEFAULT);
        Assertions.assertNull(newSuffix);
    }

    @Test
    public void adaptSuffixToTypeChange_PsMatching_PtWithDefault_CtWithDefault() { // Test is shaky as it would require second Type yielding different default suffix
        String newSuffix = ThemePublication_TypeEnum.adaptSuffixToTypeChange(NON_NULL_DEFAULT, NON_NULL_DEFAULT.getDefaultSuffix(), NON_NULL_DEFAULT);
        Assertions.assertEquals(newSuffix, NON_NULL_DEFAULT.getDefaultSuffix());
    }

    @Test
    public void adaptSuffixToTypeChange_PsMatching_PtWithDefault_CtWithNullDefault() {
        String newSuffix = ThemePublication_TypeEnum.adaptSuffixToTypeChange(NON_NULL_DEFAULT, null, NULL_DEFAULT);
        Assertions.assertNull(newSuffix);
    }

    @Test
    public void adaptSuffixToTypeChange_PsNonMatching_PtWithDefault_CtWithNullDefault() {
        String prevSuffix = "prevSuffixVal";

        String newSuffix = ThemePublication_TypeEnum.adaptSuffixToTypeChange(NON_NULL_DEFAULT, prevSuffix, NULL_DEFAULT);
        Assertions.assertEquals(prevSuffix, newSuffix);
    }

    @Test
    public void adaptSuffixToTypeChange_PsNonMatching_PtWithDefault_CtWithDefault() {
        String prevSuffix = "prevSuffixVal";

        String newSuffix = ThemePublication_TypeEnum.adaptSuffixToTypeChange(NON_NULL_DEFAULT, prevSuffix, NON_NULL_DEFAULT);
        Assertions.assertEquals(prevSuffix, newSuffix);
    }

    @Test
    public void adaptSuffixToTypeChange_PsNonMatching_PtWithNullDefault_CtWithDefault() {
        String prevSuffix = "prevSuffixVal";

        String newSuffix = ThemePublication_TypeEnum.adaptSuffixToTypeChange(NULL_DEFAULT, prevSuffix, NON_NULL_DEFAULT);
        Assertions.assertEquals(prevSuffix, newSuffix);
    }

    @Test
    public void adaptSuffixToTypeChange_PsNonMatching_PtWithNullDefault_CtWithNullDefault() {
        String prevSuffix = "prevSuffixVal";

        String newSuffix = ThemePublication_TypeEnum.adaptSuffixToTypeChange(NULL_DEFAULT, prevSuffix, NULL_DEFAULT);
        Assertions.assertEquals(prevSuffix, newSuffix);
    }
}