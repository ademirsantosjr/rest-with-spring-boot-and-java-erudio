package br.com.erudio.erudioapi.repository;

import br.com.erudio.erudioapi.integration.container.AbstractIntegrationTest;
import br.com.erudio.erudioapi.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    private static Person person;

    @BeforeAll
    public void setup() {
        person = new Person();
        person.setFirstName("Elisabeth");
        person.setLastName("Noelle-Neumann");
        person.setAddress("Allensbach, Germany");
        person.setGender("Female");
        person.setEnabled(true);
        personRepository.save(person);
    }

    @Test
    @Order(0)
    public void findByNameTest() throws JsonProcessingException {

        Pageable pageable = PageRequest.of(
                0, 12, Sort.by(Sort.Direction.ASC, "firstName"));

        Person foundPerson = personRepository.findPersonsByName(
                "isa", pageable).getContent().getFirst();

        Assertions.assertNotNull(foundPerson);
        Assertions.assertNotNull(foundPerson.getFirstName());
        Assertions.assertNotNull(foundPerson.getLastName());
        Assertions.assertNotNull(foundPerson.getAddress());
        Assertions.assertNotNull(foundPerson.getGender());

        Assertions.assertTrue(foundPerson.getId() > 0);

        Assertions.assertEquals("Elisabeth", foundPerson.getFirstName());
        Assertions.assertEquals("Noelle-Neumann", foundPerson.getLastName());
        Assertions.assertEquals("Allensbach, Germany", foundPerson.getAddress());
        Assertions.assertEquals("Female", foundPerson.getGender());
    }
}
