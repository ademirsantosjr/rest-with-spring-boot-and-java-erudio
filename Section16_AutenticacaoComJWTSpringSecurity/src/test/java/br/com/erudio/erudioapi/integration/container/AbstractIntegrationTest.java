package br.com.erudio.erudioapi.integration.container;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.35");

        private static void startContainers(){
            Startables.deepStart(Stream.of(mySQLContainer)).join();
        }

        private Map<String, Object> createConnectionConfiguration() {
            return Map.of(
                    "spring.datasource.url", mySQLContainer.getJdbcUrl(),
                    "spring.datasource.username", mySQLContainer.getUsername(),
                    "spring.datasource.password", mySQLContainer.getPassword()
            );
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            startContainers();
            ConfigurableEnvironment configurableEnvironment = applicationContext.getEnvironment();
            MapPropertySource mapPropertySourceTestContainers =
                    new MapPropertySource("testcontainers", createConnectionConfiguration());
            configurableEnvironment.getPropertySources().addFirst(mapPropertySourceTestContainers);
        }
    }

}
