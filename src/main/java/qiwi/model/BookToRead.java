package qiwi.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Date;

@MappedSuperclass
public class BookToRead {
    @Id
    private Integer id;
    private String author;
    private String name;
    private Date found;
    @Column(name = "found_description")
    private String foundDescription;

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

    public String getFoundDescription() {
        return foundDescription;
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

    public void setFoundDescription(String foundDescription) {
        this.foundDescription = foundDescription;
    }
}
