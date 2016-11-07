package net.hasanguner.controller;

import net.hasanguner.util.ErrorUtils;
import net.hasanguner.model.error.ApiError;
import net.hasanguner.model.error.BaseError;
import net.hasanguner.model.error.ErrorResponse;
import net.hasanguner.model.request.Credential;
import net.hasanguner.model.response.AuthToken;
import net.hasanguner.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by hasanguner on 06/11/2016.
 */
@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @PostMapping(value = "/login")
    public Callable<ResponseEntity> login(@Valid Credential credential, BindingResult bindingResult) {

        log.info("Login attempt -> email : {} , password : {}", credential.getEmail(), credential.getPassword());

        if (bindingResult.hasErrors()) {

            List<BaseError> errorList = ErrorUtils.getValidationErrors(bindingResult.getFieldErrors());

            return () -> new ResponseEntity(new ErrorResponse(errorList), HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return () -> {

            Future<Optional<AuthToken>> loginFuture = userService.login(credential);
            Optional<AuthToken> token = loginFuture.get();

            if (token.isPresent()) {

                return new ResponseEntity(token.get(), HttpStatus.OK);

            } else {

                ErrorResponse errorResponse = new ErrorResponse(
                        Stream.of( new ApiError("Invalid credentials.")).collect(Collectors.toList()));

                return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

            }

        };

    }

}