package ch.so.agi.simi.global.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

public class JsonFieldValidator implements ConstraintValidator<JsonField, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void initialize(JsonField constraintAnnotation) {}

    @Override
    public boolean isValid(String fieldContent, ConstraintValidatorContext context) {

        if(fieldContent == null)
            return true;

        boolean wellFormed = false;

        try{
            mapper.readTree(fieldContent);
            wellFormed = true;
        }
        catch(IOException e){}

        return wellFormed;
    }
}