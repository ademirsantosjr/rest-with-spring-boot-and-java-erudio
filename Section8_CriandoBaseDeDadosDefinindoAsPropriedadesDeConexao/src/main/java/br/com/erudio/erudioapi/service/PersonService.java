package br.com.erudio.erudioapi.service;

import br.com.erudio.erudioapi.exceptions.ResourceNotFoundException;
import br.com.erudio.erudioapi.model.Person;
import br.com.erudio.erudioapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonService {

    private final Logger logger = Logger.getLogger(PersonService.class.getName());
    @Autowired
    private PersonRepository personRepository;

    public List<Person> findAll() {
        logger.info("Finding a persons");
        return personRepository.findAll();
    }

    public Person findById(Long id) {
        logger.info("Finding a person");
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
    }

    public Person create(Person person) {
        logger.info("Creating a person");
        return personRepository.save(person);
    }

    public Person update(Person person) {
        logger.info("Creating a person");
        var foundPerson = personRepository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        foundPerson.setFirstName(person.getFirstName());
        foundPerson.setLastName(person.getLastName());
        foundPerson.setAddress(person.getAddress());
        foundPerson.setGender(person.getGender());
        return personRepository.save(person);
    }

    public void deleteById(Long id) {
        logger.info("Deleting a person");
        var foundPerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        personRepository.delete(foundPerson);
    }
}
