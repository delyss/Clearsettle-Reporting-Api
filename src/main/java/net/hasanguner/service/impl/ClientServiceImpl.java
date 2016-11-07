package net.hasanguner.service.impl;

import net.hasanguner.model.request.ClientRequest;
import net.hasanguner.model.response.ClientResponse;
import net.hasanguner.service.ClientService;
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
 * Created by hasanguner on 07/11/2016.
 */
@Service
public class ClientServiceImpl implements ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${baseUrl}")
    private String baseUrl;

    @Async
    @Override
    public Future<Optional<ClientResponse>> getClientDetails(ClientRequest request, String authorizationToken) {
        String url = baseUrl + "/client";

        ClientResponse clientResponse = null;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationToken);

        HttpEntity entity = new HttpEntity<>(request,headers);
        try {
            log.info("Api call -> to : {}, with : {}", url, request);
            clientResponse = restTemplate.postForObject(url, entity, ClientResponse.class);
        } catch (RestClientResponseException exception) {
            log.info("Api call failed -> status : {}, body : {}", exception.getStatusText(), exception.getResponseBodyAsString());
        }

        return new AsyncResult<>(Optional.ofNullable(clientResponse));
    }

}
