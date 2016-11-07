package net.hasanguner.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by hasanguner on 05/11/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthToken {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
