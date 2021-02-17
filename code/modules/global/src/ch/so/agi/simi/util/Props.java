package ch.so.agi.simi.util;

public class Props {

    // Annotation to directly create Lists from property does not work as expected....
    public static String[] toArray(String commaSeparated){
        String[] parts = commaSeparated.split(",");
        return parts;
    }
}
