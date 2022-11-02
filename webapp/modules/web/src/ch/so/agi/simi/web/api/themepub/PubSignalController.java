package ch.so.agi.simi.web.api.themepub;

import ch.so.agi.simi.core.dependency.DependencyService;
import ch.so.agi.simi.core.service.pub.PublishResult;
import ch.so.agi.simi.core.service.pub.UpdatePublishTimeService;
import ch.so.agi.simi.global.exc.CodedException;
import ch.so.agi.simi.web.api.themepub.dto.ThemePubDto;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.inject.Inject;
import javax.validation.*;
import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("pubsignal")
public class PubSignalController {

    private static Logger log = LoggerFactory.getLogger(PubSignalController.class);

    @Inject
    private UpdatePublishTimeService coreService;

    @GetMapping()
    public ResponseEntity<@Valid ThemePubDto> get() {
        ThemePubDto pub = null;

        if(Math.random() > 0.5) {
            pub = new ThemePubDto(
                    "ch.so.agi.av.mopublic",
                    List.of("224", "225"),
                    ZonedDateTime.now().format( DateTimeFormatter.ISO_LOCAL_DATE_TIME )
            );
        }
        else {
            pub = new ThemePubDto("ch.so.afu.gewaesserschutz", null, ZonedDateTime.now().format( DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        return ResponseEntity.ok(pub);
    }

    @PutMapping()
    public ResponseEntity put(
            @RequestBody String jsonBody) {

        ResponseEntity res = null;

        log.info("Submitted info: {}", jsonBody);

        try{
            PublishResult pRes = coreService.update(jsonBody);
            res = ResponseEntity.ok().body(
                    MessageFormat.format(
                            "Updated {0}. Executed {1} db insert(s) and {2} db update(s).",
                            pRes.getThemePubIdentifier(),
                            pRes.getDbInsertCount(),
                            pRes.getDbUpdateCount())
            );
        }
        catch (CodedException ce){
            String msg = MessageFormat.format("{0}.", ce.getMessage());

            if(ce.getDetailMessage() != null)
                msg += MessageFormat.format(" {0}.", ce.getDetailMessage());

            if(ce.getInnerExceptionType() != null)
                msg += MessageFormat.format(" Inner Exception: {0}", ce.getInnerExceptionType());

            if(ce.getInnerExceptionMessage() != null)
                msg += MessageFormat.format(" Inner exc. message: {0}", ce.getInnerExceptionMessage());

            res = ResponseEntity.status(ce.getErrorCode()).body(msg);
        }
        catch(Exception e){
            res = ResponseEntity.badRequest().body(e.getMessage());
        }

        return res;
    }
}
