package br.com.erudio.erudioapi.service;

import br.com.erudio.erudioapi.dto.v1.PersonDto;
import br.com.erudio.erudioapi.exceptions.RequiredObjectNullException;
import br.com.erudio.erudioapi.mocks.MockPerson;
import br.com.erudio.erudioapi.model.Person;
import br.com.erudio.erudioapi.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    MockPerson input;

    @InjectMocks
    private PersonService personService;

    @Mock
    PersonRepository personRepository;

    @BeforeEach
    public void setupMocks() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findByIdTest() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));

        PersonDto foundPersonDto = personService.findById(1L);

        assertNotNull(foundPersonDto);
        assertNotNull(foundPersonDto.getKey());
        assertNotNull(foundPersonDto.getLinks());

        assertTrue(foundPersonDto.toString().contains("links: [</api/persons/1>;rel=\"self\"]"));

        assertEquals("First Name Test 1", foundPersonDto.getFirstName());
        assertEquals("Last Name Test 1", foundPersonDto.getLastName());
        assertEquals("Female", foundPersonDto.getGender());
        assertEquals("Address Test 1", foundPersonDto.getAddress());
    }

    @Test
    public void createTest() {
        Person person = input.mockEntity(1);

        Person persitedPerson = person;
        persitedPerson.setId(1L);

        PersonDto personDto = input.mockDto(1);
        personDto.setKey(1L);

        when(personRepository.save(person)).thenReturn(persitedPerson);

        PersonDto newPersonDto = personService.create(personDto);

        assertNotNull(newPersonDto);
        assertNotNull(newPersonDto.getKey());
        assertNotNull(newPersonDto.getLinks());

        assertTrue(newPersonDto.toString().contains("links: [</api/persons/1>;rel=\"self\"]"));

        assertEquals("First Name Test 1", newPersonDto.getFirstName());
        assertEquals("Last Name Test 1", newPersonDto.getLastName());
        assertEquals("Female", newPersonDto.getGender());
        assertEquals("Address Test 1", newPersonDto.getAddress());
    }

    @Test
    public void createNullPersonTest() {
        Exception exception = assertThrows(RequiredObjectNullException.class, () -> {
            personService.create(null);
        });

        String expectedMessage = "Persisting null object is not allowed";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void updateTest() {
        Person person = input.mockEntity(1);

        Person foundPerson = person;
        foundPerson.setId(1L);

        PersonDto personDto = input.mockDto(1);
        personDto.setKey(1L);

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));
        when(personRepository.save(person)).thenReturn(foundPerson);

        PersonDto updatedPersonDto = personService.update(personDto);

        assertNotNull(updatedPersonDto);
        assertNotNull(updatedPersonDto.getKey());
        assertNotNull(updatedPersonDto.getLinks());

        assertTrue(updatedPersonDto.toString().contains("links: [</api/persons/1>;rel=\"self\"]"));

        assertEquals("First Name Test 1", updatedPersonDto.getFirstName());
        assertEquals("Last Name Test 1", updatedPersonDto.getLastName());
        assertEquals("Female", updatedPersonDto.getGender());
        assertEquals("Address Test 1", updatedPersonDto.getAddress());
    }

    @Test
    public void updateNullPersonTest() {
        Exception exception = assertThrows(RequiredObjectNullException.class, () -> {
            personService.update(null);
        });

        String expectedMessage = "Persisting null object is not allowed";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void deleteByIdTest() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));

        personService.deleteById(1L);
    }

    @Test
    public void findAllTest() {
        List<Person> persons = input.mockEntityList();

        when(personRepository.findAll()).thenReturn(persons);

        List<PersonDto> personDtoList = personService.findAll();

        assertNotNull(personDtoList);
        assertEquals(14, personDtoList.size());

        PersonDto personDtoOne = personDtoList.get(1);
        assertEquals("First Name Test 1", personDtoOne.getFirstName());
        assertEquals("Last Name Test 1", personDtoOne.getLastName());
        assertEquals("Female", personDtoOne.getGender());
        assertEquals("Address Test 1", personDtoOne.getAddress());

        PersonDto personDtoSeven = personDtoList.get(8);
        assertEquals("First Name Test 8", personDtoSeven.getFirstName());
        assertEquals("Last Name Test 8", personDtoSeven.getLastName());
        assertEquals("Male", personDtoSeven.getGender());
        assertEquals("Address Test 8", personDtoSeven.getAddress());
    }
}
