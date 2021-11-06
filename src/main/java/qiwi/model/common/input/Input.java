package qiwi.model.common.input;

import java.sql.Date;

public class Input {
    protected Integer id;
    protected String author = "";
    protected String name = "";
    protected Date found = Date.valueOf("1970-1-1");
    protected String description = "";
    protected Date start = Date.valueOf("1970-1-1");
    protected Date end = Date.valueOf("1970-1-1");

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getFound() {
        return found;
    }

    public void setFound(Date found) {
        this.found = found;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
