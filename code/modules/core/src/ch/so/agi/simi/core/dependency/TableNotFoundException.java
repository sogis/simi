package ch.so.agi.simi.core.dependency;

public class TableNotFoundException extends RuntimeException {
    public TableNotFoundException(String msg){
        super(msg);
    }
}
