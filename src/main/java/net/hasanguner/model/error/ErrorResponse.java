package net.hasanguner.model.error;

import java.util.List;

/**
 * Created by hasanguner on 06/11/2016.
 */
public class ErrorResponse {

    private List<BaseError> errors;

    public ErrorResponse() {
        super();
    }

    public ErrorResponse(List<BaseError> errors) {
        this.errors = errors;
    }
    public List<BaseError> getErrors() {
        return errors;
    }

    public void setErrors(List<BaseError> errors) {
        this.errors = errors;
    }
}
