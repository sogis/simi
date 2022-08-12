package ch.so.agi.simi.global.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static ch.so.agi.simi.global.validation.JsonArrayUtil.*;

class JsonArrayUtilTest {

    @Test
    void isValid_True_for_Array(){
        boolean res = isValidImpl("[\"fuu\",\"bar\"]");
        assertTrue(res, "Json array must validate");
    }

    @Test
    void isValid_True_for_NULL(){
        boolean res = isValidImpl(null);
        assertTrue(res, "NULL-value must validate");
    }

    @Test
    void isValid_True_for_NestedArray(){
        boolean res = isValidImpl("[\"fuu\",\"bar\"]");
        assertTrue(res, "Json array must validate");
    }

    @Test
    void isValid_False_for_NonJson(){
        boolean res = isValidImpl("fuu");
        assertFalse(res, "Non Json-parseable String must not validate");
    }

    @Test
    void isValid_False_for_JsonObj(){
        boolean res = isValidImpl("{\"fuu\":\"bar\"}");
        assertFalse(res, "Json-Object must not validate");
    }

    @Test
    void tryParse_Delimited_OK(){
        boolean semiSplits = delimiterSplits(",");
        boolean semi1Splits = delimiterSplits(";");
        boolean pointDoesNotSplit = !delimiterSplits(".");
        assertTrue(semiSplits && semi1Splits && pointDoesNotSplit, ", and ; must split. A . must not split");
    }

    private boolean delimiterSplits(String delim){
        String test = "a,b".replace("," ,delim);
        String res = tryConvertToJsonArray(test);

        if(!isValidImpl(res))
            throw new RuntimeException("Split result is not a valid json array");

        return res.length() == 9; // ["a","b"]
    }

    @Test
    void tryParse_NULL_OK(){
        String res = tryConvertToJsonArray(null);
        assertNull(res, "Converting null must yield null");
    }

    @Test
    void tryParse_JsonArray_OK(){
        String input = "[\"fuu\",\"bar\"]";
        String res = tryConvertToJsonArray(input);
        assertEquals(input, res, "Json-Array input must not be converted");
    }

    @Test
    void tryParse_SingleElem_OK(){
        String input = "a";
        String res = tryConvertToJsonArray(input);

        boolean valid = isValidImpl(res);
        boolean isArray = (res.length() == 5);

        assertTrue(valid && isArray, "Result must be a valid json array with one element");
    }
}