package br.com.erudio.erudioapi.unittests.mapper;
import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.erudio.erudioapi.data.vo.v1.PersonDto;
import br.com.erudio.erudioapi.mapper.PersonMapper;
import br.com.erudio.erudioapi.model.Person;
import br.com.erudio.erudioapi.unittests.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PersonMapperTest {

    MockPerson inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockPerson();
    }

    @Test
    public void parseEntityToVOTest() {
        PersonDto output = PersonMapper.INSTANCE.toPersonDto(inputObject.mockEntity());
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("First Name Test 0", output.getFirstName());
        assertEquals("Last Name Test 0", output.getLastName());
        assertEquals("Address Test 0", output.getAddress());
        assertEquals("Male", output.getGender());
    }

    @Test
    public void parseEntityListToVOListTest() {
        List<PersonDto> outputList = inputObject.mockEntityList()
                .stream()
                .map(PersonMapper.INSTANCE::toPersonDto)
                .toList();

        PersonDto outputZero = outputList.getFirst();

        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("First Name Test 0", outputZero.getFirstName());
        assertEquals("Last Name Test 0", outputZero.getLastName());
        assertEquals("Address Test 0", outputZero.getAddress());
        assertEquals("Male", outputZero.getGender());

        PersonDto outputSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("First Name Test 7", outputSeven.getFirstName());
        assertEquals("Last Name Test 7", outputSeven.getLastName());
        assertEquals("Address Test 7", outputSeven.getAddress());
        assertEquals("Female", outputSeven.getGender());

        PersonDto outputTwelve = outputList.get(12);

        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("First Name Test 12", outputTwelve.getFirstName());
        assertEquals("Last Name Test 12", outputTwelve.getLastName());
        assertEquals("Address Test 12", outputTwelve.getAddress());
        assertEquals("Male", outputTwelve.getGender());
    }

    @Test
    public void parseVOToEntityTest() {
        Person output = PersonMapper.INSTANCE.toPerson(inputObject.mockVO());
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("First Name Test 0", output.getFirstName());
        assertEquals("Last Name Test 0", output.getLastName());
        assertEquals("Address Test 0", output.getAddress());
        assertEquals("Male", output.getGender());
    }

    @Test
    public void parserVOListToEntityListTest() {
        List<Person> outputList = inputObject.mockVOList()
                .stream()
                .map(PersonMapper.INSTANCE::toPerson)
                .toList();

        Person outputZero = outputList.getFirst();

        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("First Name Test 0", outputZero.getFirstName());
        assertEquals("Last Name Test 0", outputZero.getLastName());
        assertEquals("Address Test 0", outputZero.getAddress());
        assertEquals("Male", outputZero.getGender());

        Person outputSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("First Name Test 7", outputSeven.getFirstName());
        assertEquals("Last Name Test 7", outputSeven.getLastName());
        assertEquals("Address Test 7", outputSeven.getAddress());
        assertEquals("Female", outputSeven.getGender());

        Person outputTwelve = outputList.get(12);

        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("First Name Test 12", outputTwelve.getFirstName());
        assertEquals("Last Name Test 12", outputTwelve.getLastName());
        assertEquals("Address Test 12", outputTwelve.getAddress());
        assertEquals("Male", outputTwelve.getGender());
    }
}
