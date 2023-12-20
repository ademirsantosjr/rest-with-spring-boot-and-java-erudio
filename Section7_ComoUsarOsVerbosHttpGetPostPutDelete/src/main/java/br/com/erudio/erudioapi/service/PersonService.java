package br.com.erudio.erudioapi.service;

import br.com.erudio.erudioapi.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonService {

    private final AtomicLong counter = new AtomicLong();
    private final Logger logger = Logger.getLogger(PersonService.class.getName());

    public List<Person> findAll() {
        logger.info("Finding a persons");
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Person person = mockPerson(i);
            persons.add(person);
        }
        return persons;
    }

    public Person findById(String id) {
        logger.info("Finding a person");
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Fulano");
        person.setLastName("De Tal");
        person.setAddress("Curitiba");
        person.setGender("Male");
        return person;
    }

    public Person create(Person person) {
        logger.info("Creating a person");
        person.setId(counter.incrementAndGet());
        return person;
    }

    public Person update(Person person) {
        logger.info("Creating a person");
        return person;
    }

    public void deleteById(String id) {
        logger.info("Deleting a person");
    }

    private Person mockPerson(int i) {
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Fulano " + i);
        person.setLastName("De Tal");
        person.setAddress("Cidade " + i);
        person.setGender("Gender " + i);
        return person;
    }
}
