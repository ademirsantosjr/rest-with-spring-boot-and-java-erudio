package br.com.erudio.erudioapi.mapper;

import br.com.erudio.erudioapi.dto.v1.BookDto;
import br.com.erudio.erudioapi.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class );

    @Mapping(target = "key", source = "id")
    BookDto toBookDto(Book book);

    @Mapping(target = "id", source = "key")
    Book toBook(BookDto bookDto);
}
