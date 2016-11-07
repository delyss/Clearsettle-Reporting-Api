package net.hasanguner.integration.transaction;

import net.hasanguner.integration.TestUtils;
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
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by hasanguner on 06/11/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionsReportTests {

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

    @Test
    public void transactionsReportWithoutAuthorizationHeaderShouldReturnAuthError() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/transactions/report"))
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
    public void transactionsReportWithEmptyAuthorizationHeaderShouldReturnAuthError() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/transactions/report").header("Authorization",""))
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
    public void transactionsReportWithInvalidAuthTokenShouldReturnApiError() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/transactions/report")
                .header("Authorization","invalidToken")
                .param("fromDate","2016-01-01")
                .param("toDate","2016-01-10"))
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
    public void transactionsReportWithValidAuthorizationTokenShouldReturnTransactionsReport() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/transactions/report")
                .header("Authorization",generateTokenWithValidCredentials())
                .param("fromDate","2015-01-01")
                .param("toDate","2016-01-10"))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(ResponseEntity.class)))
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("status").value("APPROVED"))
                .andExpect(jsonPath("response").isNotEmpty())
                .andExpect(jsonPath("response").isArray());

    }

    @Test
    public void transactionsReportWithNoParamShouldReturnErrors() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/transactions/report")
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
                .andExpect(jsonPath("errors",hasSize(2)))
                .andExpect(jsonPath("$.errors[*].field").isNotEmpty())
                .andExpect(jsonPath("$.errors[*].message").isNotEmpty());
    }


    @Test
    public void transactionsReportWithEmptyFromDateShouldReturnFromDateError() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/transactions/report")
                .header("Authorization",generateTokenWithValidCredentials())
                .param("toDate","2015-01-01"))
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
                .andExpect(jsonPath("$.errors[0].field").value("FromDate"))
                .andExpect(jsonPath("$.errors[*].message").isNotEmpty());
    }

    @Test
    public void transactionsReportWithEmptyToDateShouldReturnToDateError() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/transactions/report")
                .header("Authorization",generateTokenWithValidCredentials())
                .param("fromDate","2015-01-01"))
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
                .andExpect(jsonPath("$.errors[0].field").value("ToDate"))
                .andExpect(jsonPath("$.errors[*].message").isNotEmpty());
    }

    @Test
    public void transactionsReportWithInvalidTimeIntervalShouldReturnEmptyTransactionReportList() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/transactions/report")
                .header("Authorization",generateTokenWithValidCredentials())
                .param("fromDate","2085-01-01")
                .param("toDate","2095-01-01"))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(ResponseEntity.class)))
                .andReturn();

        this.mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("status").value("APPROVED"))
                .andExpect(jsonPath("response").isEmpty());
    }

}
