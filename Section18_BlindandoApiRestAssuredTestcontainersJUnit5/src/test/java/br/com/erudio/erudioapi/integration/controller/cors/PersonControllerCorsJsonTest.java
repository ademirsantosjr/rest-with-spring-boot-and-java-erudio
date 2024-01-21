package br.com.erudio.erudioapi.integration.controller.cors;

import br.com.erudio.erudioapi.config.TestConfig;
import br.com.erudio.erudioapi.integration.container.AbstractIntegrationTest;
import br.com.erudio.erudioapi.integration.dto.AccountCredentialsTestDto;
import br.com.erudio.erudioapi.integration.dto.PersonTestDto;
import br.com.erudio.erudioapi.integration.dto.TokenTestDto;
import br.com.erudio.erudioapi.model.User;
import br.com.erudio.erudioapi.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerCorsJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification requestSpecification;
	private static ObjectMapper objectMapper;
	private static PersonTestDto personTestDto;
	private static User user;
	@Autowired
	private UserRepository userRepository;

	@BeforeAll
	public void setup() {
		objectMapper = new ObjectMapper();
		// ignore fields of HATEOAS
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		user = new User();
		personTestDto = new PersonTestDto();
	}

	@AfterAll
	public void afterAll() {
		userRepository.deleteAll();
	}

	@Test
	@Order(0)
	public void authorization() throws JsonProcessingException {

		mockUser();

		userRepository.save(user);

		AccountCredentialsTestDto accountCredentialsTestDto =
				new AccountCredentialsTestDto("erudio", "admin123");

		String accessToken =
				given()
						.basePath("/auth/signin")
						.port(TestConfig.SERVER_PORT)
						.contentType(TestConfig.CONTENT_TYPE_JSON)
						.body(accountCredentialsTestDto)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(TokenTestDto.class)
						.getAccessToken();

		requestSpecification = new RequestSpecBuilder()
				.addHeader(
						TestConfig.HEADER_PARAM_AUTHORIZATION,
						"Bearer %s".formatted(accessToken)
				)
				.setBasePath("/api/persons")
				.setPort(TestConfig.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	public void createTest() throws JsonProcessingException {

		mockPerson();

		String content =
				given()
						.spec(requestSpecification)
						.contentType(TestConfig.CONTENT_TYPE_JSON)
						.header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_ERUDIO)
						.body(personTestDto)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		PersonTestDto persistedPersonTestDto =
				objectMapper.readValue(content, PersonTestDto.class);

		personTestDto = persistedPersonTestDto;

		Assertions.assertNotNull(persistedPersonTestDto);
		Assertions.assertNotNull(persistedPersonTestDto.getFirstName());
		Assertions.assertNotNull(persistedPersonTestDto.getLastName());
		Assertions.assertNotNull(persistedPersonTestDto.getAddress());
		Assertions.assertNotNull(persistedPersonTestDto.getGender());

		Assertions.assertTrue(persistedPersonTestDto.getId() > 0);

		Assertions.assertEquals("Richard", persistedPersonTestDto.getFirstName());
		Assertions.assertEquals("Stallman", persistedPersonTestDto.getLastName());
		Assertions.assertEquals("New York City, NY, US", persistedPersonTestDto.getAddress());
		Assertions.assertEquals("Male", persistedPersonTestDto.getGender());
	}

	@Test
	@Order(2)
	public void createTestWrongOrigin() throws JsonProcessingException {

		mockPerson();

		String content =
				given()
						.spec(requestSpecification)
						.contentType(TestConfig.CONTENT_TYPE_JSON)
						.header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_SEMERU)
						.body(personTestDto)
						.when()
						.post()
						.then()
						.statusCode(403)
						.extract()
						.body()
						.asString();


		Assertions.assertNotNull(content);
		Assertions.assertEquals("Invalid CORS request", content);
	}

	@Test
	@Order(3)
	public void findByIdTest() throws JsonProcessingException {

		mockPerson();

		String content =
				given()
						.spec(requestSpecification)
						.contentType(TestConfig.CONTENT_TYPE_JSON)
						.header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_ERUDIO)
						.pathParam("id", personTestDto.getId()) // that is why the Order annotations
						.when()
							.get("{id}")
						.then()
							.statusCode(200)
						.extract()
							.body()
								.asString();

		PersonTestDto persistedPersonTestDto =
				objectMapper.readValue(content, PersonTestDto.class);

		personTestDto = persistedPersonTestDto;

		Assertions.assertNotNull(persistedPersonTestDto);
		Assertions.assertNotNull(persistedPersonTestDto.getFirstName());
		Assertions.assertNotNull(persistedPersonTestDto.getLastName());
		Assertions.assertNotNull(persistedPersonTestDto.getAddress());
		Assertions.assertNotNull(persistedPersonTestDto.getGender());

		Assertions.assertTrue(persistedPersonTestDto.getId() > 0);

		Assertions.assertEquals("Richard", persistedPersonTestDto.getFirstName());
		Assertions.assertEquals("Stallman", persistedPersonTestDto.getLastName());
		Assertions.assertEquals("New York City, NY, US", persistedPersonTestDto.getAddress());
		Assertions.assertEquals("Male", persistedPersonTestDto.getGender());
	}

	@Test
	@Order(4)
	public void findByIdWrongOriginTest() throws JsonProcessingException {

		mockPerson();

		String content =
				given()
						.spec(requestSpecification)
						.contentType(TestConfig.CONTENT_TYPE_JSON)
						.header(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_SEMERU)
						.pathParam("id", personTestDto.getId()) // that is why the Order annotations
						.when()
						.get("{id}")
						.then()
						.statusCode(403)
						.extract()
						.body()
						.asString();

		Assertions.assertNotNull(content);
		Assertions.assertEquals("Invalid CORS request", content);
	}

	private void mockUser() {
		user.setUsername("erudio");
		user.setFullName("Erudio API");
		user.setPassword("{pbkdf2}08646dd795a79e50e806532088040741913b70edd713e3227925d94cc1e12f783ac34525592a2acb");
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setEnabled(true);
	}

	private void mockPerson() {
		personTestDto.setFirstName("Richard");
		personTestDto.setLastName("Stallman");
		personTestDto.setAddress("New York City, NY, US");
		personTestDto.setGender("Male");
	}

}
