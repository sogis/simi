package ch.so.agi.simi.global.validation;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

public class JsonArrayUtil implements ConstraintValidator<JsonArrayField, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void initialize(JsonArrayField constraintAnnotation) {}

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

            wellFormed = fieldContent.startsWith("[");
        }
        catch(IOException e){}

        return wellFormed;
    }

    public static String tryConvertToJsonArray(String delimited){

        if(delimited == null || delimited.startsWith("["))
            return delimited;

        String[] elem1 = delimited.split(",");
        if(elem1 == null)
            elem1 = new String[]{delimited};

        String[] elem2 = delimited.split(";");
        if(elem2 == null)
            elem2 = new String[]{delimited};

        String[] split;
        if(elem1.length > elem2.length)
            split = elem1;
        else
            split = elem2;

        for(int i=0; i<split.length; i++){
            String s = split[i];
            split[i] = "\"" + s.trim() + "\"";
        }

        String res = "[" + String.join(",", split) + "]";

        return res;
    }
}