package br.com.erudio.erudioapi.mocks;

import br.com.erudio.erudioapi.dto.v1.PersonDto;
import br.com.erudio.erudioapi.model.Person;

import java.util.ArrayList;
import java.util.List;

public class MockPerson {

    public Person mockEntity() {
        return mockEntity(0);
    }

    public PersonDto mockDto() {
        return mockDto(0);
    }

    public List<Person> mockEntityList() {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
    }

    public List<PersonDto> mockDtoList() {
        List<PersonDto> personsDtos = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            personsDtos.add(mockDto(i));
        }
        return personsDtos;
    }

    public Person mockEntity(Integer number) {
        Person person = new Person();
        person.setAddress("Address Test " + number);
        person.setFirstName("First Name Test " + number);
        person.setLastName("Last Name Test " + number);
        person.setGender(((number % 2) == 0) ? "Male" : "Female");
        person.setId(number.longValue());
        person.setLastName("Last Name Test " + number);
        return person;
    }

    public PersonDto mockDto(Integer number) {
        PersonDto personDto = new PersonDto();
        personDto.setAddress("Address Test " + number);
        personDto.setFirstName("First Name Test " + number);
        personDto.setLastName("Last Name Test " + number);
        personDto.setGender(((number % 2) == 0) ? "Male" : "Female");
        personDto.setKey(number.longValue());
        personDto.setLastName("Last Name Test " + number);
        return personDto;
    }
}
