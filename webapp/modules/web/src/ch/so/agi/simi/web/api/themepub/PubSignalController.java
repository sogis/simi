package ch.so.agi.simi.web.api.themepub;

import ch.so.agi.simi.web.api.themepub.dto.ThemePubDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.validation.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("simi/themepub/pubsignal")
public class PubSignalController {

    private static Logger log = LoggerFactory.getLogger(PubSignalController.class);

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
            @RequestBody ThemePubDto pub) {

        //OffsetDateTime odt = OffsetDateTime.parse( pub.getPublished() );

        log.info("Submitted info: {}", pub);

        ResponseEntity res = null;

        try {
            assertValid(pub);
            res = ResponseEntity.ok(HttpStatus.OK);
        }
        catch(RuntimeException r){
            res = ResponseEntity.badRequest().body(r.getMessage());
        }

        return res;
    }

    private static void assertValid(ThemePubDto pub){
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<ThemePubDto>> violations = validator.validate(pub);

        if(violations != null && violations.size() > 0){
            String innerMessages = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(" | "));
            throw new RuntimeException("Validation error(s): " + innerMessages);
        }
    }
}
