package qiwi.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Date;

@MappedSuperclass
public class FinishedBook {
    @Id
    private Integer id;
    private String author;
    private String name;
    private Date start;
    private Date end;
    @Column(name = "start_description")
    private String startDescription;
    @Column(name = "end_description")
    private String endDescription;
    private Date found;
    @Column(name = "found_description")
    private String foundDescription;

    public FinishedBook() {
    }

    public FinishedBook(String author, String name, Date start, Date end, String startDescription, String endDescription,
                        Date found, String foundDescription) {
        this.setName(name);
        this.setAuthor(author);
        this.setStart(start);
        this.setEnd(end);
        this.setStartDescription(startDescription);
        this.setEndDescription(endDescription);
        this.setFound(found);
        this.setFoundDescription(foundDescription);
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

    public Date getFound() {
        return found;
    }

    public String getFoundDescription() {
        return foundDescription;
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
}
