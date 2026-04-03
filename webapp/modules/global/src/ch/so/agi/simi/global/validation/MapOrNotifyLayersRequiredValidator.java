package ch.so.agi.simi.global.validation;

import ch.so.agi.simi.entity.extended.CCCIntegration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

public class MapOrNotifyLayersRequiredValidator implements ConstraintValidator<MapOrNotifyLayersRequired, CCCIntegration> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void initialize(MapOrNotifyLayersRequired constraintAnnotation) {}

    @Override
    public boolean isValid(CCCIntegration value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return isValidImpl(value.getMap(), value.getNotifyLayers());
    }

    static boolean isValidImpl(Object map, String notifyLayers) {
        boolean hasMap = map != null;
        boolean hasLayers = hasLayersKey(notifyLayers);
        return hasMap ^ hasLayers; // XOR: genau eine der beiden Angaben muss gesetzt sein
    }

    private static boolean hasLayersKey(String notifyLayers) {
        if (notifyLayers == null) {
            return false;
        }
        try {
            JsonNode node = mapper.readTree(notifyLayers);
            return node.isObject() && node.has("layers");
        } catch (IOException e) {
            return false;
        }
    }
}
