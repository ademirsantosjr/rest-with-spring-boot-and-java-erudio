package br.com.erudio.erudioapi.integration.controller.json;

import br.com.erudio.erudioapi.config.TestConfig;
import br.com.erudio.erudioapi.integration.container.AbstractIntegrationTest;
import br.com.erudio.erudioapi.integration.dto.AccountCredentialsTestDto;
import br.com.erudio.erudioapi.integration.dto.PersonTestDto;
import br.com.erudio.erudioapi.integration.dto.TokenTestDto;
import br.com.erudio.erudioapi.integration.dto.wrappers.WrapperPersonDto;
import br.com.erudio.erudioapi.model.Person;
import br.com.erudio.erudioapi.model.User;
import br.com.erudio.erudioapi.repository.PersonRepository;
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

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification requestSpecification;
	private static ObjectMapper objectMapper;
	private static PersonTestDto personTestDto;
	private static List<PersonTestDto> personTestDtoList;
	private static User user;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PersonRepository personRepository;

	@BeforeAll
	public void setup() {
		objectMapper = new ObjectMapper();
		// ignore fields of HATEOAS
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		user = new User();
		personTestDto = new PersonTestDto();
		personTestDtoList = new ArrayList<>();
		mockPersonList();
		personRepository.saveAll(
				personTestDtoList.stream()
						.map(dto -> {
							var person = new Person();
							person.setFirstName(dto.getFirstName());
							person.setLastName(dto.getLastName());
							person.setAddress(dto.getAddress());
							person.setGender(dto.getGender());
							person.setEnabled(dto.getEnabled());
							return person;
						})
						.toList());
	}

	@AfterAll
	public void afterAll() {
		userRepository.deleteAll();
		personRepository.deleteAll();
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
		Assertions.assertTrue(persistedPersonTestDto.getEnabled());

		Assertions.assertTrue(persistedPersonTestDto.getId() > 0);

		Assertions.assertEquals("Elisabeth", persistedPersonTestDto.getFirstName());
		Assertions.assertEquals("Noelle-Neumann", persistedPersonTestDto.getLastName());
		Assertions.assertEquals("Allensbach, Germany", persistedPersonTestDto.getAddress());
		Assertions.assertEquals("Female", persistedPersonTestDto.getGender());
	}

	@Test
	@Order(2)
	public void updateTest() throws JsonProcessingException {

		personTestDto.setLastName("Neumann");

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
		Assertions.assertTrue(persistedPersonTestDto.getEnabled());

		Assertions.assertEquals(personTestDto.getId(), persistedPersonTestDto.getId());

		Assertions.assertEquals("Elisabeth", persistedPersonTestDto.getFirstName());
		Assertions.assertEquals("Neumann", persistedPersonTestDto.getLastName());
		Assertions.assertEquals("Allensbach, Germany", persistedPersonTestDto.getAddress());
		Assertions.assertEquals("Female", persistedPersonTestDto.getGender());
	}

	@Test
	@Order(3)
	public void disableByIdTest() throws JsonProcessingException {

		String content =
				given()
						.spec(requestSpecification)
						.contentType(TestConfig.CONTENT_TYPE_JSON)
						.pathParam("id", personTestDto.getId()) // that is why the Order annotations
						.when()
							.patch("{id}")
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
		Assertions.assertFalse(persistedPersonTestDto.getEnabled());

		Assertions.assertTrue(persistedPersonTestDto.getId() > 0);

		Assertions.assertEquals("Elisabeth", persistedPersonTestDto.getFirstName());
		Assertions.assertEquals("Neumann", persistedPersonTestDto.getLastName());
		Assertions.assertEquals("Allensbach, Germany", persistedPersonTestDto.getAddress());
		Assertions.assertEquals("Female", persistedPersonTestDto.getGender());
	}

	@Test
	@Order(4)
	public void findByIdTest() throws JsonProcessingException {

		mockPerson();

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
		Assertions.assertFalse(persistedPersonTestDto.getEnabled());

		Assertions.assertTrue(persistedPersonTestDto.getId() > 0);

		Assertions.assertEquals("Elisabeth", persistedPersonTestDto.getFirstName());
		Assertions.assertEquals("Neumann", persistedPersonTestDto.getLastName());
		Assertions.assertEquals("Allensbach, Germany", persistedPersonTestDto.getAddress());
		Assertions.assertEquals("Female", persistedPersonTestDto.getGender());
	}

	@Test
	@Order(5)
	public void deleteByIdTest() throws JsonProcessingException {

		given()
				.spec(requestSpecification)
				.contentType(TestConfig.CONTENT_TYPE_JSON)
				.pathParam("id", personTestDto.getId()) // that is why the Order annotations
				.when()
				.delete("{id}")
				.then()
				.statusCode(204);
	}

	@Test
	@Order(6)
	public void findAllTest() throws JsonProcessingException {

		mockPersonList();

		String content =
				given()
						.spec(requestSpecification)
						.contentType(TestConfig.CONTENT_TYPE_JSON)
						.queryParam("page", 0, "limit", 12, "direction", "asc")
						.when()
						.get()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		WrapperPersonDto wrapperPersonDto = objectMapper.readValue(
				content, WrapperPersonDto.class);

		List<PersonTestDto> personTestDtoList = wrapperPersonDto.getEmbeddedDto().getPersonTestDtoList();

		PersonTestDto persistedPersonTestDto = personTestDtoList.getFirst();

		Assertions.assertNotNull(persistedPersonTestDto);
		Assertions.assertNotNull(persistedPersonTestDto.getFirstName());
		Assertions.assertNotNull(persistedPersonTestDto.getLastName());
		Assertions.assertNotNull(persistedPersonTestDto.getAddress());
		Assertions.assertNotNull(persistedPersonTestDto.getGender());

		Assertions.assertTrue(persistedPersonTestDto.getId() > 0);

		Assertions.assertEquals("Elisabeth", persistedPersonTestDto.getFirstName());
		Assertions.assertEquals("Noelle-Neumann", persistedPersonTestDto.getLastName());
		Assertions.assertEquals("Allensbach, Germany", persistedPersonTestDto.getAddress());
		Assertions.assertEquals("Female", persistedPersonTestDto.getGender());
	}

	@Test
	@Order(7)
	public void findAllNoTokenTest() throws JsonProcessingException {

		mockPersonList();

		RequestSpecification requestSpecificationNoToken =
				new RequestSpecBuilder()
						.setBasePath("/api/persons")
						.setPort(TestConfig.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();

		given()
				.spec(requestSpecificationNoToken)
				.contentType(TestConfig.CONTENT_TYPE_JSON)
				.when()
				.get()
				.then()
				.statusCode(403);
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
		personTestDto.setFirstName("Elisabeth");
		personTestDto.setLastName("Noelle-Neumann");
		personTestDto.setAddress("Allensbach, Germany");
		personTestDto.setGender("Female");
		personTestDto.setEnabled(true);
	}
	private void mockPersonList() {
		mockPerson();
		personTestDtoList.add(personTestDto);
	}

}
