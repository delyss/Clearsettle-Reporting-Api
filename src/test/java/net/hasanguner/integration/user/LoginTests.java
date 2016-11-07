package net.hasanguner.integration.user;

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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTests {

	@Autowired
	private MockMvc mockMvc;

	@Value("${email}")
	private String validEmail;

	@Value("${password}")
	private String validPassword;


	@Test
	public void loginWithNoParamShouldReturnErrors() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(post("/login"))
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
//				.andExpect(jsonPath("$.errors[0,1].message").isNotEmpty())
				.andExpect(jsonPath("$.errors[*].message").isNotEmpty())
				.andExpect(jsonPath("$.errors[*].field").isNotEmpty());

	}

	@Test
	public void loginWithEmptyEmailShouldReturnEmailError() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(post("/login")
				.param("password","value")
				.param("email",""))
//				.andDo(print())
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
				.andExpect(jsonPath("$.errors[0].message").isNotEmpty())
				.andExpect(jsonPath("$.errors[0].field",is("Email")));
	}


	@Test
	public void loginWithInvalidEmailShouldReturnEmailError() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(post("/login")
				.param("password","123456")
				.param("email","john"))
//				.andDo(print())
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
				.andExpect(jsonPath("$.errors[0].message").isNotEmpty())
				.andExpect(jsonPath("$.errors[0].field",is("Email")));
	}


	@Test
	public void loginWithEmptyPasswordShouldReturnPasswordError() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(post("/login")
				.param("password","")
				.param("email","john@doe.com"))
//				.andDo(print())
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
				.andExpect(jsonPath("$.errors[0].message").isNotEmpty())
				.andExpect(jsonPath("$.errors[0].field",is("Password")));
	}

	@Test
	public void loginWithInValidCredentialsShouldReturnApiError() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(post("/login")
				.param("password","123456")
				.param("email","john@doe.com"))
//				.andDo(print())
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
				.andExpect(jsonPath("$.errors[0].message").isNotEmpty());
	}

	@Test
	public void loginWithValidCredentialsShouldReturnToken() throws Exception {

		MvcResult mvcResult = this.mockMvc.perform(post("/login")
				.param("email", validEmail)
				.param("password", validPassword))
//				.andDo(print())
				.andExpect(request().asyncStarted())
				.andExpect(request().asyncResult(instanceOf(ResponseEntity.class)))
				.andReturn();


		this.mockMvc.perform(asyncDispatch(mvcResult))
				.andDo(print())
				.andExpect(status().is2xxSuccessful())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("token").isNotEmpty());
	}

}
