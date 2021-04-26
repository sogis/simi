package ch.so.agi.simi.core.entity;

public class ViewLoadException extends RuntimeException {

    public String viewName;

    public ViewLoadException(String viewName, Exception inner){
        super("Exception when loading view '" + viewName + "'", inner);
        this.viewName = viewName;
    }
}
