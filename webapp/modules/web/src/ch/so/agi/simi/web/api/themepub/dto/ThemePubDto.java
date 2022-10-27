package ch.so.agi.simi.web.api.themepub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

public class ThemePubDto {

    @NotBlank(message = "Data identifier must not be blank.")
    String dataIdent;

    @JsonInclude(JsonInclude.Include.NON_EMPTY) // Greift nur beim Serialisieren. Beim Deserialisieren wird leere collection erzeugt
    List<@NotBlank(message = "Part identifier must not be blank.") String> partIdentifiers;

    @NotBlank(message = "Published timestamp must not be blank")
    String published; // $td: Configure jackson mapping

    public ThemePubDto(){}
    public ThemePubDto(String dataIdent, List<String> partIdentifiers, String published){
        setDataIdent(dataIdent);
        setPartIdentifiers(partIdentifiers);
        setPublished(published);
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getDataIdent() {
        return dataIdent;
    }

    public void setDataIdent(String dataIdent) {
        this.dataIdent = dataIdent;
    }

    public List<String> getPartIdentifiers() {
        if(partIdentifiers == null)
            partIdentifiers = List.of();

        return partIdentifiers;
    }

    public void setPartIdentifiers(List<String> partIdentifiers) {
        this.partIdentifiers = partIdentifiers;
    }

    @Override
    public String toString(){
        String parts = getPartIdentifiers().stream().collect(Collectors.joining(", "));
        return MessageFormat.format("identifier: {0}, published: {1} parts: {2}", getDataIdent(), getPublished(), parts);
    }
}

