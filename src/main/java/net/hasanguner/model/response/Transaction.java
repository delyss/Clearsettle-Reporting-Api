package net.hasanguner.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by hasanguner on 06/11/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

    @JsonProperty
    private TransactionMerchant merchant;

    public Transaction() {
        super();
    }

    public TransactionMerchant getMerchant() {
        return merchant;
    }

    public void setMerchant(TransactionMerchant merchant) {
        this.merchant = merchant;
    }
}
