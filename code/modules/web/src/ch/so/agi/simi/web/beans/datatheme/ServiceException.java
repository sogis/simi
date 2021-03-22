package ch.so.agi.simi.web.beans.datatheme;

public class ServiceException extends RuntimeException {
    public ServiceException(String msg){ super(msg); }
    public ServiceException(String msg, Exception inner){ super(msg, inner); }
}
