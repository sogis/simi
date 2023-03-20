package ch.so.agi.simi.core.dependency.v1;

public class TableNotFoundException extends RuntimeException {
    public TableNotFoundException(String msg){
        super(msg);
    }
}
