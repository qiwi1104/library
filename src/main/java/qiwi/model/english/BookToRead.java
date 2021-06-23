package qiwi.model.english;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "`books_to_read_english`")
public class BookToRead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String author;
    private Date found;
    @Column(name = "found_description")
    private String foundDescription;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getFound() {
        return found;
    }

    public void setFound(Date found) {
        this.found = found;
    }

    public String getFoundDescription() {
        return foundDescription;
    }

    public void setFoundDescription(String foundDescription) {
        this.foundDescription = foundDescription;
    }
}
