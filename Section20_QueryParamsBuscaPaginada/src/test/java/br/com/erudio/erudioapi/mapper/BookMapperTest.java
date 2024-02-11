package br.com.erudio.erudioapi.mapper;

import br.com.erudio.erudioapi.dto.v1.BookDto;
import br.com.erudio.erudioapi.mocks.MockBook;
import br.com.erudio.erudioapi.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookMapperTest {

    MockBook inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockBook();
    }

    @Test
    public void parseEntityToVOTest() {
        BookDto output = BookMapper.INSTANCE.toBookDto(inputObject.mockEntity());
        assertEquals(Long.valueOf(0L), output.getKey());
        assertEquals("Author Test 0", output.getAuthor());
        assertEquals("Title Test 0", output.getTitle());
    }

    @Test
    public void parseEntityListToVOListTest() {
        List<BookDto> outputList = inputObject.mockEntityList()
                .stream()
                .map(BookMapper.INSTANCE::toBookDto)
                .toList();

        BookDto outputZero = outputList.getFirst();

        assertEquals(Long.valueOf(0L), outputZero.getKey());
        assertEquals("Author Test 0", outputZero.getAuthor());
        assertEquals("Title Test 0", outputZero.getTitle());

        BookDto outputSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), outputSeven.getKey());
        assertEquals("Author Test 7", outputSeven.getAuthor());
        assertEquals("Title Test 7", outputSeven.getTitle());

        BookDto outputTwelve = outputList.get(12);

        assertEquals(Long.valueOf(12L), outputTwelve.getKey());
        assertEquals("Author Test 12", outputTwelve.getAuthor());
        assertEquals("Title Test 12", outputTwelve.getTitle());
    }

    @Test
    public void parseVOToEntityTest() {
        Book output = BookMapper.INSTANCE.toBook(inputObject.mockDto());
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Author Test 0", output.getAuthor());
        assertEquals("Title Test 0", output.getTitle());
    }

    @Test
    public void parserVOListToEntityListTest() {
        List<Book> outputList = inputObject.mockDtoList()
                .stream()
                .map(BookMapper.INSTANCE::toBook)
                .toList();

        Book outputZero = outputList.getFirst();

        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Author Test 0", outputZero.getAuthor());
        assertEquals("Title Test 0", outputZero.getTitle());


        Book outputSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Author Test 7", outputSeven.getAuthor());
        assertEquals("Title Test 7", outputSeven.getTitle());

        Book outputTwelve = outputList.get(12);

        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Author Test 12", outputTwelve.getAuthor());
        assertEquals("Title Test 12", outputTwelve.getTitle());
    }
}
