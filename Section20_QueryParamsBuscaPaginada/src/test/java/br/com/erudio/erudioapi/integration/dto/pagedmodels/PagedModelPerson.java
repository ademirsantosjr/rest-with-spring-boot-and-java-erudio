package br.com.erudio.erudioapi.integration.dto.pagedmodels;

import br.com.erudio.erudioapi.integration.dto.PersonTestDto;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@XmlRootElement
public class PagedModelPerson implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "content")
    private List<PersonTestDto> content;

    public PagedModelPerson() {
    }

    public List<PersonTestDto> getContent() {
        return content;
    }

    public void setContent(List<PersonTestDto> content) {
        this.content = content;
    }
}
