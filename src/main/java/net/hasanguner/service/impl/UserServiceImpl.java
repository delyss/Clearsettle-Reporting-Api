package net.hasanguner.service.impl;

import net.hasanguner.model.response.AuthToken;
import net.hasanguner.model.request.Credential;
import net.hasanguner.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by hasanguner on 05/11/2016.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${baseUrl}")
    private String baseUrl;

    @Async
    @Override
    public Future<Optional<AuthToken>> login(Credential credential) {

        String url = baseUrl + "/merchant/user/login";
        AuthToken authToken = null;

        try {
            log.info("Api call -> to : {}, with : {}", url, credential);
            authToken = restTemplate.postForObject(url, credential, AuthToken.class);
        } catch (RestClientResponseException exception) {
            log.info("Api call failed -> status : {} body : {}", exception.getStatusText(), exception.getResponseBodyAsString());
        }

        return new AsyncResult<>(Optional.ofNullable(authToken));
    }
}
