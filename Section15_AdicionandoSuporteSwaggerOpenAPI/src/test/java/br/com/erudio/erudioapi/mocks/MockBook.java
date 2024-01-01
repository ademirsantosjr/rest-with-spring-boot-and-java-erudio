package br.com.erudio.erudioapi.mocks;

import br.com.erudio.erudioapi.data.vo.v1.BookDto;
import br.com.erudio.erudioapi.data.vo.v1.PersonDto;
import br.com.erudio.erudioapi.model.Book;
import br.com.erudio.erudioapi.model.Person;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {

    public Book mockEntity() {
        return mockEntity(0);
    }

    public BookDto mockDto() {
        return mockDto(0);
    }

    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookDto> mockDtoList() {
        List<BookDto> bookDtos = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            bookDtos.add(mockDto(i));
        }
        return bookDtos;
    }

    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setAuthor("Author Test " + number);
        book.setTitle("Title Test " + number);
        book.setLaunchDate(
                Date.from(
                        Instant
                                .parse("2023-11-30T00:00:00.00Z")
                                .minusSeconds(number * 86400)
                )
        );
        book.setPrice(number + 10D);
        book.setId(number.longValue());
        return book;
    }

    public BookDto mockDto(Integer number) {
        BookDto bookDto = new BookDto();
        bookDto.setAuthor("Author Test " + number);
        bookDto.setTitle("Title Test " + number);
        bookDto.setLaunchDate(
                Date.from(
                        Instant
                                .parse("2023-11-30T00:00:00.00Z")
                                .minusSeconds(number * 86400)
                )
        );
        bookDto.setPrice(number + 10D);
        bookDto.setKey(number.longValue());
        return bookDto;
    }
}
