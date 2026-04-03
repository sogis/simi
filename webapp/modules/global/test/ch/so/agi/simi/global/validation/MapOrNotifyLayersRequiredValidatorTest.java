package ch.so.agi.simi.global.validation;

import org.junit.jupiter.api.Test;

import static ch.so.agi.simi.global.validation.MapOrNotifyLayersRequiredValidator.isValidImpl;
import static org.junit.jupiter.api.Assertions.*;

class MapOrNotifyLayersRequiredValidatorTest {

    private static final Object SOME_MAP = new Object();

    @Test
    void valid_when_map_is_set() {
        assertTrue(isValidImpl(SOME_MAP, null));
    }

    @Test
    void valid_when_map_is_set_and_notifyLayers_has_no_layers_key() {
        assertTrue(isValidImpl(SOME_MAP, "{\"foo\":\"bar\"}"));
    }

    @Test
    void valid_when_map_null_and_notifyLayers_has_layers_key() {
        assertTrue(isValidImpl(null, "{\"layers\":[\"a\",\"b\"]}"));
    }

    @Test
    void invalid_when_map_null_and_notifyLayers_null() {
        assertFalse(isValidImpl(null, null));
    }

    @Test
    void invalid_when_map_null_and_notifyLayers_has_no_layers_key() {
        assertFalse(isValidImpl(null, "{\"foo\":\"bar\"}"));
    }

    @Test
    void invalid_when_map_null_and_notifyLayers_invalid_json() {
        assertFalse(isValidImpl(null, "not-json"));
    }

    @Test
    void invalid_when_map_null_and_notifyLayers_is_json_array() {
        assertFalse(isValidImpl(null, "[\"layers\"]"));
    }

    @Test
    void invalid_when_both_map_and_layers_are_set() {
        assertFalse(isValidImpl(SOME_MAP, "{\"layers\":[\"a\",\"b\"]}"));
    }
}
