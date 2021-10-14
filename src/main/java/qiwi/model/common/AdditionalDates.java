package qiwi.model.common;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Date;
import java.util.Objects;

@MappedSuperclass
public class AdditionalDates {
    @Id
    private Integer id;
    @Column(name = "finished_book_id")
    private Integer finishedBookId;
    private Date start;
    private Date end;

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
