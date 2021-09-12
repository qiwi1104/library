package qiwi.model.russian;

import qiwi.model.common.AdditionalDates;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "`additional_dates_russian`")
public class AdditionalDatesRussian extends AdditionalDates {
    @ManyToOne
    @JoinColumn(name = "finished_book_id", insertable = false, updatable = false)
    private FinishedBookRussian finishedBook;
}
