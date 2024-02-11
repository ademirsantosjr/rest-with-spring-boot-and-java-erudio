package br.com.erudio.erudioapi.controllers;

import br.com.erudio.erudioapi.dto.v1.PersonDto;
import br.com.erudio.erudioapi.dto.v2.PersonDtoV2;
import br.com.erudio.erudioapi.service.PersonService;
import br.com.erudio.erudioapi.util.MediaTypeCustom;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @CrossOrigin // for every endpoint
@RestController
@RequestMapping("/api/persons")
@Tag(name = "Persons", description = "Endpoint for managing persons")
public class PersonController {

    @Autowired
    private PersonService service;

    // @CrossOrigin(origins = {"http://localhost:8080"})
    @GetMapping(
            produces = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YAML
            }
    )
    @Operation(
            summary = "Find all persons",
            description = "List of all registered persons in JSON, XML or YAML formats",
            tags = {"Persons"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(
                                                    schema = @Schema(implementation = PersonDto.class)
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<PagedModel<EntityModel<PersonDto>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page, size, Sort.by(sortDirection, "firstName"));

        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping(
            value = "/findPersonsByName/{firstName}",
            produces = {
                    MediaTypeCustom.APPLICATION_JSON,
                    MediaTypeCustom.APPLICATION_XML,
                    MediaTypeCustom.APPLICATION_YAML
            }
    )
    @Operation(
            summary = "Find persons by first name",
            description = "List of registered persons with a specific first name in JSON, XML or YAML formats",
            tags = {"Persons"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(
                                                    schema = @Schema(implementation = PersonDto.class)
                                            )
                                    )
                            }
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<PagedModel<EntityModel<PersonDto>>> findByName(
            @PathVariable("firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page, size, Sort.by(sortDirection, "firstName"));

        return ResponseEntity.ok(service.findPersonsByName(firstName, pageable));
    }

    @GetMapping(
            value = "/{id}",
            produces = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YAML
            }
    )
    @Operation(
            summary = "Find a person by id",
            description = "Find a person by its id in JSON, XML or YAML formats.",
            tags = {"Persons"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PersonDto.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public PersonDto findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    // @CrossOrigin(origins = {"http://localhost:8080", "https://erudio.com.br"})
    @PostMapping(
            consumes = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YAML
            },
            produces = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YAML
            }
    )
    @Operation(
            summary = "Save a person",
            description = "Save a person in JSON, XML or YAML formats.",
            tags = {"Persons"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PersonDto.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public PersonDto create(@RequestBody PersonDto personDto) {
        return service.create(personDto);
    }

    @PostMapping(
            value = "/v2",
            consumes = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YAML
            },
            produces = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YAML
            }
    )
    @Operation(
            summary = "Save a person",
            description = "Save a person in JSON, XML or YAML formats.",
            tags = {"Persons"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PersonDtoV2.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public PersonDtoV2 createV2(@RequestBody PersonDtoV2 personDtoV2) {
        return service.createV2(personDtoV2);
    }

    @PutMapping(
            consumes = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YAML
            },
            produces = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YAML
            }
    )
    @Operation(
            summary = "Update a person",
            description = "Update a person consuming and producing JSON, XML or YAML media types.",
            tags = {"Persons"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PersonDtoV2.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public PersonDto udpate(@RequestBody PersonDto personDto) {
        return service.update(personDto);
    }

    @PatchMapping(
            value = "/{id}",
            produces = {
                    MediaTypeCustom.APPLICATION_JSON,
                    MediaTypeCustom.APPLICATION_XML,
                    MediaTypeCustom.APPLICATION_YAML
            }
    )
    @Operation(
            summary = "Disable a person by its id",
            description = "Disable a person by its id in JSON, XML or YAML formats.",
            tags = {"Persons"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PersonDto.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public PersonDto disablePerson(@PathVariable(value = "id") Long id) {
        return service.disablePerson(id);
    }

    @DeleteMapping(
            value = "/{id}",
            produces = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YAML
            }
    )
    @Operation(
            summary = "Delete a person",
            description = "Delete a person by its id producing JSON, XML or YAML media types.",
            tags = {"Persons"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content()),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
