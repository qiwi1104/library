package qiwi.model.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import qiwi.model.AdditionalDates;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "`finished_books`")
public class FinishedBook extends Book implements Cloneable {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "M/d/yy")
    private Date start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "M/d/yy")
    private Date end;
    @JsonProperty("additional_dates")
    @OneToMany(mappedBy = "finishedBook", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdditionalDates> additionalDates;

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public List<AdditionalDates> getAdditionalDates() {
        return additionalDates;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setAdditionalDates(List<AdditionalDates> dates) {
        this.additionalDates = dates;
    }

    public boolean hasDates(AdditionalDates dates) {
        return additionalDates.contains(dates) || dates.getStart().equals(start) || dates.getEnd().equals(end);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        FinishedBook book = (FinishedBook) o;

        return name.equals(book.name) && author.equals(book.author);
    }

    @Override
    public FinishedBook clone() {
        try {
            return (FinishedBook) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return new FinishedBook();
    }
}
