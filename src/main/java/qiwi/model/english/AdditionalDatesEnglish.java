package qiwi.model.english;

import qiwi.model.common.AdditionalDates;
import qiwi.model.common.book.FinishedBook;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "`additional_dates_english`")
public class AdditionalDatesEnglish extends AdditionalDates {
    @ManyToOne
    @JoinColumn(name = "finished_book_id", insertable = false, updatable = false)
    private FinishedBookEnglish finishedBook;

    @Override
    public void setFinishedBook(FinishedBook finishedBook) {
        this.finishedBook = (FinishedBookEnglish) finishedBook;
    }
}
