package ch.so.agi.simi.core.service.pub.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class Region {
    @NotNull(message = "Implementation error. May be empty but not null")
    @JsonSetter(nulls= Nulls.AS_EMPTY)
    private List<Basket> publishedBaskets;

    @NotBlank()
    private String region;

    public List<Basket> getPublishedBaskets() {
        return publishedBaskets;
    }

    public void setPublishedBaskets(List<Basket> publishedBaskets) {
        this.publishedBaskets = publishedBaskets;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
