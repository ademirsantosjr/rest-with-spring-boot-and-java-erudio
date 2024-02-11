package br.com.erudio.erudioapi.service;

import br.com.erudio.erudioapi.controllers.PersonController;
import br.com.erudio.erudioapi.dto.v1.PersonDto;
import br.com.erudio.erudioapi.dto.v2.PersonDtoV2;
import br.com.erudio.erudioapi.exceptions.RequiredObjectNullException;
import br.com.erudio.erudioapi.exceptions.ResourceNotFoundException;
import br.com.erudio.erudioapi.mapper.PersonMapper;
import br.com.erudio.erudioapi.model.Person;
import br.com.erudio.erudioapi.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService {

    private final Logger logger = Logger.getLogger(PersonService.class.getName());
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PagedResourcesAssembler<PersonDto> assembler;

    public PagedModel<EntityModel<PersonDto>> findAll(Pageable pageable) {
        logger.info("Finding all persons");

        Page<Person> personPage = personRepository.findAll(pageable);

        Page<PersonDto> personDtosPage = personPage.map(PersonMapper.INSTANCE::toPersonDto);

        personDtosPage.map(
                personDto ->
                        personDto.add(
                                linkTo(methodOn(PersonController.class).findById(personDto.getKey()))
                                        .withSelfRel()
                ));

        Link link = linkTo(
                methodOn(PersonController.class)
                        .findAll(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                "asc"))
                .withSelfRel();

        return assembler.toModel(personDtosPage, link);
    }

    public PagedModel<EntityModel<PersonDto>> findPersonsByName(String firstName,
                                                                Pageable pageable) {
        logger.info("Finding persons by name");

        Page<Person> personPage = personRepository.findPersonsByName(firstName, pageable);

        Page<PersonDto> personDtosPage = personPage.map(PersonMapper.INSTANCE::toPersonDto);

        personDtosPage.map(
                personDto ->
                        personDto.add(
                                linkTo(methodOn(PersonController.class).findById(personDto.getKey()))
                                        .withSelfRel()
                        ));

        Link link = linkTo(
                methodOn(PersonController.class)
                        .findAll(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                "asc"))
                .withSelfRel();

        return assembler.toModel(personDtosPage, link);
    }

    public PersonDto findById(Long id) {
        logger.info("Finding a person");
        Person foundPerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        PersonDto personDto = PersonMapper.INSTANCE.toPersonDto(foundPerson);
        personDto.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return personDto;
    }

    public PersonDto create(PersonDto personDto) {
        logger.info("Creating a person");
        if (Objects.isNull(personDto)) throw new RequiredObjectNullException();
        Person newPerson = personRepository.save(PersonMapper.INSTANCE.toPerson(personDto));
        PersonDto newPersonDto = PersonMapper.INSTANCE.toPersonDto(newPerson);
        newPersonDto.add(linkTo(methodOn(PersonController.class).findById(newPerson.getId())).withSelfRel());
        return newPersonDto;
    }

    public PersonDtoV2 createV2(PersonDtoV2 personDtoV2) {
        logger.info("Creating a person V2");
        var newPerson = PersonMapper.INSTANCE.toPerson(personDtoV2);
        return PersonMapper.INSTANCE.toPersonDtoV2(personRepository.save(newPerson));
    }

    public PersonDto update(PersonDto personDto) {
        logger.info("Updating a person");
        if (Objects.isNull(personDto)) throw new RequiredObjectNullException();
        var foundPerson = personRepository.findById(personDto.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        foundPerson.setFirstName(personDto.getFirstName());
        foundPerson.setLastName(personDto.getLastName());
        foundPerson.setAddress(personDto.getAddress());
        foundPerson.setGender(personDto.getGender());
        Person updatedPerson = personRepository.save(foundPerson);
        PersonDto updatedPersonDto = PersonMapper.INSTANCE.toPersonDto(updatedPerson);
        updatedPersonDto.add(linkTo(methodOn(PersonController.class).findById(updatedPerson.getId())).withSelfRel());
        return updatedPersonDto;
    }

    // A nivel de metodo ou classe
    @Transactional // Garantir ACID porque spring data nao gerencia a transacao
    public PersonDto disablePerson(Long id) {
        logger.info("Disabling a person");
        personRepository.disablePerson(id);
        Person disabledPerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        PersonDto personDto = PersonMapper.INSTANCE.toPersonDto(disabledPerson);
        personDto.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return personDto;
    }

    public void deleteById(Long id) {
        logger.info("Deleting a person");
        var foundPerson = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        personRepository.delete(foundPerson);
    }


}
