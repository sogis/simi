package ch.so.agi.simi.web.api.themepub;

import ch.so.agi.simi.core.service.pub.PublishResult;
import ch.so.agi.simi.core.service.pub.UpdatePublishTimeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.inject.Inject;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("pubsignal")
public class PubSignalController {

    private static Logger log = LoggerFactory.getLogger(PubSignalController.class);

    @Inject
    private UpdatePublishTimeService coreService;

    @PutMapping()
    public ResponseEntity put(
            @RequestBody String jsonBody) {

        ResponseEntity res = null;

        log.info("Submitted info: {}", jsonBody);

        try{
            PublishResult pRes = coreService.update(jsonBody);
            String msg = MessageFormat.format(
                    "Updated {0}. Executed {1} db insert(s) and {2} db update(s).",
                    pRes.getThemePubIdentifier(),
                    pRes.getDbInsertCount(),
                    pRes.getDbUpdateCount());

            log.info(msg);
            res = ResponseEntity.ok().body(msg);
        }
        catch(Exception e){
            res = ExcConverter.toResponse(e);
        }

        return res;
    }
}
