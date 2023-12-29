package br.com.erudio.erudioapi.service;

import br.com.erudio.erudioapi.data.vo.v1.PersonDto;
import br.com.erudio.erudioapi.data.vo.v2.PersonDtoV2;
import br.com.erudio.erudioapi.exceptions.ResourceNotFoundException;
import br.com.erudio.erudioapi.mapper.PersonMapper;
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

    public List<PersonDto> findAll() {
        logger.info("Finding a persons");
        return personRepository.findAll()
                .stream()
                .map(PersonMapper.INSTANCE::toPersonDto)
                .toList();
    }

    public PersonDto findById(Long id) {
        logger.info("Finding a person");
        var foundPerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        return PersonMapper.INSTANCE.toPersonDto(foundPerson);
    }

    public PersonDto create(PersonDto personDto) {
        logger.info("Creating a person");
        var newPerson = PersonMapper.INSTANCE.toPerson(personDto);
        return PersonMapper.INSTANCE.toPersonDto(personRepository.save(newPerson));
    }

    public PersonDtoV2 createV2(PersonDtoV2 personDtoV2) {
        logger.info("Creating a person V2");
        var newPerson = PersonMapper.INSTANCE.toPerson(personDtoV2);
        return PersonMapper.INSTANCE.toPersonDtoV2(personRepository.save(newPerson));
    }

    public PersonDto update(PersonDto personDto) {
        logger.info("Creating a person");
        var foundPerson = personRepository.findById(personDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        foundPerson.setFirstName(personDto.getFirstName());
        foundPerson.setLastName(personDto.getLastName());
        foundPerson.setAddress(personDto.getAddress());
        foundPerson.setGender(personDto.getGender());
        return PersonMapper.INSTANCE.toPersonDto(personRepository.save(foundPerson));
    }

    public void deleteById(Long id) {
        logger.info("Deleting a person");
        var foundPerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        personRepository.delete(foundPerson);
    }


}
