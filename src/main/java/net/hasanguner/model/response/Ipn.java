package net.hasanguner.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Ipn {

    @JsonProperty("sent")
    private Boolean sent;
    @JsonProperty("merchant")
    private IpnMerchant merchant;

    public Ipn() {
        super();
    }

    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    public IpnMerchant getMerchant() {
        return merchant;
    }

    public void setMerchant(IpnMerchant merchant) {
        this.merchant = merchant;
    }
}
