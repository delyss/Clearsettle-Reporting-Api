
package net.hasanguner.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Merchant {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("allowPartialRefund")
    private Boolean allowPartialRefund;
    @JsonProperty("allowPartialCapture")
    private Boolean allowPartialCapture;

    public Merchant() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAllowPartialRefund() {
        return allowPartialRefund;
    }

    public void setAllowPartialRefund(Boolean allowPartialRefund) {
        this.allowPartialRefund = allowPartialRefund;
    }

    public Boolean getAllowPartialCapture() {
        return allowPartialCapture;
    }

    public void setAllowPartialCapture(Boolean allowPartialCapture) {
        this.allowPartialCapture = allowPartialCapture;
    }
}
