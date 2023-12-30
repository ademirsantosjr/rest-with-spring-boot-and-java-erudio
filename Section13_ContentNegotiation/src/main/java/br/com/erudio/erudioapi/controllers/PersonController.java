package br.com.erudio.erudioapi.controllers;

import br.com.erudio.erudioapi.data.vo.v1.PersonDto;
import br.com.erudio.erudioapi.data.vo.v2.PersonDtoV2;
import br.com.erudio.erudioapi.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService service;

    @GetMapping
    public List<PersonDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public PersonDto findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    public PersonDto create(@RequestBody PersonDto personDto) {
        return service.create(personDto);
    }

    @PostMapping("/v2")
    public PersonDtoV2 createV2(@RequestBody PersonDtoV2 personDtoV2) {
        return service.createV2(personDtoV2);
    }

    @PutMapping
    public PersonDto udpate(@RequestBody PersonDto personDto) {
        return service.update(personDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
