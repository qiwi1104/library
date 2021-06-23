package qiwi.model.russian;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;

@Entity
@Table(name = "`finished_books_russian")
public class BookRussian {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String author;
    private Date start;
    private Date end;
    private String start_info;
    private String end_info;
    private String found;

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

    public String getStart_info() {
        return start_info;
    }

    public String getEnd_info() {
        return end_info;
    }

    public String getFound() {
        return found;
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

    public void setStart_info(String start_info) {
        this.start_info = start_info;
    }

    public void setEnd_info(String end_info) {
        this.end_info = end_info;
    }

    public void setFound(String found) {
        this.found = found;
    }

    @Override
    public String toString() {
        return id + " " + author + " " + name;
    }
}
