package net.hasanguner.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Fx {

    @JsonProperty("merchant")
    private FxMerchant fxMerchant;

    public Fx() {
        super();
    }

    public FxMerchant getFxMerchant() {
        return fxMerchant;
    }

    public void setFxMerchant(FxMerchant fxMerchant) {
        this.fxMerchant = fxMerchant;
    }
}
