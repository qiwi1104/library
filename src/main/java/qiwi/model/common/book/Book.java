package qiwi.model.common.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import qiwi.controllers.common.BookController;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@MappedSuperclass
public abstract class Book {
    @JsonIgnore
    @Id
    protected Integer id;
    protected String author;
    protected String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "M/d/yy")
    protected Date found;
    protected String description;

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

    @Override
    public int hashCode() {
        return name.hashCode() + author.hashCode();
    }

    /*
     * Used in toString() to replace "\"" so that an exception is not thrown if that symbol has been encountered
     * Also reduces amount of code
     * */
    protected Map<String, String> setKeys() {
        String nameStr = name.replace("\"", "\\\"");
        String descriptionStr = description
                .replace("\"", "\\\"")
                .replace("\n", "\\n");;
        String foundStr = BookController.TimeFormat.formatTime("yyyy-M-d", "M/d/yy", found.toString());

        return new HashMap<>(Map.of(
                "name", nameStr,
                "description", descriptionStr,
                "found", foundStr)
        );
    }

    /*
    * Used when converting entity to JSON Object
    * */
    @Override
    public String toString() {
        Map<String, String> keys = setKeys();

        return "{" +
                "\"author\": \"" + author + "\"" +
                ", \"name\": \"" + keys.get("name") + "\"" +
                ", \"found\": \"" + keys.get("found") + "\"" +
                ", \"description\": \"" + keys.get("description") + "\"" +
                "}";
    }
}
