package qiwi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import qiwi.model.book.FinishedBook;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "`additional_dates`")
@JsonIgnoreProperties({"id", "finishedBook"})
public class AdditionalDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "M/d/yy")
    private Date start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "M/d/yy")
    private Date end;

    @ManyToOne
    @JoinColumn(name = "finished_book_id")
    private FinishedBook finishedBook;

    public Integer getId() {
        return id;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public FinishedBook getFinishedBook() {
        return finishedBook;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setFinishedBook(FinishedBook book) {
        this.finishedBook = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdditionalDate)) return false;

        AdditionalDate that = (AdditionalDate) o;

        if (!start.equals(that.start)) return false;
        if (!end.equals(that.end)) return false;
        return finishedBook.equals(that.finishedBook);
    }

    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + finishedBook.hashCode();
        return result;
    }
}
