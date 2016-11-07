package net.hasanguner.model.request;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by hasanguner on 05/11/2016.
 */
public class Credential {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public Credential() {
        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Credential{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
