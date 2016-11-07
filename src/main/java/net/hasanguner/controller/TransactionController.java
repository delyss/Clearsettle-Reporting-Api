package net.hasanguner.controller;

import net.hasanguner.model.error.ApiError;
import net.hasanguner.model.error.AuthorizationError;
import net.hasanguner.model.error.BaseError;
import net.hasanguner.model.error.ErrorResponse;
import net.hasanguner.model.request.TransactionQueryRequest;
import net.hasanguner.model.request.TransactionsReportRequest;
import net.hasanguner.model.response.TransactionQueryResponse;
import net.hasanguner.model.response.TransactionsReportResponse;
import net.hasanguner.service.TransactionService;
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
 * Created by hasanguner on 06/11/2016.
 */
@RestController
public class TransactionController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    TransactionService transactionService;

    @PostMapping(value = "/transactions/report")
    public Callable<ResponseEntity> reportTransactions(@RequestHeader(value="Authorization", required = false) String authorization, @Valid TransactionsReportRequest transactionsReportRequest, BindingResult bindingResult) {

        if (StringUtils.isEmpty(authorization)) {

            ErrorResponse errorResponse = new ErrorResponse(
                    Stream.of( new AuthorizationError()).collect(Collectors.toList()));

            return () -> new ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED);

        }

        log.info("Transactions report request -> with : {} ", transactionsReportRequest);

        if (bindingResult.hasErrors()) {

            List<BaseError> errorList = ErrorUtils.getValidationErrors(bindingResult.getFieldErrors());

            return () -> new ResponseEntity(new ErrorResponse(errorList), HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return () -> {

            Future<Optional<TransactionsReportResponse>> transactionsReportFuture = transactionService.reportTransactions(transactionsReportRequest, authorization);
            Optional<TransactionsReportResponse> transactionsReportResponse = transactionsReportFuture.get();

            if (transactionsReportResponse.isPresent()) {

                return new ResponseEntity(transactionsReportResponse.get(), HttpStatus.OK);

            } else {

                ErrorResponse errorResponse = new ErrorResponse(
                        Stream.of( new ApiError()).collect(Collectors.toList()));

                return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

            }

        };

    }

    @PostMapping(value = "/transaction/list")
    public Callable<ResponseEntity> queryTransactions(@RequestHeader(value="Authorization", required = false) String authorization, @Valid TransactionQueryRequest transactionQueryRequest, BindingResult bindingResult) {

        if (StringUtils.isEmpty(authorization)) {

            ErrorResponse errorResponse = new ErrorResponse(
                    Stream.of( new AuthorizationError()).collect(Collectors.toList()));

            return () -> new ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED);

        }

        log.info("Transaction query request -> with : {} ", transactionQueryRequest);

        if (bindingResult.hasErrors()) {

            List<BaseError> errorList = ErrorUtils.getValidationErrors(bindingResult.getFieldErrors());

            return () -> new ResponseEntity(new ErrorResponse(errorList), HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return () -> {

            Future<Optional<TransactionQueryResponse>> transactionQueryFuture = transactionService.queryTransactions(transactionQueryRequest, authorization);
            Optional<TransactionQueryResponse> transactionQueryResponse = transactionQueryFuture.get();

            if (transactionQueryResponse.isPresent()) {

                return new ResponseEntity(transactionQueryResponse.get(), HttpStatus.OK);

            } else {

                ErrorResponse errorResponse = new ErrorResponse(
                        Stream.of( new ApiError()).collect(Collectors.toList()));

                return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

            }

        };

    }
}
