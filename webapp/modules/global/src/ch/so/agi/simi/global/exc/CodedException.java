package ch.so.agi.simi.global.exc;

import com.haulmont.cuba.core.global.SupportedByClient;

import java.util.Optional;

@SupportedByClient
public class CodedException extends RuntimeException {

    public static final String ERR_MSGBODY_EMPTY =  "Message body is empty"; //400
    public static final String ERR_MSGBODY_INVALID =  "Message body json is invalid or incomplete"; //400
    public static final String ERR_THEMEPUB_UNKNOWN = "ThemePublication reference is not known"; //404
    public static final String ERR_SUBAREA_UNKNOWN = "SubArea (region) reference is not known";  //404
    public static final String ERR_SERVER = "Processing of the reqest failed";  //500

    private int errorCode = Integer.MIN_VALUE;
    private String detailMessage;
    private String innerExceptionMessage;
    private String innerExceptionType;

    public CodedException(int errorCode, String message, String detailMessage, Exception inner){
        super(message);
        if(message == null || message.isEmpty())
            throw new SimiException("Exception msg ist mandatory");

        this.errorCode = errorCode;
        this.detailMessage = detailMessage;

        if(inner != null){
            this.innerExceptionType = inner.getClass().getName();
            this.innerExceptionMessage = inner.getMessage();
        }
    }

    public CodedException(int errorCode, String message){
        this(errorCode, message, null, null);
    }

    public CodedException(int errorCode, String message, String detailMessage){
        this(errorCode, message, detailMessage, null);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Optional<String> getDetailMessage() {
        return Optional.ofNullable(detailMessage);
    }

    public Optional<String> getInnerExceptionMessage() {
        return Optional.ofNullable(innerExceptionMessage);
    }

    public Optional<String> getInnerExceptionType() {
        return Optional.ofNullable(innerExceptionType);
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer(CodedException.class.getSimpleName())
                .append(" | msg:").append(getMessage())
                .append(" | code:").append(errorCode);

        if(getDetailMessage().isPresent())
            buf = buf.append(" | detail:").append(getDetailMessage().get());

        if(getInnerExceptionType().isPresent())
            buf = buf.append(" | innerExType:").append(getInnerExceptionType().get());

        if(getInnerExceptionMessage().isPresent())
            buf = buf.append(" | innerExMsg:").append(getInnerExceptionMessage().get());

        return buf.append(" |").toString();
    }
}
