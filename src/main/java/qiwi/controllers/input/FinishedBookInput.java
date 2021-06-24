package qiwi.controllers.input;

import lombok.Data;

import java.sql.Date;

@Data
public class FinishedBookInput {
    private String author;
    private String name;
    private Date start;
    private Date end;
    private Date found;

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

    public Date getFound() {
        return found;
    }

}
