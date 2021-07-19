package qiwi.model.spanish;

import qiwi.model.common.AdditionalDates;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "`additional_dates_spanish`")
public class AdditionalDatesSpanish extends AdditionalDates {
    @ManyToOne
    @JoinColumn(name = "finished_book_id", insertable = false, updatable = false)
    private FinishedBookSpanish finishedBook;

    public AdditionalDatesSpanish() {super();}

    public AdditionalDatesSpanish(Integer id, Integer finishedBookId, Date start, Date end) {super(id, finishedBookId, start, end);}
}
