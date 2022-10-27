package ch.so.agi.simi.core.service.pub.request;

import javax.validation.constraints.NotBlank;

public class Basket {
    @NotBlank
    private String model;
    @NotBlank
    private String topic;
    @NotBlank
    private String basket;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBasket() {
        return basket;
    }

    public void setBasket(String basket) {
        this.basket = basket;
    }
}
