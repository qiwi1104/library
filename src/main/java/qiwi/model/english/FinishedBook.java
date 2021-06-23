package qiwi.model.english;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "`finished_books_english`")
public class FinishedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String author;
    private Date start;
    private Date end;
    @Column(name = "start_description")
    private String startDescription;
    @Column(name = "end_description")
    private String endDescription;
    private Date found;
    @Column(name = "found_description")
    private String foundDescription;

    public FinishedBook() {}

    public FinishedBook(String name, String author, Date start, Date end, String startDescription, String endDescription, Date found, String foundDescription) {
        this.name = name;
        this.author = author;
        this.start = start;
        this.end = end;
        this.startDescription = startDescription;
        this.endDescription = endDescription;
        this.found = found;
        this.foundDescription = foundDescription;
    }

    public Integer getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public String getStartDescription() {
        return startDescription;
    }

    public String getEndDescription() {
        return endDescription;
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

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setStartDescription(String startDescription) {
        this.startDescription = startDescription;
    }

    public void setEndDescription(String endDescription) {
        this.endDescription = endDescription;
    }

    public void setFound(Date found) {
        this.found = found;
    }

    public void setFoundDescription(String foundDescription) {
        this.foundDescription = foundDescription;
    }

    @Override
    public String toString() {
        return id + " " + author + " " + name;
    }
}
