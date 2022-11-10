package ch.so.agi.simi.web.api.themepub;

import ch.so.agi.simi.core.service.pub.GenerateThemePubDocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("doc")
public class DataDocController {

    private static Logger log = LoggerFactory.getLogger(DataDocController.class);

    @Inject
    private GenerateThemePubDocService coreService;

    //@GetMapping
    /*
    public ResponseEntity<String> get(HttpServletRequest request) {
        log.debug("Request: {}", request);
        log.debug("User: {}", request.getUserPrincipal());
        log.debug("QueryString: {}", request.getQueryString());

        Iterator<String> iter = request.getHeaderNames().asIterator();
        while(iter.hasNext()){
            String headerKey = iter.next();
            log.debug("Header: {}={}", headerKey, request.getHeader(headerKey));
        }

        return returnUnderConstruction("fuu");
    }*/

    @GetMapping()
    public ResponseEntity<String> get(@NotEmpty @RequestParam String dataident, @RequestHeader Map<String, String> headers) {

        log.debug("Dataident = {}", dataident);

        headers.forEach((key, value) -> {
                    if(value.toLowerCase().contains("bearer")){
                        String newVal = "Bearer" + " ***" + value.substring(value.length() - 4);
                        value = newVal;
                    }

                    log.debug(String.format("Header '%s' = %s", key, value));
                });

        ResponseEntity res = null;

        res = returnUnderConstruction(dataident);
        /*
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
            log.info("Generation of data-doc failed: {}", e.getMessage());
        }*/

        return res;
    }

    private static ResponseEntity returnUnderConstruction(String dataident){
        String template = "<!DOCTYPE html>\n" +
                "<html lang=\"de\">\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Datadoc-Mock</title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <h1>Datadoc von {0}</h1>\n" +
                "    <p>Pendent: Authentication für /doc schlägt fehl</p>\n" +
                "  </body>\n" +
                "</html>";

        String body = MessageFormat.format(template, dataident);

        return ResponseEntity.ok().body(body);
    }
}

