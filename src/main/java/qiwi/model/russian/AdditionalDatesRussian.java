package qiwi.model.russian;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "`additional_dates_russian`")
public class AdditionalDatesRussian extends qiwi.model.common.AdditionalDates {
    @ManyToOne
    @JoinColumn(name = "finished_book_id", insertable = false, updatable = false)
    private FinishedBookRussian finishedBook;

    public AdditionalDatesRussian() {super();}

    public AdditionalDatesRussian(Integer id, Integer finishedBookId, Date start, Date end) {super(id, finishedBookId, start, end);}
}