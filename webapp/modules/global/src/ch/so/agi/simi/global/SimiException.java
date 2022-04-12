package ch.so.agi.simi.global;

import java.text.MessageFormat;

public class SimiException extends RuntimeException {

    public SimiException(String msg){ super(msg); }

    public SimiException(Exception wrapped){ super(wrapped); }

    public SimiException(String msg, Object... msgValues){
        super(MessageFormat.format(msg, msgValues));
    }

    public SimiException(Exception inner, String msg, Object... msgValues){
        super(MessageFormat.format(msg, msgValues), inner);
    }
}


