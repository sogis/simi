package ch.so.agi.simi.web.api.themepub;

import ch.so.agi.simi.core.service.pub.GenerateThemePubDocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("doc")
public class DataDocController {

    private static Logger log = LoggerFactory.getLogger(DataDocController.class);

    @Inject
    private GenerateThemePubDocService coreService;

    @GetMapping()
    public ResponseEntity<String> get(@NotEmpty @RequestParam String dataident) {

        ResponseEntity res = null;

        try{
            String generatedDoc = coreService.generateDoc(dataident);

            res = ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(generatedDoc)
            ;

            log.info("Returned doc for identifier: {}", dataident);
        }
        catch(Exception e){
            res = ExcConverter.toResponse(e);
        }

        return res;
    }
}

