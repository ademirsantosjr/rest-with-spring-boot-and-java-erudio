package br.com.erudio.erudioapi.integration.controller;

import br.com.erudio.erudioapi.config.TestConfig;
import br.com.erudio.erudioapi.integration.container.AbstractIntegrationTest;
import br.com.erudio.erudioapi.integration.dto.PersonTestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification requestSpecification;
	private static ObjectMapper objectMapper;

	private static PersonTestDto personTestDto;

	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		// ignore fields of HATEOAS
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		personTestDto = new PersonTestDto();
	}

	@Test
	@Order(1)
	public void createTest() throws JsonProcessingException {

		mockPerson();

		requestSpecification = new RequestSpecBuilder()
				.addHeader(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_ERUDIO)
				.setBasePath("/api/persons")
				.setPort(TestConfig.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		String content =
				given()
						.spec(requestSpecification)
						.contentType(TestConfig.CONTENT_TYPE_JSON)
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

		requestSpecification = new RequestSpecBuilder()
				.addHeader(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_SEMERU)
				.setBasePath("/api/persons")
				.setPort(TestConfig.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		String content =
				given()
						.spec(requestSpecification)
						.contentType(TestConfig.CONTENT_TYPE_JSON)
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

		requestSpecification = new RequestSpecBuilder()
				.addHeader(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_ERUDIO)
				.setBasePath("/api/persons")
				.setPort(TestConfig.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		String content =
				given()
						.spec(requestSpecification)
						.contentType(TestConfig.CONTENT_TYPE_JSON)
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

		requestSpecification = new RequestSpecBuilder()
				.addHeader(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_SEMERU)
				.setBasePath("/api/persons")
				.setPort(TestConfig.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		String content =
				given()
						.spec(requestSpecification)
						.contentType(TestConfig.CONTENT_TYPE_JSON)
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

	private void mockPerson() {
		personTestDto.setFirstName("Richard");
		personTestDto.setLastName("Stallman");
		personTestDto.setAddress("New York City, NY, US");
		personTestDto.setGender("Male");
	}

}
