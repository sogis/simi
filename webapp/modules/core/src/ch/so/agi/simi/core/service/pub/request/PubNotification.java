package ch.so.agi.simi.core.service.pub.request;

import ch.so.agi.simi.global.exc.CodedException;
import ch.so.agi.simi.global.exc.SimiException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
            throw new CodedException(400, CodedException.ERR_MSGBODY_EMPTY);

        try {
            res = mapper.readValue(json, PubNotification.class);
            res.assertValid();
        }
        catch(CodedException e){
            throw e;
        }
        catch(JsonParseException pe){
            throw new CodedException(400, CodedException.ERR_MSGBODY_INVALID, null, pe);
        }
        catch(JsonProcessingException pe){
            throw new CodedException(400, CodedException.ERR_MSGBODY_INVALID, null, pe);
        }
        catch (Exception e){
            throw new CodedException(500, CodedException.ERR_SERVER, e.getMessage());
        }

        return res;
    }

    public String toJson(){
        String json = null;

        try{
            json = mapper.writeValueAsString(this);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
        return json;
    }

    private void assertValid(){
        Validator validator = valFact.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(this);

        if(violations.size() > 0){
            String innerMessages = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(" | "));
            throw new CodedException(400, CodedException.ERR_MSGBODY_INVALID, " Validation error(s): " + innerMessages);
        }
    }

    public Optional<String> deferModelName(){
        String modelName = null;

        if(publishedRegions != null && publishedRegions.size() > 0){
            List<String> distinctModels = publishedRegions.stream()
                    .flatMap(region -> region.getPublishedBaskets().stream())
                    .map(basket -> basket.getModel())
                    .distinct()
                    .collect(Collectors.toList());

            if(distinctModels.size() != 1){
                throw new CodedException(
                        400,
                        CodedException.ERR_MSGBODY_INVALID,
                        "Unsupported: Basket.Regions in request reference multiple public models: " + distinctModels
                );
            }

            modelName = distinctModels.get(0);
        }
        else if(publishedBaskets != null && publishedBaskets.size() > 0){
            List<String> distinctModels = publishedBaskets.stream()
                    .map(basket -> basket.getModel())
                    .distinct()
                    .collect(Collectors.toList());

            if(distinctModels.size() != 1) {
                throw new CodedException(
                        400,
                        CodedException.ERR_MSGBODY_INVALID,
                        "Unsupported: Baskets in request reference multiple public models: " + distinctModels
                );
            }

            modelName = distinctModels.get(0);
        }
        return Optional.ofNullable(modelName);
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


