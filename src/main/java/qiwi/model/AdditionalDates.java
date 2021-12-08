package qiwi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import qiwi.model.book.FinishedBook;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "`additional_dates`")
@JsonIgnoreProperties({"id", "finishedBookId"})
public class AdditionalDates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "finished_book_id")
    private Integer finishedBookId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "M/d/yy")
    private Date start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "M/d/yy")
    private Date end;

    @ManyToOne
    @JoinColumn(name = "finished_book_id", insertable = false, updatable = false, nullable = false)
    private FinishedBook finishedBook;

    public Integer getId() {
        return id;
    }

    public Integer getFinishedBookId() {
        return finishedBookId;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFinishedBookId(Integer finishedBookId) {
        this.finishedBookId = finishedBookId;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdditionalDates)) return false;
        AdditionalDates dates = (AdditionalDates) o;
        return finishedBookId.equals(dates.finishedBookId) &&
                start.equals(dates.start) &&
                end.equals(dates.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(finishedBookId, start, end);
    }
}
