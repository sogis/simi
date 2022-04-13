package ch.so.agi.simi.global.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;


public class JsonFieldValidatorTest {

    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void nullValue_OK() {

        JsonFieldsMock cls = new JsonFieldsMock();
        cls.nullString = null;

        Assertions.assertTrue(validates(cls));
    }

    @Test
    void wellFormedJson_OK() {

        JsonFieldsMock cls = new JsonFieldsMock();
        cls.wellFormedJson = "{}";

        Assertions.assertTrue(validates(cls));
    }

    @Test
    void nonJsonString_NotOK() {

        JsonFieldsMock cls = new JsonFieldsMock();
        cls.nonJsonString = "{";

        Assertions.assertFalse(validates(cls));
    }

    private static boolean validates(JsonFieldsMock mock){
        Set<ConstraintViolation<JsonFieldsMock>> violations = validator.validate(mock);

        return (violations == null || violations.size() == 0);
    }
}
