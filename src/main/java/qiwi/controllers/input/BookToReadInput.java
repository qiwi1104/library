package qiwi.controllers.input;

import lombok.Data;

import java.sql.Date;

@Data
public class BookToReadInput {
    private Integer id;

    private String author;
    private String name;
    private Date found;
    private Date start;
    private Date end;
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

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public String getFoundDescription() {
        return foundDescription;
    }
}
