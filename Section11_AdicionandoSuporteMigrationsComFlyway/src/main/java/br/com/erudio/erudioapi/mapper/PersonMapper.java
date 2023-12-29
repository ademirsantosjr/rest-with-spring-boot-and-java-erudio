package br.com.erudio.erudioapi.mapper;

import br.com.erudio.erudioapi.data.vo.v1.PersonDto;
import br.com.erudio.erudioapi.data.vo.v2.PersonDtoV2;
import br.com.erudio.erudioapi.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Date;

@Mapper
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class );

    PersonDto toPersonDto(Person person);
    Person toPerson(PersonDto personDto);
    @Mapping(target = "birthDay", expression = "java(new java.util.Date())")
    PersonDtoV2 toPersonDtoV2(Person person);
    Person toPerson(PersonDtoV2 personDtoV2);

}
