package br.com.erudio.erudioapi.integration.controller.yaml;

import br.com.erudio.erudioapi.config.TestConfig;
import br.com.erudio.erudioapi.integration.container.AbstractIntegrationTest;
import br.com.erudio.erudioapi.integration.dto.AccountCredentialsTestDto;
import br.com.erudio.erudioapi.integration.dto.PersonTestDto;
import br.com.erudio.erudioapi.integration.dto.TokenTestDto;
import br.com.erudio.erudioapi.model.User;
import br.com.erudio.erudioapi.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerYamlTest extends AbstractIntegrationTest {

    private static YamlMapper yamlMapper;
    private static TokenTestDto tokenTestDto;
    private static PersonTestDto personTestDto;
    private static User user;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public void setup() {
        user = new User();
        yamlMapper = new YamlMapper();
    }

    @AfterAll
    public void aflterAll() {
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testSignIn() throws JsonProcessingException {

        mockUser();

        userRepository.save(user);

        AccountCredentialsTestDto accountCredentialsTestDto =
                new AccountCredentialsTestDto("erudio", "admin123");

        tokenTestDto =
                given()
                        .config(
                                RestAssuredConfig
                                        .config()
                                        .encoderConfig(
                                                EncoderConfig
                                                        .encoderConfig()
                                                        .encodeContentTypeAs(
                                                                TestConfig.CONTENT_TYPE_YAML,
                                                                ContentType.TEXT
                                                        )
                                        )
                        )
                        .accept(TestConfig.CONTENT_TYPE_YAML)
                        .basePath("/auth/signin")
                        .port(TestConfig.SERVER_PORT)
                        .contentType(TestConfig.CONTENT_TYPE_YAML)
                        .body(accountCredentialsTestDto, yamlMapper)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenTestDto.class, yamlMapper);

        assertNotNull(tokenTestDto.getAccessToken());
        assertNotNull(tokenTestDto.getRefreshToken());
    }

    @Test
    @Order(2)
    public void testRefresh() throws JsonProcessingException {

        mockUser();

        userRepository.save(user);

        AccountCredentialsTestDto accountCredentialsTestDto =
                new AccountCredentialsTestDto("erudio", "admin123");

        var newTokenTestDto =
                given()
                        .config(
                                RestAssuredConfig
                                        .config()
                                        .encoderConfig(
                                                EncoderConfig
                                                        .encoderConfig()
                                                        .encodeContentTypeAs(
                                                                TestConfig.CONTENT_TYPE_YAML,
                                                                ContentType.TEXT
                                                        )
                                        )
                        )
                        .accept(TestConfig.CONTENT_TYPE_YAML)
                        .basePath("/auth/refresh/%s".formatted(tokenTestDto.getUsername()))
                        .port(TestConfig.SERVER_PORT)
                        .contentType(TestConfig.CONTENT_TYPE_YAML)
                        .header(
                                TestConfig.HEADER_PARAM_AUTHORIZATION,
                                "Bearer " + tokenTestDto.getRefreshToken()
                        )
                        .when()
                        .put()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenTestDto.class, yamlMapper);

        assertNotNull(newTokenTestDto.getAccessToken());
        assertNotNull(newTokenTestDto.getRefreshToken());
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
}
