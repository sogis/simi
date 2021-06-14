package ch.so.agi.simi.core.entity;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;


public class EntityLoadException extends RuntimeException {

    private ArrayList<ViewLoadException> viewExceptions;

    public EntityLoadException(ArrayList<ViewLoadException> viewExceptions){
        super(buildErrorMsg(viewExceptions));
        this.viewExceptions = viewExceptions;
    }

    private static String buildErrorMsg(ArrayList<ViewLoadException> viewExceptions){
        String msg = viewExceptions.size() + " Exception(s) when loading entity with view(s): ";
        String delim = ", ";
        for(ViewLoadException ex : viewExceptions){
            msg = msg + ex.viewName + delim;
        }
        msg = msg.substring(0, msg.length() - delim.length());

        return msg;
    }

    @Override
    public void printStackTrace(){
        super.printStackTrace();

        int num = 1;
        for (ViewLoadException te : viewExceptions) {
            System.err.println(MessageFormat.format("ViewLoadException {0}:", num));
            te.printStackTrace();

            num++;
        }
    }
}
