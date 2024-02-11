package br.com.erudio.erudioapi.integration.dto.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@XmlRootElement
public class WrapperPersonDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private PersonEmbeddedDto embeddedDto;

    public WrapperPersonDto() {
    }

    public PersonEmbeddedDto getEmbeddedDto() {
        return embeddedDto;
    }

    public void setEmbeddedDto(PersonEmbeddedDto embeddedDto) {
        this.embeddedDto = embeddedDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WrapperPersonDto that = (WrapperPersonDto) o;
        return Objects.equals(embeddedDto, that.embeddedDto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(embeddedDto);
    }
}
