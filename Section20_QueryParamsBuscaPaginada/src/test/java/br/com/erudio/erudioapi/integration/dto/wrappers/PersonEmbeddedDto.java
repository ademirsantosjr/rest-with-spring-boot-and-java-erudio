package br.com.erudio.erudioapi.integration.dto.wrappers;

import br.com.erudio.erudioapi.dto.v1.PersonDto;
import br.com.erudio.erudioapi.integration.dto.PersonTestDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PersonEmbeddedDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("personDtoList")
    private List<PersonTestDto> personTestDtoList;

    public PersonEmbeddedDto() {
    }

    public List<PersonTestDto> getPersonTestDtoList() {
        return personTestDtoList;
    }

    public void setPersonTestDtoList(List<PersonTestDto> personTestDtoList) {
        this.personTestDtoList = personTestDtoList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonEmbeddedDto that = (PersonEmbeddedDto) o;
        return Objects.equals(personTestDtoList, that.personTestDtoList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personTestDtoList);
    }
}
