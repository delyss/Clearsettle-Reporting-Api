package net.hasanguner.controller;

import net.hasanguner.model.error.ApiError;
import net.hasanguner.model.error.AuthorizationError;
import net.hasanguner.model.error.BaseError;
import net.hasanguner.model.error.ErrorResponse;
import net.hasanguner.model.request.ClientRequest;
import net.hasanguner.model.response.ClientResponse;
import net.hasanguner.service.ClientService;
import net.hasanguner.util.ErrorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by hasanguner on 07/11/2016.
 */
@RestController
public class ClientController {

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    ClientService clientService;

    @PostMapping(value = "/client")
    public Callable<ResponseEntity> getClientDetails(@RequestHeader(value = "Authorization", required = false) String authorization, @Valid ClientRequest clientRequest, BindingResult bindingResult) {

        if (StringUtils.isEmpty(authorization)) {

            ErrorResponse errorResponse = new ErrorResponse(
                    Stream.of(new AuthorizationError()).collect(Collectors.toList()));

            return () -> new ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED);

        }

        log.info("Transactions report request -> with : {} ", clientRequest);

        if (bindingResult.hasErrors()) {

            List<BaseError> errorList = ErrorUtils.getValidationErrors(bindingResult.getFieldErrors());

            return () -> new ResponseEntity(new ErrorResponse(errorList), HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return () -> {

            Future<Optional<ClientResponse>> clientInfoFuture = clientService.getClientDetails(clientRequest, authorization);
            Optional<ClientResponse> clientResponse = clientInfoFuture.get();

            if (clientResponse.isPresent()) {

                return new ResponseEntity(clientResponse.get(), HttpStatus.OK);

            } else {

                ErrorResponse errorResponse = new ErrorResponse(
                        Stream.of(new ApiError()).collect(Collectors.toList()));

                return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

            }

        };

    }

}
