package br.com.erudio.erudioapi.service;

import br.com.erudio.erudioapi.data.vo.v1.BookDto;
import br.com.erudio.erudioapi.data.vo.v1.PersonDto;
import br.com.erudio.erudioapi.exceptions.RequiredObjectNullException;
import br.com.erudio.erudioapi.mocks.MockBook;
import br.com.erudio.erudioapi.model.Book;
import br.com.erudio.erudioapi.model.Person;
import br.com.erudio.erudioapi.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    MockBook input;

    @InjectMocks
    private BookService bookService;

    @Mock
    BookRepository bookRepository;

    @BeforeEach
    public void setupMocks() {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findByIdTest() {
        Book book = input.mockEntity(1);
        book.setId(1L);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        BookDto foundBookDto = bookService.findById(1L);

        assertNotNull(foundBookDto);
        assertNotNull(foundBookDto.getKey());
        assertNotNull(foundBookDto.getLinks());

        assertTrue(foundBookDto.toString().contains("links: [</api/books/1>;rel=\"self\"]"));

        assertEquals("Author Test 1", foundBookDto.getAuthor());
        assertEquals("Title Test 1", foundBookDto.getTitle());
    }

    @Test
    public void createTest() {
        Book book = input.mockEntity(1);

        Book persitedBook = book;
        persitedBook.setId(1L);

        BookDto bookDto = input.mockDto(1);
        bookDto.setKey(1L);

        when(bookRepository.save(book)).thenReturn(persitedBook);

        BookDto newBookDto = bookService.create(bookDto);

        assertNotNull(newBookDto);
        assertNotNull(newBookDto.getKey());
        assertNotNull(newBookDto.getLinks());

        assertTrue(newBookDto.toString().contains("links: [</api/books/1>;rel=\"self\"]"));

        assertEquals("Author Test 1", newBookDto.getAuthor());
        assertEquals("Title Test 1", newBookDto.getTitle());
    }

    @Test
    public void createNullPersonTest() {
        Exception exception = assertThrows(RequiredObjectNullException.class, () -> {
            bookService.create(null);
        });

        String expectedMessage = "Persisting null object is not allowed";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void updateTest() {
        Book book = input.mockEntity(1);

        Book foundBook = book;
        foundBook.setId(1L);

        BookDto bookDto = input.mockDto(1);
        bookDto.setKey(1L);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(foundBook);

        BookDto updatedBookDto = bookService.update(bookDto);

        assertNotNull(updatedBookDto);
        assertNotNull(updatedBookDto.getKey());
        assertNotNull(updatedBookDto.getLinks());

        assertTrue(updatedBookDto.toString().contains("links: [</api/books/1>;rel=\"self\"]"));

        assertEquals("Author Test 1", updatedBookDto.getAuthor());
        assertEquals("Title Test 1", updatedBookDto.getTitle());
    }

    @Test
    public void updateNullBookTest() {
        Exception exception = assertThrows(RequiredObjectNullException.class, () -> {
            bookService.update(null);
        });

        String expectedMessage = "Persisting null object is not allowed";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void deleteByIdTest() {
        Book book = input.mockEntity(1);
        book.setId(1L);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        bookService.deleteById(1L);
    }

    @Test
    public void findAllTest() {
        List<Book> books = input.mockEntityList();

        when(bookRepository.findAll()).thenReturn(books);

        List<BookDto> bookDtoList = bookService.findAll();

        assertNotNull(bookDtoList);
        assertEquals(14, bookDtoList.size());

        BookDto bookDtoOne = bookDtoList.get(1);
        assertEquals("Author Test 1", bookDtoOne.getAuthor());
        assertEquals("Title Test 1", bookDtoOne.getTitle());

        BookDto bookDtoEight = bookDtoList.get(8);
        assertEquals("Author Test 8", bookDtoEight.getAuthor());
        assertEquals("Title Test 8", bookDtoEight.getTitle());
    }
}
