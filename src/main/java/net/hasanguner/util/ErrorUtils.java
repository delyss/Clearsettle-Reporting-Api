package net.hasanguner.util;

import net.hasanguner.model.error.BaseError;
import net.hasanguner.model.error.ValidationError;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hasanguner on 06/11/2016.
 */
public class ErrorUtils {

    public static List<BaseError> getValidationErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(f -> new ValidationError(StringUtils.capitalize(f.getField()),f.getDefaultMessage()))
                .collect(Collectors.toList());
    }
}
