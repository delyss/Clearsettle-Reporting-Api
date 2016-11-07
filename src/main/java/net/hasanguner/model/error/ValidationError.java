package net.hasanguner.model.error;

/**
 * Created by hasanguner on 06/11/2016.
 */
public class ValidationError extends BaseError {

    private String field;
    private String message;

    public ValidationError(String field, String message) {
        super();
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
