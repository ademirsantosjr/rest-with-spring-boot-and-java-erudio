package br.com.erudio.erudioapi.mapper;

import br.com.erudio.erudioapi.data.vo.v1.BookDto;
import br.com.erudio.erudioapi.data.vo.v1.PersonDto;
import br.com.erudio.erudioapi.data.vo.v2.PersonDtoV2;
import br.com.erudio.erudioapi.model.Book;
import br.com.erudio.erudioapi.model.Person;
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
