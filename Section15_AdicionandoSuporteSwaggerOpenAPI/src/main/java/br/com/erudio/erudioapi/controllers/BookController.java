package br.com.erudio.erudioapi.controllers;

import br.com.erudio.erudioapi.data.vo.v1.BookDto;
import br.com.erudio.erudioapi.service.BookService;
import br.com.erudio.erudioapi.util.MediaTypeCustom;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Endpoint for managing books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping(
            produces = {
                MediaTypeCustom.APPLICATION_JSON,
                MediaTypeCustom.APPLICATION_XML,
                MediaTypeCustom.APPLICATION_YAML
            }
    )
    @Operation(
            summary = "Find all books",
            description = "List all registered books in JSON, XML or YAML formats",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(
                                                    schema = @Schema(implementation = BookDto.class)
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
    public List<BookDto> findAll() {
        return bookService.findAll();
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
            summary = "Find a book by id",
            description = "Find a book by its id in JSON, XML or YAML formats.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = BookDto.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public BookDto findById(@PathVariable(value = "id") Long id) {
        return bookService.findById(id);
    }

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
            summary = "Save a book",
            description = "Save a book in JSON, XML or YAML formats.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = BookDto.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public BookDto create(@RequestBody BookDto bookDto) {
        return bookService.create(bookDto);
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
            summary = "Update a book",
            description = "Update a book consuming and producing JSON, XML or YAML media types.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = BookDto.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public BookDto udpate(@RequestBody BookDto bookDto) {
        return bookService.update(bookDto);
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
            summary = "Delete a book",
            description = "Delete a book by its id producing JSON, XML or YAML media types.",
            tags = {"Books"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content()),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
