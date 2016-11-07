package net.hasanguner.integration.client;

import net.hasanguner.integration.TestUtils;
import org.hamcrest.collection.IsMapContaining;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by hasanguner on 07/11/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ClientTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${email}")
    private String validEmail;

    @Value("${password}")
    private String validPassword;

    private String generateTokenWithValidCredentials() {
        return TestUtils.generateTokenWithValidCredentials(restTemplate,baseUrl,validEmail,validPassword);
    }

    private String getValidTransactionId() {
        return TestUtils.getValidTransactionId(restTemplate,baseUrl,generateTokenWithValidCredentials());
    }


    @Test
    public void clientInfoRequestWithoutAuthorizationHeaderShouldReturnAuthError() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/client"))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(ResponseEntity.class)))
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("errors").isNotEmpty())
                .andExpect(jsonPath("errors").isArray())
                .andExpect(jsonPath("errors",hasSize(1)))
                .andExpect(jsonPath("$.errors[*].message").isNotEmpty());

    }

    @Test
    public void clientInfoRequestWithEmptyAuthorizationHeaderShouldReturnAuthError() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/client").header("Authorization",""))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(ResponseEntity.class)))
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("errors").isNotEmpty())
                .andExpect(jsonPath("errors").isArray())
                .andExpect(jsonPath("errors",hasSize(1)))
                .andExpect(jsonPath("$.errors[*].message").isNotEmpty());

    }

    @Test
    public void clientInfoRequestWithInvalidAuthTokenShouldReturnApiError() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/client")
                .header("Authorization","invalidToken")
                .param("transactionId",getValidTransactionId()))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(ResponseEntity.class)))
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("errors").isNotEmpty())
                .andExpect(jsonPath("errors").isArray())
                .andExpect(jsonPath("errors",hasSize(1)))
                .andExpect(jsonPath("$.errors[*].message").isNotEmpty());

    }

    @Test
    public void clientInfoRequestWithValidAuthorizationTokenShouldReturnClientInfo() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/client")
                .header("Authorization",generateTokenWithValidCredentials())
                .param("transactionId",getValidTransactionId()))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(ResponseEntity.class)))
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("customerInfo").isNotEmpty());

    }

    @Test
    public void clientInfoRequestWithNoParamShouldReturnErrors() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/client")
                .header("Authorization",generateTokenWithValidCredentials()))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(ResponseEntity.class)))
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("errors").isNotEmpty())
                .andExpect(jsonPath("errors").isArray())
                .andExpect(jsonPath("errors",hasSize(1)))
                .andExpect(jsonPath("$.errors[*].field").isNotEmpty())
                .andExpect(jsonPath("$.errors[*].message").isNotEmpty());
    }


    @Test
    public void clientInfoRequestWithEmptyTransactionIdShouldReturnTransactionIdError() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/client")
                .header("Authorization",generateTokenWithValidCredentials())
                .param("transactionId",""))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(ResponseEntity.class)))
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("errors").isNotEmpty())
                .andExpect(jsonPath("errors").isArray())
                .andExpect(jsonPath("errors",hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field").value("TransactionId"))
                .andExpect(jsonPath("$.errors[*].message").isNotEmpty());
    }


    @Test
    public void clientInfoRequestWithInvalidTransactionIdShouldReturnEmptyResponse() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/client")
                .header("Authorization",generateTokenWithValidCredentials())
                .param("transactionId","invalidTransactionId"))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(ResponseEntity.class)))
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath(".customerInfo.billingAddress").doesNotExist());
    }
}
