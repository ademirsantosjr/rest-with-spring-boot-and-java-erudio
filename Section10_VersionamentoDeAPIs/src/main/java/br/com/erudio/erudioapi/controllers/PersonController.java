package br.com.erudio.erudioapi.controllers;

import br.com.erudio.erudioapi.data.vo.v1.PersonDto;
import br.com.erudio.erudioapi.data.vo.v2.PersonDtoV2;
import br.com.erudio.erudioapi.service.PersonService;
import br.com.erudio.erudioapi.util.MediaTypeCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService service;

    @GetMapping(
            produces = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YML
            })
    public List<PersonDto> findAll() {
        return service.findAll();
    }

    @GetMapping(
            value = "/{id}",
            produces = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YML
            })
    public PersonDto findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @PostMapping(
            consumes = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YML
            },
            produces = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YML
            })
    public PersonDto create(@RequestBody PersonDto personDto) {
        return service.create(personDto);
    }

    @PostMapping(
            value = "/v2",
            consumes = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YML
            },
            produces = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YML
            })
    public PersonDtoV2 createV2(@RequestBody PersonDtoV2 personDtoV2) {
        return service.createV2(personDtoV2);
    }

    @PutMapping(
            consumes = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YML
            },
            produces = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YML
            })
    public PersonDto udpate(@RequestBody PersonDto personDto) {
        return service.update(personDto);
    }

    @DeleteMapping(
            value = "/{id}",
            produces = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YML
            })
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
