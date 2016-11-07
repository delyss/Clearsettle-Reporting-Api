package net.hasanguner.service.impl;

import net.hasanguner.model.request.TransactionQueryRequest;
import net.hasanguner.model.request.TransactionsReportRequest;
import net.hasanguner.model.response.TransactionQueryResponse;
import net.hasanguner.model.response.TransactionsReportResponse;
import net.hasanguner.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by hasanguner on 06/11/2016.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${baseUrl}")
    private String baseUrl;

    @Async
    @Override
    public Future<Optional<TransactionsReportResponse>> reportTransactions(TransactionsReportRequest request, String authorizationToken) {

        String url = baseUrl + "/transactions/report";

        TransactionsReportResponse transactionsReportResponse = null;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationToken);

        HttpEntity entity = new HttpEntity<>(request,headers);
        try {
            log.info("Api call -> to : {}, with : {}", url, request);
            transactionsReportResponse = restTemplate.postForObject(url, entity, TransactionsReportResponse.class);
        } catch (RestClientResponseException exception) {
            log.info("Api call failed -> status : {}, body : {}", exception.getStatusText(), exception.getResponseBodyAsString());
        }

        return new AsyncResult<>(Optional.ofNullable(transactionsReportResponse));
    }

    @Async
    @Override
    public Future<Optional<TransactionQueryResponse>> queryTransactions(TransactionQueryRequest request, String authorizationToken) {

        String url = baseUrl + "/transaction/list";

        TransactionQueryResponse transactionQueryResponse = null;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationToken);

        HttpEntity entity = new HttpEntity<>(request,headers);
        try {
            log.info("Api call -> to : {}, with : {}", url, request);
            transactionQueryResponse = restTemplate.postForObject(url, entity, TransactionQueryResponse.class);
        } catch (RestClientResponseException exception) {
            log.info("Api call failed -> status : {}, body : {}", exception.getStatusText(), exception.getResponseBodyAsString());
        }

        return new AsyncResult<>(Optional.ofNullable(transactionQueryResponse));
    }

}
