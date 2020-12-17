package ch.so.agi.simi.service.dependency;

public class TableNotFoundException extends RuntimeException {
    public TableNotFoundException(String msg){
        super(msg);
    }
}
