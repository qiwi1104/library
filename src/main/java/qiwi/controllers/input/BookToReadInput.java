package qiwi.controllers.input;

import lombok.Data;

import java.sql.Date;

@Data
public class BookToReadInput {
    private String author;
    private String name;
    private Date found;
    private String foundDescription;

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
}
