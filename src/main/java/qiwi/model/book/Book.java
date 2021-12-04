package qiwi.model.book;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Date;

@MappedSuperclass
public abstract class Book {
    @Id
    protected Integer id;
    protected String author;
    protected String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "M/d/yy")
    protected Date found;
    protected String description;
    protected String language;

    public Integer getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public Date getFound() {
        return found;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setFound(Date found) {
        this.found = found;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + author.hashCode();
    }
}
