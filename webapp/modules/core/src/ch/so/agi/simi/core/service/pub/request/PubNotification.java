package ch.so.agi.simi.core.service.pub.request;

import ch.so.agi.simi.core.service.pub.UpdatePublishTimeService;
import ch.so.agi.simi.global.exc.CodedException;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * POJO representing the valid message body from a pub signal api call.
 */
public class PubNotification {

    private static ValidatorFactory valFact;

    private static ObjectMapper mapper;

    @NotBlank(message = "Data identifier must not be blank.")
    private String dataIdent;

    @NotNull(message = "Published timestamp must not be blank")
    private LocalDateTime published;

    private List<Basket> publishedBaskets;

    private List<Region> publishedRegions;

    static{
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        valFact = Validation.buildDefaultValidatorFactory();
    }

    public static PubNotification parseFromJson(String json){
        PubNotification res = null;

        if(json == null || json.isEmpty())
            throw new CodedException(400, UpdatePublishTimeService.ERR_MSGBODY_EMPTY);

        try {
            res = mapper.readValue(json, PubNotification.class);
            res.assertValid();
        }
        catch(CodedException e){
            throw e;
        }
        catch(JsonParseException pe){
            throw new CodedException(400, UpdatePublishTimeService.ERR_MSGBODY_INVALID, null, pe);
        }
        catch(JsonProcessingException pe){
            throw new CodedException(400, UpdatePublishTimeService.ERR_MSGBODY_INVALID, null, pe);
        }
        catch (Exception e){
            throw new CodedException(500, UpdatePublishTimeService.ERR_SERVER, e.getMessage());
        }

        return res;
    }

    private void assertValid(){
        Validator validator = valFact.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(this);

        List<String> validationErrors = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());

        boolean regionsMissing = (publishedRegions == null || publishedRegions.size() == 0);
        boolean basketsMissing = (publishedBaskets == null || publishedBaskets.size() == 0);

        if(regionsMissing && basketsMissing)
            validationErrors.add("Either publishedRegions or publishedBaskets must be set");

        if(violations.size() > 0){
            String innerMessages = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(" | "));
            throw new CodedException(400, UpdatePublishTimeService.ERR_MSGBODY_INVALID, " Validation error(s): " + innerMessages);
        }
    }

    public boolean hasRegionInfo(){
        return publishedRegions.size() > 0;
    }

    public String getDataIdent() {
        return dataIdent;
    }

    public void setDataIdent(String dataIdent) {
        this.dataIdent = dataIdent;
    }

    public LocalDateTime getPublished() {
        return published;
    }

    public void setPublished(LocalDateTime published) {
        this.published = published;
    }

    public List<Basket> getPublishedBaskets() {
        return publishedBaskets;
    }

    public void setPublishedBaskets(List<Basket> publishedBaskets) {
        this.publishedBaskets = publishedBaskets;
    }

    public List<Region> getPublishedRegions() {
        return publishedRegions;
    }

    public void setPublishedRegions(List<Region> publishedRegions) {
        this.publishedRegions = publishedRegions;
    }
}


