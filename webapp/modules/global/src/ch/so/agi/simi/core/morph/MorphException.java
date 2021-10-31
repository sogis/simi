package ch.so.agi.simi.core.morph;

public class MorphException extends RuntimeException {

    public MorphException(String msg){
        super(msg);
    }

    public MorphException(String msg, Exception inner){
        super(msg, inner);
    }
}
