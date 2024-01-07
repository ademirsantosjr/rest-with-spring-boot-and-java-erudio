package br.com.erudio.erudioapi.integration.swagger;

import br.com.erudio.erudioapi.config.TestConfig;
import br.com.erudio.erudioapi.integration.container.AbstractIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

	@Test
	public void shouldDisplaySwaggerUiPage() {
		String content =
				given()
						.basePath("/swagger-ui/index.html")
						.port(TestConfig.SERVER_PORT)
						.when()
						.get()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		Assertions.assertTrue(content.contains("Swagger UI"));
	}

}
