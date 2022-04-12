package ch.so.agi.simi.global.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;


public class JsonFieldsValidatorTest {

    private static Validator validator;

    @JsonFields(fields = "field")
    class NullValueCls extends StringFieldMock{};

    @JsonFields(fields = "nonExistingFieldName")
    class WrongAnnotatedCls extends StringFieldMock{};

    @JsonFields(fields = "field")
    class JsonValueCls extends StringFieldMock{};

    @BeforeAll
    static void beforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void nullValue_OK() {

        StringFieldMock cls = new JsonFieldsValidatorTest.NullValueCls();
        cls.setFieldValue(null);

        Assertions.assertTrue(validates(cls));
    }

    @Test
    void wrongFieldName_ThrowsException() {
        StringFieldMock cls = new JsonFieldsValidatorTest.WrongAnnotatedCls();
        cls.setFieldValue("fuu");

        Assertions.assertThrows(Exception.class, () -> validates(cls));
    }

    @Test
    void jsonValue_OK() {
        StringFieldMock cls = new JsonFieldsValidatorTest.JsonValueCls();
        cls.setFieldValue("jsooon");

        Assertions.assertTrue(validates(cls));
    }

    @Test
    void stringValue_NotOK() {

    }

    private static boolean validates(StringFieldMock mock){
        Set<ConstraintViolation<StringFieldMock>> violations = validator.validate(mock);

        return (violations == null || violations.size() == 0);
    }
}
