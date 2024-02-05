package ch.so.agi.simi.global.validation;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

public class JsonObjectFieldUtil implements ConstraintValidator<JsonObjectField, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void initialize(JsonObjectField constraintAnnotation) {}

    @Override
    public boolean isValid(String fieldContent, ConstraintValidatorContext context) {
        return isValidImpl(fieldContent);
    }

    //Spezialfall nested arrays validiert auch (Ist nicht abgefangen)
    static boolean isValidImpl(String fieldContent){
        if(fieldContent == null)
            return true;

        boolean wellFormed = false;

        try{
            mapper.readTree(fieldContent);

            wellFormed = fieldContent.startsWith("{");
        }
        catch(IOException e){}

        return wellFormed;
    }
}
