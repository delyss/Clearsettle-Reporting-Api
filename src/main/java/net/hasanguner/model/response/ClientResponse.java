package net.hasanguner.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by hasanguner on 07/11/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientResponse {

    @JsonProperty("customerInfo")
    private CustomerInfo customerInfo;

    public ClientResponse() {
        super();
    }
    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }
}
