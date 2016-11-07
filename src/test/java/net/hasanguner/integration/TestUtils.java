package net.hasanguner.integration;

import net.hasanguner.model.request.Credential;
import net.hasanguner.model.request.TransactionQueryRequest;
import net.hasanguner.model.response.AuthToken;
import net.hasanguner.model.response.TransactionQueryResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * Created by hasanguner on 07/11/2016.
 */
public class TestUtils {


    public static String generateTokenWithValidCredentials(RestTemplate restTemplate, String baseUrl, String validEmail, String validPassword) {

        String url = baseUrl + "/merchant/user/login";
        Credential credential = new Credential();
        credential.setEmail(validEmail);
        credential.setPassword(validPassword);

        AuthToken authToken = restTemplate.postForObject(url, credential, AuthToken.class);

        return authToken.getToken();
    }

    public static String getValidTransactionId(RestTemplate restTemplate, String baseUrl, String token) {

        String url = baseUrl + "/transaction/list";


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity entity = new HttpEntity<>(new TransactionQueryRequest(), headers);

        TransactionQueryResponse transactionQueryResponse = restTemplate.postForObject(url, entity, TransactionQueryResponse.class);

        return transactionQueryResponse.getData().get(0).getTransaction().getMerchant().getTransactionId();
    }

}
