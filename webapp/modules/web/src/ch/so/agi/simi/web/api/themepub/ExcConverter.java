package ch.so.agi.simi.web.api.themepub;

import ch.so.agi.simi.global.exc.CodedException;
import org.springframework.http.ResponseEntity;

import java.text.MessageFormat;

public class ExcConverter {
    public static ResponseEntity toResponse(Exception e){
        ResponseEntity res = null;

        if(e instanceof CodedException){
            CodedException ce = (CodedException)e;

            String msg = MessageFormat.format("{0}.", ce.getMessage());

            if(ce.getDetailMessage() != null)
                msg += MessageFormat.format(" {0}.", ce.getDetailMessage());

            if(ce.getInnerExceptionType() != null)
                msg += MessageFormat.format(" Inner Exception: {0}", ce.getInnerExceptionType());

            if(ce.getInnerExceptionMessage() != null)
                msg += MessageFormat.format(" Inner exc. message: {0}", ce.getInnerExceptionMessage());

            res = ResponseEntity.status(ce.getErrorCode()).body(msg);
        }
        else{
            res = ResponseEntity.status(500).body(e.getMessage());
        }

        return res;
    }
}
