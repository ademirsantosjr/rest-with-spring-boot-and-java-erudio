package br.com.erudio.erudioapi.service;

import br.com.erudio.erudioapi.controllers.BookController;
import br.com.erudio.erudioapi.controllers.PersonController;
import br.com.erudio.erudioapi.data.vo.v1.BookDto;
import br.com.erudio.erudioapi.data.vo.v1.PersonDto;
import br.com.erudio.erudioapi.data.vo.v2.PersonDtoV2;
import br.com.erudio.erudioapi.exceptions.RequiredObjectNullException;
import br.com.erudio.erudioapi.exceptions.ResourceNotFoundException;
import br.com.erudio.erudioapi.mapper.BookMapper;
import br.com.erudio.erudioapi.mapper.PersonMapper;
import br.com.erudio.erudioapi.model.Book;
import br.com.erudio.erudioapi.model.Person;
import br.com.erudio.erudioapi.repository.BookRepository;
import br.com.erudio.erudioapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    private final Logger logger = Logger.getLogger(BookService.class.getName());
    @Autowired
    private BookRepository bookRepository;

    public List<BookDto> findAll() {
        logger.info("Finding a books");
        List<BookDto> bookDtoList = bookRepository.findAll()
                .stream()
                .map(BookMapper.INSTANCE::toBookDto)
                .toList();
        bookDtoList.forEach(
                bookDto ->
                        bookDto.add(
                                linkTo(
                                        methodOn(BookController.class).findById(bookDto.getKey())
                                ).withSelfRel()
                        )
        );
        return bookDtoList;
    }

    public BookDto findById(Long id) {
        logger.info("Finding a book");
        Book foundBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        BookDto bookDto = BookMapper.INSTANCE.toBookDto(foundBook);
        bookDto.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return bookDto;
    }

    public BookDto create(BookDto bookDto) {
        logger.info("Creating a book");
        if (Objects.isNull(bookDto)) throw new RequiredObjectNullException();
        Book book = bookRepository.save(BookMapper.INSTANCE.toBook(bookDto));
        BookDto newBookDto = BookMapper.INSTANCE.toBookDto(book);
        newBookDto.add(linkTo(methodOn(BookController.class).findById(book.getId())).withSelfRel());
        return newBookDto;
    }

    public BookDto update(BookDto bookDto) {
        logger.info("Updating a book");
        if (Objects.isNull(bookDto)) throw new RequiredObjectNullException();
        var foudnBook = bookRepository.findById(bookDto.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
        foudnBook.setAuthor(bookDto.getAuthor());
        foudnBook.setTitle(bookDto.getTitle());
        foudnBook.setLaunchDate(bookDto.getLaunchDate());
        foudnBook.setPrice(bookDto.getPrice());
        Book updatedBook = bookRepository.save(foudnBook);
        BookDto updatedBookDto = BookMapper.INSTANCE.toBookDto(updatedBook);
        updatedBookDto.add(linkTo(methodOn(BookController.class).findById(updatedBook.getId())).withSelfRel());
        return updatedBookDto;
    }

    public void deleteById(Long id) {
        logger.info("Deleting a book");
        var foundBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        bookRepository.delete(foundBook);
    }


}
