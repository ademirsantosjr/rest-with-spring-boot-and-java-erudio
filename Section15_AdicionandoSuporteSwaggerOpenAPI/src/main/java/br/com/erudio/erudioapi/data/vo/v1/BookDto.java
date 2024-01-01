package br.com.erudio.erudioapi.data.vo.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@JsonPropertyOrder({"id", "author", "title", "launch_date", "price"})
public class BookDto extends RepresentationModel<BookDto> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long key;
    private String author;
    private String title;
    @JsonProperty("launch_date")
    private Date launchDate;
    private Double price;

    public BookDto() {
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDto bookDto = (BookDto) o;
        return Objects.equals(key, bookDto.key) &&
                Objects.equals(author, bookDto.author) &&
                Objects.equals(title, bookDto.title) &&
                Objects.equals(launchDate, bookDto.launchDate) &&
                Objects.equals(price, bookDto.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, author, title, launchDate, price);
    }
}
